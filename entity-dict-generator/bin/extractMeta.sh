#!/bin/bash



function usage() {
	echo "usage: $0 [options]"
	echo "-h	help"
	echo "-i	path to the page sources"
	echo "-o	output path"
	exit 1
}

while test $# -gt 0; do
	case "$1" in
		-h)
			shift
			usage
			;;
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

if [[ -z ${INPUT} ]] || [[ -z ${OUTPUT} ]]; then 
	usage
fi


while read line
do
	./extractMetaSingle.sh -i ./pages/${line} -o ${OUTPUT}
done < ${INPUT}
