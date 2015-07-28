package com.blocksberg.java2word2vec;

import ce.payback.rainbow.PathWalker;
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
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.deeplearning4j.clustering.algorithm.strategy.ClusteringStrategy;
import org.deeplearning4j.clustering.cluster.Cluster;
import org.deeplearning4j.clustering.cluster.ClusterSet;
import org.deeplearning4j.clustering.cluster.Point;
import org.deeplearning4j.clustering.kmeans.KMeansClustering;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.EndingPreProcessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jh
 */
public class Compiler {

    public static void main(String[] args) throws IOException {
        final CommandLine commandLine = parseCommandLine(args);
        final String fileName = commandLine.getOptionValue("o");
        final com.blocksberg.java2word2vec.compilers.Compiler
                compiler = new com.blocksberg.java2word2vec.compilers.Compiler(fileName);
        final List<Path> sourceDirs = Arrays.asList(commandLine.getOptionValues("i")).stream().map(d -> new File(d)
                .toPath()).collect(Collectors.toList());

        CompilerBundle compilerBundle = createParserFactory(commandLine.getOptionValue("m"));
        System.out.println("compile " + sourceDirs + " with " + compilerBundle.getClass().getSimpleName());
        compile(compiler, sourceDirs, compilerBundle);

        File tempFile = File.createTempFile("neuronalNetwork", "out");

        /*try  {
            Runtime rt = Runtime.getRuntime() ;
            Process p = new ProcessBuilder("word2vec/word2vec.exe" ,"-train " + fileName + " -output out/" + tempFile.getName() +  " -cbow 1 -size 200 -window 8 -negative 25 -hs 0 -sample 1e-4 -threads 20 -iter 15 -classes 500").start();
            InputStream os = p.getInputStream();
            while(os.available() != -1) {
                System.out.print(os.read());
            }
        } catch (Exception exc) {
    /*handle exception
            System.out.println(exc.getMessage());
        }*/


        Process process = new ProcessBuilder("D:\\git\\java2words-compiler\\word2vec\\word2vec.exe", "-train " + fileName + " -output out/" + tempFile.getName() +  " -cbow 1 -size 200 -window 8 -negative 25 -hs 0 -sample 1e-4 -threads 20 -iter 15 -classes 500").start();
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        System.out.printf("Output of running %s is:", Arrays.toString(args));

        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

    }

    private static void prepareNNAndFeedCompiledFile(String fileName) throws IOException {
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



        System.out.println("sout endet");

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