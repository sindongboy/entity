#!/bin/bash

function usage() {
	echo "usage: $0 [options]"
	echo "-h	help"
	echo "-i	input path"
	echo "-d	category depth"
	echo "-m	delim"
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
			input=$1
			shift ;;
		-d)
			shift
			depth=$1
			shift ;;
		-m)
			shift
			delim=$1
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

if [[ -z ${input} ]] || [[ -z ${output} ]] || [[ -z ${delim} ]] || [[ -z ${depth} ]]; then
	usage
fi

field=""
for (( i=1; i<=${depth}; i++ ))
do
	field="${field},$i"
done
field=`echo "${field}" | sed 's/^\,//g'`

for category in `ls -1 ${input} | cut -d "${delim}" -f ${field} | sort -u`
do
	echo "category: ${category}"
	for line in `ls -1 ${input}/ | grep "${category}"`
	do
		cat ${input}/${line} >> ${output}/${category}
	done
done


