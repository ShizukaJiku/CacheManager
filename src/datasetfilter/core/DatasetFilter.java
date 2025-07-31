package datasetfilter.core;

public interface DatasetFilter<E> extends Criterion<E> {

    boolean isIncludedIn(DatasetFilter<E> other);

    boolean isEquivalentTo(DatasetFilter<E> other);
}
