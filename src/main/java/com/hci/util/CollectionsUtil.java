package com.hci.util;

import java.util.HashSet;
import java.util.Set;

public class CollectionsUtil {
    public static <T> Set<T> asSet(T... args) {
        Set<T> newSet = new HashSet<T>(args.length);
        for (T arg : args) {
            newSet.add(arg);
        }

        return newSet;
    }
}
