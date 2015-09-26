package org.cleanarchitecture.common.dao;

import java.util.HashMap;
import java.util.Map;

public class RepositoryRegistry {
    private static Map<Class, Repository> repositoryMap = new HashMap<Class, Repository>();


    public static <R extends Repository> void registerRepository(R repo) {
        repositoryMap.put(repo.getClass(), repo);
    }

    @SuppressWarnings("unchecked")
    public static <R extends Repository> R repository(Class clazz) {
        return (R)repositoryMap.get(clazz);
    }

    public static <R extends Repository> void registerRepository(Class<R> repoClazz, R repo) {
        repositoryMap.put(repoClazz, repo);
    }
}
