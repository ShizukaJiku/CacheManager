package datasetfilter.fields;

import datasetfilter.core.FieldFilter;

import java.util.Objects;

public class StartsWithFilter implements FieldFilter<String> {

    private final String prefix;

    public StartsWithFilter(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean test(String value) {
        return value != null && value.startsWith(prefix);
    }

    @Override
    public String fingerprint() {
        return "startsWith:" + prefix;
    }

    @Override
    public boolean isIncludedIn(FieldFilter<String> other) {
        if (!(other instanceof StartsWithFilter o)) return false;
        return this.prefix.startsWith(o.prefix);
    }

    @Override
    public boolean isEquivalentTo(FieldFilter<String> other) {
        return other instanceof StartsWithFilter o && Objects.equals(this.prefix, o.prefix);
    }

    public String getPrefix() {
        return prefix;
    }
}
