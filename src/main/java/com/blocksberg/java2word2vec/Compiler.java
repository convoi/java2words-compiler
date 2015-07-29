package com.blocksberg.java2word2vec;

import ce.payback.rainbow.PathWalker;
import ce.payback.rainbow.tree.RootTreeNode;
import ce.payback.rainbow.writer.JsonWriter;
import ce.payback.rainbow.writer.TreeNodeJsonWriter;
import ce.payback.rainbow.writer.WriterException;
import com.blocksberg.java2word2vec.compilers.CompilerBundle;
import com.blocksberg.java2word2vec.compilers.java7.Java7CompilerBundle;
import com.blocksberg.java2word2vec.compilers.java7.Java7SemanticsBundle;
import com.blocksberg.java2word2vec.compilers.java7.KnownTypesLibrary;
import com.blocksberg.java2word2vec.compilers.java8.Java8CompilerBundle;
import com.blocksberg.java2word2vec.model.Project;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jh
 */
public class Compiler {

    public static void main(String[] args) throws IOException, WriterException {
        final CommandLine commandLine = parseCommandLine(args);
        final String fileName = commandLine.getOptionValue("o");
        final String jsonFileName = commandLine.getOptionValue("j");
        final String classes = commandLine.getOptionValue("c");
        final com.blocksberg.java2word2vec.compilers.Compiler
                compiler = new com.blocksberg.java2word2vec.compilers.Compiler(fileName);
        final List<Path> sourceDirs = Arrays.asList(commandLine.getOptionValues("i")).stream().map(d -> new File(d)
                .toPath()).collect(Collectors.toList());
        final String word2vecPath = commandLine.getOptionValue("w");

        CompilerBundle compilerBundle = createParserFactory(commandLine.getOptionValue("m"));
        System.out.println("compile " + sourceDirs + " with " + compilerBundle.getClass().getSimpleName());
        final KnownTypesLibrary types = compile(compiler, sourceDirs, compilerBundle);

        System.out.printf("Output of running %s is:", Arrays.toString(args));
        File tempFile = runWord2Vec(fileName, word2vecPath, classes);
        updateTypeClasses(tempFile.toPath(), types);
        createJsonOutput(jsonFileName, types, new Project(sourceDirs.get(0).getFileName().toString(), Arrays.asList
                ("methods", "fields")));


    }

    private static File runWord2Vec(String fileName, String word2vecPath, String classes) throws IOException {
        File tempFile = File.createTempFile("neuronalNetwork", "out");


        Process process = Runtime.getRuntime().exec(word2vecPath + " " +
                "-train " + fileName + " -output " + tempFile.getAbsolutePath() +
                " -cbow 1 -size 200 -window 5 -negative 25 -hs 0 -sample 1e-4 -threads 20 -iter 15 -classes " +
                "" + classes);
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;


        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        is.close();
        return tempFile;
    }

    private static void updateTypeClasses(Path absolutePath, KnownTypesLibrary types) throws IOException {
        Stream<String> lines = Files.lines(absolutePath);
        lines.forEach(l -> {
            final String[] splitted = l.split(" ");
            if (splitted.length > 1 && splitted[0] != null && !splitted[0].isEmpty()) {
                types.getType(splitted[0]).ifPresent(t -> t.setClusterId(Integer.valueOf(splitted[1])));
            }
        });

    }

    /*private static void prepareNNAndFeedCompiledFile(String fileName, KnownTypesLibrary types, String jsonFileName)
            throws IOException, WriterException {
        System.out.println("Load data...");
        File file = new File(fileName);
        SentenceIterator iter = new LineSentenceIterator(file);

        System.out.println("Tokenize data....");
        final EndingPreProcessor preProcessor = new EndingPreProcessor();
        TokenizerFactory tokenizer = new DefaultTokenizerFactory();
        tokenizer.setTokenPreProcessor(new TokenPreProcess() {
            @Override
            public String preProcess(String token) {
                String base = preProcessor.preProcess(token);
                base = base.replaceAll("\\d", "d");
                return base;

            }
        });

        int batchSize = 1000;
        int iterations = 30;
        int layerSize = 300;

        System.out.println("Build model....");
        Word2Vec vec = new Word2Vec.Builder()
                .batchSize(batchSize) //# words per minibatch.
                .sampling(1e-5) // negative sampling. drops words out
                .minWordFrequency(5) //
                .useAdaGrad(false) //
                .layerSize(layerSize) // word feature vector size
                .iterations(iterations) // # iterations to train
                .learningRate(0.025) //
                .minLearningRate(1e-2) // learning rate decays wrt # words. floor learning
                .negativeSample(10) // sample size 10 words
                .iterate(iter) //
                .tokenizerFactory(tokenizer)
                .build();

        vec.fit();
        final List<Point> wordPoints = vec.vocab().words().stream().map(w -> {
            final INDArray wordVectorMatrix = vec.getWordVectorMatrix(w);
            return new Point(w, wordVectorMatrix);
        }).collect(Collectors.toList());

        System.out.println("clustering...");
        KMeansClustering kMeansClustering = KMeansClustering.setup(10, 5, "cosine"); // cosine

        wordPoints.forEach(x -> System.out.println(x.getArray().length() + " " + x.getId()));
        final ClusterSet clusterSet = kMeansClustering.applyTo(wordPoints);

        System.out.println("applyTo ended");
        List<String> clusterIds = clusterSet.getClusters().stream().map(Cluster::getId).collect(Collectors.toList());
        Map<String, Integer> clusterIdMap = new HashMap<>();
        for (int i = 0; i < clusterIds.size(); i++) {
            clusterIdMap.put(clusterIds.get(i), i);
        }

        clusterSet.getClusters().forEach(
                c -> c.getPoints().forEach(p -> System.out.println(p.getId() + " " + clusterIdMap.get(c.getId()))));


                c -> c.getPoints().forEach(p -> types.getType(p.getId()).setClusterId(clusterIdMap.get(c.getId())
                )));

        System.out.println("sout endet");

    }*/

    private static void createJsonOutput(String jsonFileName, KnownTypesLibrary types, Project project)
            throws IOException, WriterException {
        final PathWalker pathWalker = new PathWalker(0, ".");
        types.allTypes().forEach(t -> pathWalker.addPath(t.fullQualifiedName(), t.getClusterId(), t.getStatistics()));


        final FileWriter fileWriter = new FileWriter(jsonFileName);
        final JsonWriter jsonWriter = JsonWriter.of(fileWriter);

        final TreeNodeJsonWriter treeNodeJsonWriter = new TreeNodeJsonWriter();

        final RootTreeNode tree = pathWalker.getTree();

        treeNodeJsonWriter.transformRootTreeToJson(jsonWriter, tree, project);
    }


    private static KnownTypesLibrary compile(com.blocksberg.java2word2vec.compilers.Compiler compiler,
                                             List<Path> sourceDirs,
                                             CompilerBundle compilerBundle) throws IOException {
        sourceDirs.forEach(dir -> {
            try {
                compiler.walk(dir, compilerBundle);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        compiler.close();
        return compiler.getKnownTypesLibrary();
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


    private static CommandLine parseCommandLine(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder("o").longOpt("out").hasArg(true).required(true).desc("compile words file")
                .build());
        options.addOption(Option.builder("i").longOpt("in").hasArg(true).required(true).valueSeparator(' ').desc
                ("directories with java files").build());
        options.addOption(Option.builder("h").longOpt("help").hasArg(false).required(false).desc("prints usage")
                .build());
        options.addOption(Option.builder("m").longOpt("mode").hasArg(true).required(false).desc("mode: java7 / " +
                "java8").build());
        options.addOption(Option.builder("j").longOpt("json").hasArg(true).required(true).desc("json output").build());
        options.addOption(Option.builder("w").longOpt("word2vec").hasArg(true).required(true).desc("path to " +
                "word2vec").build());
        options.addOption(Option.builder("c").longOpt("classes").hasArg(true).required(true).desc("number of " +
                "classes").build());
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