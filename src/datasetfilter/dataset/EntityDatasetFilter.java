package datasetfilter.dataset;

import datasetfilter.core.DatasetFilter;
import datasetfilter.core.FieldBinding;

import java.util.List;
import java.util.Objects;

//TODO: Revisar logica
public class EntityDatasetFilter<T> implements DatasetFilter<T> {

    private final List<FieldBinding<T, ?>> bindings;

    public EntityDatasetFilter(List<FieldBinding<T, ?>> bindings) {
        this.bindings = bindings;
    }

    @Override
    public boolean test(T value) {
        for (FieldBinding<T, ?> binding : bindings) {
            if (!binding.test(value)) return false;
        }
        return true;
    }

    @Override
    public boolean isEquivalentTo(DatasetFilter<T> other) {
        if (!(other instanceof EntityDatasetFilter<T> o)) return false;

        if (bindings.size() != o.bindings.size()) return false;

        for (var thisBinding : bindings) {
            FieldBinding<T, ?> otherBinding = findByFieldName(thisBinding.getFieldName(), o.getBindings());
            if (otherBinding == null || !thisBinding.isEquivalentTo(otherBinding)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isIncludedIn(DatasetFilter<T> other) {
        if (!(other instanceof EntityDatasetFilter<T> o)) return false;

        for (FieldBinding<T, ?> thisBinding : bindings) {
            FieldBinding<T, ?> otherBinding = findByFieldName(thisBinding.getFieldName(), o.bindings);
            if (otherBinding == null || !thisBinding.isIncludedIn(otherBinding)) {
                return false;
            }
        }
        return true;
    }

    private FieldBinding<T, ?> findByFieldName(String fieldName, List<FieldBinding<T, ?>> list) {
        for (var binding : list) {
            if (Objects.equals(binding.getFieldName(), fieldName)) {
                return binding;
            }
        }
        return null;
    }

    @Override
    public String fingerprint() {
        return bindings.stream()
                .map(FieldBinding::fingerprint)
                .sorted()
                .reduce((a, b) -> a + "|" + b)
                .orElse("");
    }

    public List<FieldBinding<T, ?>> getBindings() {
        return bindings;
    }
}
