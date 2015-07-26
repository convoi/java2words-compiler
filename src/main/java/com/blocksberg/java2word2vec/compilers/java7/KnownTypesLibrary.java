package com.blocksberg.java2word2vec.compilers.java7;

import com.blocksberg.java2word2vec.model.Type;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Contains all known types and can look them up by package name.
 *
 * @author jh
 */
public class KnownTypesLibrary {

    private Multimap<String, Type> packages;

    public KnownTypesLibrary() {
        packages = HashMultimap.create();
    }

    public void addType(Type type) {
        packages.put(type.packageName(), type);
    }

    /**
     * finds a type by its shortname looking in a given list of packages.
     *
     * @param searchPackages
     * @param shortName
     * @return
     */
    public Optional<Type> findTypeInPackages(Collection<String> searchPackages, String shortName) {
        return packages.asMap().entrySet().stream()
                .filter(e -> searchPackages.contains(e.getKey()))
                .flatMap(e -> e.getValue().stream().filter(t -> t.shortName().equals(shortName))).findFirst();

    }

    /**
     * returns true if a shortname can be looked up in this library.
     *
     * @param shortName
     * @return
     */
    public boolean knows(String shortName) {
        return typesMatchingShortNameStream(shortName).findAny().isPresent();
    }

    private Stream<Type> typesMatchingShortNameStream(String shortName) {
        return packages.values().stream().filter(t -> t.shortName().equals(shortName));
    }

    public Optional<Type> findFirst(String shortName) {
        return typesMatchingShortNameStream(shortName).findFirst();
    }
}
