package datasetfilter.core;

public interface FieldFilter<F> extends Criterion<F> {

    boolean isIncludedIn(FieldFilter<F> other);

    boolean isEquivalentTo(FieldFilter<F> other);
}
