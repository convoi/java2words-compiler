package com.blocksberg.java2word2vec.compilers.java7;

import com.blocksberg.java2word2vec.model.Type;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * Contains all known types and can look them up by package name.
 *
 * @author jh
 */
public class KnownTypesLibrary {
    private Set<Type> knownTypes;

    private Multimap<String, Type> packages;

    public KnownTypesLibrary() {
        packages = HashMultimap.create();
    }

    public void addType(Type type) {
        packages.put(type.packageName(), type);
    }

    Optional<Type> findTypeInPackages(Collection<String> searchPackages, String shortName) {
        return packages.asMap().entrySet().stream()
                .filter(e -> searchPackages.contains(e.getKey()))
                .flatMap(e -> e.getValue().stream().filter(t -> t.shortName().equals(shortName))).findFirst();

    }
}
