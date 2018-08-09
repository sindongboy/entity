#!/bin/bash

function usage() {
	echo "usage: $0 [options]"
	echo "-d	dictionary file"
	echo "-m	mode [cli | file]"
	echo "-i	input file [ file mode only ]"
	echo "-o	output file [ file mode only ]"
	exit 1
}

if [[ $# -eq 0 ]]; then
	usage
fi

DICT_FILE=""
MODE=""
INPUT=""
OUTPUT=""

while test $# -gt 0; do
	case "$1" in
		-d)
			shift
			DICT_FILE=$1
			shift ;;
		-m)
			shift
			MODE=$1
			shift ;;
		-i)
			shift
			INPUT=$1
			shift ;;
		-o)
			shift
			OUTPUT=$1
			shift ;;
		*)
			break
			;;
	esac
done

if [[ -z ${DICT_FILE} ]] || [[ -z ${MODE} ]]; then
	usage
fi

# env.
CONFIG="../config"
DICTIONARY="../resource/dictionary"
NLPCONFIG="../config/nlp"
NLPRESOURCE="../resource/nlp"
CRFLIB="/Users/sindongboy/Dropbox/Documents/workspace/crfpp/java"
MODEL="../resource/models"
ENV="${CONFIG}:${DICTIONARY}:${NLPCONFIG}:${NLPRESOURCE}:${CRFLIB}:${MODEL}"
CP="${ENV}"

# dependencies
LIB_HOME="../lib"
for libs in `ls -1 ${LIB_HOME}`
do
	CP="${CP}:${LIB_HOME}/${libs}"
done

# Target
TARGET="../target/entity-finder-1.0.1-SNAPSHOT.jar" 
CP="${CP}:${TARGET}"

PACKAGE="com.skplanet.nlp"
RUNNER="driver.ProductFinderCLI"

if [[ ${MODE} == "file" ]]; then
	if [[ -z ${INPUT} ]] || [[ -z ${OUTPUT} ]]; then
		usage
	fi
	java -Xmx4G -Dfile.encoding=UTF-8 -Djava.library.path=${CRFLIB} -cp ${CP} ${PACKAGE}.${RUNNER} -d ${DICT_FILE} -m ${MODE} -i ${INPUT} -o ${OUTPUT} 
elif [[ ${MODE} == "cli" ]]; then
	java -Xmx4G -Dfile.encoding=UTF-8 -Djava.library.path=${CRFLIB} -cp ${CP} ${PACKAGE}.${RUNNER} -d ${DICT_FILE} -m ${MODE}
else
	usage
fi

