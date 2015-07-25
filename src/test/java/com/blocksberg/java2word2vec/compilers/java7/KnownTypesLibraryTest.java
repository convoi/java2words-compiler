package com.blocksberg.java2word2vec.compilers.java7;

import com.blocksberg.java2word2vec.model.Type;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author jh
 */
public class KnownTypesLibraryTest {
    final KnownTypesLibrary knownTypesLibrary = new KnownTypesLibrary();

    @Test
    public void aTypeKnownShouldBeFound() throws Exception {
        knownTypesLibrary.addType(new Type("com.blocksberg.foo.Bar"));
        final Optional<Type> bar =
                knownTypesLibrary.findTypeInPackages(Collections.singleton("com.blocksberg.foo"), "Bar");
        assertTrue(bar.isPresent());
    }

    @Test
    public void aTypeNotKnownShouldNotBeFound() throws Exception {
        knownTypesLibrary.addType(new Type("com.blocksberg.foo.Bar"));
        final Optional<Type> bar =
                knownTypesLibrary.findTypeInPackages(Collections.singleton("com.blocksberg.foo"), "Baz");
        assertFalse(bar.isPresent());
    }
}