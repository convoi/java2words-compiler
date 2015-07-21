package com.blocksberg.java2word2vec;

import com.blocksberg.java2word2vec.grammar.Java8Lexer;
import com.blocksberg.java2word2vec.grammar.Java8Parser;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenFactory;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.UnbufferedCharStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author jh
 */
public class Compiler {

    private final File outputFile;
    private FileWriter fileWriter;
    private ParseTreeWalker parseTreeWalker;

    public static void main(String[] args) throws IOException {
        final CommandLine commandLine = parseCommandLine(args);
        final Compiler compiler = new Compiler(commandLine.getOptionValue("o"));
        final List<String> sourceDirs = Arrays.asList(commandLine.getOptionValues("i"));
        sourceDirs.forEach(dir -> {
            try {
                compiler.walk(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        compiler.close();
    }


    private static CommandLine parseCommandLine(String[] args)  {
        Options options = new Options();
        options.addOption(Option.builder("o").longOpt("out").hasArg(true).required(true).desc("compile words file")
                .build());
        options.addOption(Option.builder("i").longOpt("in").hasArg(true).required(true).valueSeparator(' ').desc
                ("directories with java files").build());
        options.addOption(Option.builder("h").longOpt("help").hasArg(false).required(false).desc("prints usage")
                .build());

        try {

            final CommandLine commandLine = new DefaultParser().parse(options, args);
            if (commandLine.hasOption('h')) {
                printUsage(options);
                System.exit(0);
            }
            return commandLine;
        } catch (ParseException e) {
            printUsage(options);
            System.exit(0);
            return null;
        }
    }

    private static void printUsage(Options options) {
        new HelpFormatter().printHelp("java -jar java2words-compile.jar", options);
    }

    public Compiler(String outfile) throws IOException {
        outputFile = new File(outfile);
        if (outputFile.exists()) {
            outputFile.delete();
        }
        outputFile.createNewFile();
        fileWriter = new FileWriter(outputFile);
        parseTreeWalker = ParseTreeWalker.DEFAULT;
    }


    private void walk(String dir) throws IOException {
        final Stream<Path> pathStream = Files
                .walk(new File(dir).toPath())
                .filter(p -> p.toString().endsWith(".java"));
        pathStream.forEach(p -> {
            try {
                compileJava7(p);
            } catch (IOException e) {

            }
        });
    }

    private void close() throws IOException {
        fileWriter.close();
    }

    private void compile(Path fileToCompile) throws IOException {
        final Java8TypesListener compiler = new Java8TypesListener();
        final ParseTree compilationUnit = parse(fileToCompile, new Java8ParserFactory());


        parseTreeWalker.walk(compiler, compilationUnit);

        fileWriter.append(compiler.getStringBuilder().toString());
        fileWriter.flush();

    }

    private void compileJava7(Path fileToCompile) throws IOException {
        final JavaTypesListener compiler = new JavaTypesListener();
        final ParseTree compilationUnit = parse(fileToCompile, new Java7ParserFactory());


        parseTreeWalker.walk(compiler, compilationUnit);

        fileWriter.append(compiler.getStringBuilder().toString());
        fileWriter.flush();
    }

    private String getOutPath(String path, String outputDir) {
        final File file = new File(path);
        final File outputDirectory = new File(outputDir);
        outputDirectory.mkdirs();
        return new File(outputDir, file.getName()).getAbsolutePath();
    }

    private ParseTree parse(Path path, ParserFactory parserFactory) throws
            FileNotFoundException {
        final File file = path.toFile();
        try (final FileInputStream fileInputStream = new FileInputStream(file);
        ) {
            final UnbufferedCharStream input = new UnbufferedCharStream(fileInputStream);
            final Lexer javaLexer = parserFactory.createLexer(input);
            javaLexer.setTokenFactory(new CommonTokenFactory(true));
            final CommonTokenStream commonTokenStream = new CommonTokenStream(javaLexer);
            final Parser parser = parserFactory.createParser(commonTokenStream);
            //parser.addParseListener(compiler);

            parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
            try {
                return parserFactory.rootElement(parser); // STAGE 1
            }
            catch (Exception ex) {
                commonTokenStream.reset(); // rewind input stream
                parser.reset();
                parser.getInterpreter().setPredictionMode(PredictionMode.LL);
                return parserFactory.rootElement(parser);  // STAGE 2
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