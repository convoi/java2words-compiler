package com.blocksberg.java2word2vec.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author jh
 */
public class TypeTest {

    @Test
    public void testShortName() throws Exception {
        final Type type = new Type("com.blocksberg.foo.Bla");
        assertThat(type.shortName(), is("Bla"));
    }

    @Test
    public void testPackageName() {
        final Type type = new Type("com.blocksberg.foo.Bla");
        assertThat(type.packageName(), is("com.blocksberg.foo"));
    }
}