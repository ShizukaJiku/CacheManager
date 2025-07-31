package datasetfilter.fields;

import datasetfilter.core.FieldFilter;

import java.util.Objects;

public class EqualsFilter<F> implements FieldFilter<F> {

    private final F expected;

    public EqualsFilter(F expected) {
        this.expected = expected;
    }

    @Override
    public boolean test(F value) {
        return Objects.equals(expected, value);
    }

    @Override
    public String fingerprint() {
        return "equals:" + expected;
    }

    @Override
    public boolean isIncludedIn(FieldFilter<F> other) {
        if (!(other instanceof EqualsFilter<?> o)) return false;
        return Objects.equals(expected, o.expected);
    }

    @Override
    public boolean isEquivalentTo(FieldFilter<F> other) {
        return other instanceof EqualsFilter<?> o && Objects.equals(expected, o.expected);
    }

    public F getExpected() {
        return expected;
    }
}
