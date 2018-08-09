#!/bin/bash

function usage() {
	echo "usage: $0 [options]"
	echo "-d	dictionary file"
	echo "-i	input raw file"
	echo "-o	output training set"
	exit 1
}

if [[ $# -eq 0 ]]; then
	usage
fi

DICT_FILE=""
RAW=""
TRAINSET=""
while test $# -gt 0; do
	case "$1" in
		-d)
			shift
			DICT_FILE=$1
			shift ;;
		-i)
			shift
			RAW=$1
			shift ;;
		-o)
			shift
			TRAINSET=$1
			shift ;;
		*)
			break
			;;
	esac
done

if [[ -z ${DICT_FILE} ]] || [[ -z ${RAW} ]] || [[ -z ${TRAINSET} ]]; then
	usage
fi

# env.
CONFIG="../config"
DICTIONARY="../resource/dictionary"
NLPCONFIG="../config/nlp"
NLPRESOURCE="../resource/nlp"
CRFLIB="/Users/sindongboy/Documents/workspace/crfpp/java"
ENV="${CONFIG}:${DICTIONARY}:${NLPCONFIG}:${NLPRESOURCE}:${CRFLIB}"
CP="${ENV}"

# dependencies
LIB_HOME="../lib"
for libs in `ls -1 ${LIB_HOME}`
do
	CP="${CP}:${LIB_HOME}/${libs}"
done

TARGET="../target/entity-finder-1.0.1-SNAPSHOT.jar"
CP="${CP}:${TARGET}"

# ======================= #
# Create Training Set
# ======================= #
PACKAGE="com.skplanet.nlp"
RUNNER="driver.CRFTrainer"
java -Xmx4G -Dfile.encoding=UTF-8 -Djava.library.path=${CRFLIB} -cp ${CP} ${PACKAGE}.${RUNNER} -d ${DICT_FILE} -i ${RAW} -o ${TRAINSET} -r 
