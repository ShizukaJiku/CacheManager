package datasetfilter.fields;

import datasetfilter.core.FieldFilter;

import java.util.Objects;

public class ContainsFilter implements FieldFilter<String> {

    private final String substring;

    public ContainsFilter(String substring) {
        this.substring = substring;
    }

    @Override
    public boolean test(String value) {
        return value != null && value.contains(substring);
    }

    @Override
    public String fingerprint() {
        return "contains:" + substring;
    }

    @Override
    public boolean isIncludedIn(FieldFilter<String> other) {
        if (!(other instanceof ContainsFilter o)) return false;
        return o.substring.contains(this.substring);
    }

    @Override
    public boolean isEquivalentTo(FieldFilter<String> other) {
        return other instanceof ContainsFilter o && Objects.equals(this.substring, o.substring);
    }

    public String getSubstring() {
        return substring;
    }
}