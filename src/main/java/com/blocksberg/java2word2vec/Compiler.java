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
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.deeplearning4j.clustering.cluster.Cluster;
import org.deeplearning4j.clustering.cluster.ClusterSet;
import org.deeplearning4j.clustering.cluster.Point;
import org.deeplearning4j.clustering.kmeans.KMeansClustering;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.EndingPreProcessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jh
 */
public class Compiler {

    public static void main(String[] args) throws IOException, WriterException {
        final CommandLine commandLine = parseCommandLine(args);
        final String fileName = commandLine.getOptionValue("o");
        final String jsonFileName = commandLine.getOptionValue("j");
        final com.blocksberg.java2word2vec.compilers.Compiler
                compiler = new com.blocksberg.java2word2vec.compilers.Compiler(fileName);
        final List<Path> sourceDirs = Arrays.asList(commandLine.getOptionValues("i")).stream().map(d -> new File(d)
                .toPath()).collect(Collectors.toList());

        CompilerBundle compilerBundle = createParserFactory(commandLine.getOptionValue("m"));
        System.out.println("compile " + sourceDirs + " with " + compilerBundle.getClass().getSimpleName());
        final KnownTypesLibrary types = compile(compiler, sourceDirs, compilerBundle);

        createJsonOutput(jsonFileName, types);



    }

    private static void prepareNNAndFeedCompiledFile(String fileName, KnownTypesLibrary types, String jsonFileName)
            throws IOException, WriterException {
        System.out.println("Load data...");
        File file = new File(fileName);
        SentenceIterator iter = new LineSentenceIterator(file);
        /*iter.setPreProcessor(new SentencePreProcessor() {
            @Override
            public String preProcess(String sentence) {
                return sentence.toLowerCase();
            }
        });*/


        System.out.println("Tokenize data....");
        final EndingPreProcessor preProcessor = new EndingPreProcessor();
        TokenizerFactory tokenizer = new DefaultTokenizerFactory();
        tokenizer.setTokenPreProcessor(new TokenPreProcess() {
            @Override
            public String preProcess(String token) {
                //token = token.toLowerCase();
                String base = preProcessor.preProcess(token);
                base = base.replaceAll("\\d", "d");
                if (base.endsWith("ly") || base.endsWith("ing"))
                    System.out.println();
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
        KMeansClustering kMeansClustering = KMeansClustering.setup(10, 15, "cosine");
        final ClusterSet clusterSet = kMeansClustering.applyTo(wordPoints);

        List<String> clusterIds = clusterSet.getClusters().stream().map(Cluster::getId).collect(Collectors.toList());
        Map<String, Integer> clusterIdMap = new HashMap<>();
        for (int i = 0; i < clusterIds.size(); i++) {
            clusterIdMap.put(clusterIds.get(i), i);
        }

        clusterSet.getClusters().forEach(
                c -> c.getPoints().forEach(p -> types.getType(p.getId()).setClusterId(clusterIdMap.get(c.getId())
                )));

        System.out.println("sout endet");

    }

    private static void createJsonOutput(String jsonFileName, KnownTypesLibrary types)
            throws IOException, WriterException {
        final PathWalker pathWalker = new PathWalker(0, ".");
        types.allTypes().forEach(t -> pathWalker.addPath(t.fullQualifiedName(), t.getClusterId().toString()));


        final FileWriter fileWriter = new FileWriter(jsonFileName);
        final JsonWriter jsonWriter = JsonWriter.of(fileWriter);

        final TreeNodeJsonWriter treeNodeJsonWriter = new TreeNodeJsonWriter();

        final RootTreeNode tree = pathWalker.getTree();

        treeNodeJsonWriter.transformRootTreeToJson(jsonWriter, tree);
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
        options.addOption(Option.builder("j").longOpt("json").hasArg(true).required(true).desc("json output").build());

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