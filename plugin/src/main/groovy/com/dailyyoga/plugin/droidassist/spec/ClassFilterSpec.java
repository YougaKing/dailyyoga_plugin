package com.dailyyoga.plugin.droidassist.spec;

import org.apache.commons.io.FilenameUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * ClassFilterSpec with wildcard match
 */
@SuppressWarnings("WeakerAccess")
public class ClassFilterSpec {
    private Set<String> excludes = new HashSet<>();

    public void addExclude(String filter) {
        if (filter == null) {
            return;
        }
        filter = filter.trim();
        if (filter.equals("")) {
            return;
        }
        excludes.add(filter);
    }

    public void addExcludes(Collection<String> filters) {
        for (String filter : filters) {
            addExclude(filter);
        }
    }

    public Set<String> getExcludes() {
        return excludes;
    }

    private boolean isExcludeClass(String className) {
        if (excludes.isEmpty()) {
            return false;
        }
        for (String fi : excludes) {
            if (FilenameUtils.wildcardMatch(className, fi)) {
                return true;
            }
        }
        return false;
    }

    public boolean classAllowed(String className) {
        return !isExcludeClass(className);
    }

    @Override
    public String toString() {
        return "{" +
                "excludes=" + excludes +
                '}';
    }
}
