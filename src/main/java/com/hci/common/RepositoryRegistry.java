package com.hci.common;

import java.util.HashMap;
import java.util.Map;

public class RepositoryRegistry {
    private static Map<Class, Repository> repositoryMap = new HashMap<Class, Repository>();


    public static <R extends Repository> void registerRepository(R repo) {
        repositoryMap.put(repo.getClass(), repo);
    }

    public static <R extends Repository> R repository(Class clazz) {
        return (R)repositoryMap.get(clazz);
    }
}
