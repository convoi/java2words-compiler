package com.blocksberg.java2word2vec.compilers.java7;

import com.blocksberg.java2word2vec.compilers.Compiler;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author jh
 */
public class Java7CompilerTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void compilerShouldProduceCorrectWordFile() throws IOException, URISyntaxException {
        final Path path = makePath("/testsource/bla");
        assertThatCompilerProducesOutfile(path);
    }

    @Test
    public void compilerShouldProduceCorrectWordFileWithMethodParams() throws URISyntaxException, IOException {
        assertThatCompilerProducesOutfile(makePath("/testsource/methodparams"));
    }


    private Path makePath(String resourceName) throws URISyntaxException {
        return Paths.get(this.getClass().getResource(resourceName).toURI());
    }

    private void assertThatCompilerProducesOutfile(Path path) throws IOException {
        final Java7CompilerBundle compilerBundle = new Java7CompilerBundle();
        final File outFile = temporaryFolder.newFile();
        final Compiler compiler = new Compiler(outFile.toPath().toString());
        compiler.walk(path, Collections.emptyList(), compilerBundle);
        compiler.close();
        final String output = new String(Files.readAllBytes(outFile.toPath())).trim();
        final String expectedOutput = new String(Files.readAllBytes(path.resolve("outfile"))).replace('\n', ' ').trim();
        assertThat(output, is(expectedOutput));
    }
}
