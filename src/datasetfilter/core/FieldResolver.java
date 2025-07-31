package datasetfilter.core;

@FunctionalInterface
public interface FieldResolver<E, F> {
    F resolve(E entity);
}
