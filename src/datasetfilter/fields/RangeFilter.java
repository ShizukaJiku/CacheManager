package datasetfilter.fields;

import datasetfilter.core.FieldFilter;

import java.util.Objects;

public class RangeFilter<N extends Comparable<N>> implements FieldFilter<N> {

    private final N min;
    private final N max;

    public RangeFilter(N min, N max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean test(N value) {
        if (value == null) return false;
        if (min != null && value.compareTo(min) < 0) return false;
        if (max != null && value.compareTo(max) > 0) return false;
        return true;
    }

    @Override
    public String fingerprint() {
        return "range:[" + min + "," + max + "]";
    }

    @Override
    public boolean isIncludedIn(FieldFilter<N> other) {
        if (!(other instanceof RangeFilter<N> o)) return false;
        boolean minOk = (o.min == null || (this.min != null && this.min.compareTo(o.min) >= 0));
        boolean maxOk = (o.max == null || (this.max != null && this.max.compareTo(o.max) <= 0));
        return minOk && maxOk;
    }

    @Override
    public boolean isEquivalentTo(FieldFilter<N> other) {
        if (!(other instanceof RangeFilter<?> o)) return false;
        return Objects.equals(min, o.min)
                && Objects.equals(max, o.max);
    }

    public N getMin() {
        return min;
    }

    public N getMax() {
        return max;
    }
}
