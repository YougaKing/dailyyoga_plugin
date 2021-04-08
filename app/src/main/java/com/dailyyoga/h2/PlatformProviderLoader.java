package com.dailyyoga.h2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2019/02/28 10:35
 * @description:
 */
public class PlatformProviderLoader {

    public static <T> T load(
            Class<T> klass,
            Iterable<Class<?>> hardcoded) {
        List<T> candidates = loadAll(klass, hardcoded);
        if (candidates.isEmpty()) {
            return null;
        }
        return candidates.get(0);
    }

    public static <T> List<T> loadAll(
            Class<T> klass,
            Iterable<Class<?>> hardcoded) {
        Iterable<T> candidates = getCandidatesViaHardCoded(klass, hardcoded);

        List<T> list = new ArrayList<>();
        for (T current : candidates) {
            if (current != null) list.add(current);
        }

        return Collections.unmodifiableList(list);
    }


    static <T> Iterable<T> getCandidatesViaHardCoded(Class<T> klass, Iterable<Class<?>> hardcoded) {
        List<T> list = new ArrayList<>();
        for (Class<?> candidate : hardcoded) {
            list.add(create(klass, candidate));
        }
        return list;
    }

    static <T> T create(Class<T> klass, Class<?> rawClass) {
        try {
            return rawClass.asSubclass(klass).getConstructor().newInstance();
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
}
