package datasetfilter.core;

import java.util.Objects;
import java.util.function.Function;

public final class FieldBinding<T, F> {
    private final String fieldName;
    private final FieldFilter<F> filter;
    private final Function<T, F> resolver;

    public FieldBinding(
            String fieldName,
            FieldFilter<F> filter,
            Function<T, F> resolver
    ) {
        this.fieldName = fieldName;
        this.filter = filter;
        this.resolver = resolver;
    }

    public boolean test(T entity) {
        F fieldValue = resolver.apply(entity);
        return filter.test(fieldValue);
    }

    public String fingerprint() {
        return fieldName + ":" + filter.fingerprint();
    }

    public boolean isEquivalentTo(FieldBinding<T, ?> other) {
        if (verifyAnotherBinding(other)) return false;

        try {
            return filter.isEquivalentTo(getOtherFilter(other));
        } catch (ClassCastException e) {
            return false;
        }
    }

    public boolean isIncludedIn(FieldBinding<T, ?> other) {
        if (verifyAnotherBinding(other)) return false;

        try {
            return filter.isIncludedIn(getOtherFilter(other));
        } catch (ClassCastException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T, F> FieldFilter<F> getOtherFilter(FieldBinding<T, ?> other) {
        return (FieldFilter<F>) other.getFilter();
    }

    private boolean verifyAnotherBinding(FieldBinding<T, ?> other) {
        if (!Objects.equals(this.fieldName, other.fieldName)) return true;
        if (!Objects.equals(this.filter.getClass(), other.filter.getClass())) return true;
        return false;
    }


    public String getFieldName() {
        return fieldName;
    }

    public FieldFilter<F> getFilter() {
        return filter;
    }

    public Function<T, F> getResolver() {
        return resolver;
    }
}
