#!/bin/bash
numClasses=$1
input=$2
if [ -z $numClasses ]; then
	echo "please provide a number of classes"
	exit 1
fi
if [ -z $input ]; then
	echo "please provide an input file"
	exit 1
fi
echo making $numClasses classes from $input

word2vec -train $input -output camel-classes.txt -cbow 1 -size 200 -window 5 -negative 25 -hs 0 -sample 1e-4 -threads 20 -iter 15 -classes $numClasses
