package com.blocksberg.java2word2vec.compilers;

import org.antlr.v4.runtime.CommonTokenFactory;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.UnbufferedCharStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Compiles all files in a directory to some output file.
 */
public class Compiler {
    private final File outputFile;
    private final FileWriter fileWriter;
    private final ParseTreeWalker parseTreeWalker;

    public Compiler(String outfile) throws IOException {
        outputFile = new File(outfile);
        if (outputFile.exists()) {
            outputFile.delete();
        }
        outputFile.createNewFile();
        fileWriter = new FileWriter(outputFile);
        parseTreeWalker = ParseTreeWalker.DEFAULT;
    }

    /**
     * compiles all matching files within dir with the compiler.
     *
     * @param dir
     * @param compilerBundle
     * @throws IOException
     */
    public void walk(String dir, CompilerBundle compilerBundle) throws IOException {
        for (int pass = 0; pass < compilerBundle.numberOfPasses(); pass++) {
            System.out.println("running compilation pass " + pass);
            final int currentPass = pass;
            final Stream<Path> pathStream = Files
                    .walk(new File(dir).toPath())
                    .filter(p -> p.toString().endsWith(".java"));
            pathStream.forEach(p -> {
                try {
                    compile(p, compilerBundle, currentPass);
                } catch (IOException e) {

                }
            });
        }
    }

    /**
     * closes the compilers output.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        fileWriter.close();
    }

    private void compile(final Path fileToCompile, final CompilerBundle compilerBundle, final int pass) throws
            IOException {
        final ParseTree compilationUnit = parse(fileToCompile, compilerBundle);
        final ParseTreeListener compiler = compilerBundle.getCompilerInstance(pass);

        parseTreeWalker.walk(compiler, compilationUnit);

        fileWriter.append(compiler.toString());
        fileWriter.flush();
    }

    ParseTree parse(Path path, CompilerBundle compilerBundle) throws
            FileNotFoundException {
        final File file = path.toFile();
        try (final FileInputStream fileInputStream = new FileInputStream(file);
        ) {
            final UnbufferedCharStream input = new UnbufferedCharStream(fileInputStream);
            final Lexer javaLexer = compilerBundle.createLexer(input);
            javaLexer.setTokenFactory(new CommonTokenFactory(true));
            final CommonTokenStream commonTokenStream = new CommonTokenStream(javaLexer);
            final Parser parser = compilerBundle.createParser(commonTokenStream);
            //parser.addParseListener(compiler);

            parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
            try {
                return compilerBundle.rootElement(parser); // STAGE 1
            } catch (Exception ex) {
                commonTokenStream.reset(); // rewind input stream
                parser.reset();
                parser.getInterpreter().setPredictionMode(PredictionMode.LL);
                return compilerBundle.rootElement(parser);  // STAGE 2
                // if we parse ok, it's LL not SLL
            }
            //javaParser.getInterpreter().setPredictionMode(PredictionMode.SLL);
            //javaParser.setErrorHandler(new BailErrorStrategy());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}