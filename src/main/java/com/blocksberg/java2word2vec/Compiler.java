package com.blocksberg.java2word2vec;

import com.blocksberg.java2word2vec.compilers.CompilerBundle;
import com.blocksberg.java2word2vec.compilers.java7.Java7CompilerBundle;
import com.blocksberg.java2word2vec.compilers.java7.Java7SemanticsBundle;
import com.blocksberg.java2word2vec.compilers.java8.Java8CompilerBundle;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jh
 */
public class Compiler {

    public static void main(String[] args) throws IOException {
        final CommandLine commandLine = parseCommandLine(args);
        final com.blocksberg.java2word2vec.compilers.Compiler
                compiler = new com.blocksberg.java2word2vec.compilers.Compiler(commandLine.getOptionValue("o"));
        final List<Path> sourceDirs = Arrays.asList(commandLine.getOptionValues("i")).stream().map(d -> new File(d)
                .toPath()).collect(Collectors.toList());

        CompilerBundle compilerBundle = createParserFactory(commandLine.getOptionValue("m"));
        System.out.println("compile " + sourceDirs + " with " + compilerBundle.getClass().getSimpleName());
        compile(compiler, sourceDirs, compilerBundle);
    }

    private static void compile(com.blocksberg.java2word2vec.compilers.Compiler compiler, List<Path> sourceDirs,
                                CompilerBundle compilerBundle) throws IOException {
        sourceDirs.forEach(dir -> {
            try {
                compiler.walk(dir, compilerBundle);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        compiler.close();
    }

    private static CompilerBundle createParserFactory(String mode) {
        if (mode == null || mode.equals("java7")) {
            return new Java7CompilerBundle();
        } else if (mode.equals("java7semantics")) {
            return new Java7SemanticsBundle();
        } else if (mode.equals("java8")) {
            return new Java8CompilerBundle();
        } else {
            throw new IllegalArgumentException("there is no mode matching '" + mode + "'");
        }
    }


    private static CommandLine parseCommandLine(String[] args)  {
        Options options = new Options();
        options.addOption(Option.builder("o").longOpt("out").hasArg(true).required(true).desc("compile words file")
                .build());
        options.addOption(Option.builder("i").longOpt("in").hasArg(true).required(true).valueSeparator(' ').desc
                ("directories with java files").build());
        options.addOption(Option.builder("h").longOpt("help").hasArg(false).required(false).desc("prints usage")
                .build());
        options.addOption(Option.builder("m").longOpt("mode").hasArg(true).required(false).desc("mode: java7 / " +
                "java8").build());

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



    private String getOutPath(String path, String outputDir) {
        final File file = new File(path);
        final File outputDirectory = new File(outputDir);
        outputDirectory.mkdirs();
        return new File(outputDir, file.getName()).getAbsolutePath();
    }

}