#!/bin/bash

source ./env.sh

function usage() {
	echo "usage: $0 [options]"
	echo "-h	help"
	echo "-d	depth"
	echo "-q	category query"
	echo "-o	dictionary path"
	exit 1
}

DICT_EXT="dict"

while test $# -gt 0; do
	case "$1" in
		-h)
			shift
			usage
			;;
		-d)
			shift
			depth=$1
			shift ;;
		-q)
			shift
			query=$1
			shift ;;
		-o)
			shift
			output=$1
			shift ;;
		*)
			break
			;;
	esac
done

if [[ -z ${depth} ]] || [[ -z ${query} ]] || [[ -z ${output} ]]; then 
	usage
fi

#DICT_NAME=`cat ${MAP}/${CATEGORY_MAP}.${depth} | grep "${query}" | cut -f 2 | awk '{printf("%s.", $0);}' | sed 's/\.$//g'`

query=`cat ${MAP}/${CATEGORY_MAP}.${depth} | grep "${query}" | cut -f 2`
categories=`cat ${MAP}/${CATEGORY_MAP}.${depth} | grep "${query}" | cut -f 3`

if [[ -z ${query} ]]; then
	echo "Check your query - no dictionary found"
	exit 1
fi

if [[ -f ${output} ]]; then
	rm -f ${output}
fi

for target in ${categories}
do
	echo "${target}"
done

for category in ${query}
do
	echo "${category}"
	cat ${KNOWLEDGE}/depth-${depth}/${category}.know >> ${output}.${DICT_EXT}
done
echo "done!"
