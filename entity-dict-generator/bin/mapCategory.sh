#!/bin/bash

function usage() {
	echo "usage: $0 [options]"
	echo "-h	help"
	echo "-i	input path"
	echo "-o	output file"
	echo "-d	depth"
	echo "-m	delim"
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
		-o)
			shift
			output=$1
			shift ;;
		-d)
			shift
			depth=$1
			shift ;;
		-m)
			shift
			delim=$1
			shift ;;
		*)
			break
			;;
	esac
done

if [[ -z ${input} ]] || [[ -z ${output} ]]; then
	usage
fi

field=""
for (( i=1; i<=${depth}; i++ ))
do
	field="${field},$i"
done
field=`echo "${field}" | sed 's/^\,//g'`

if [[ -f ${output} ]]; then
	rm -f ${output}
fi

count=0
for file in `ls -1 ${input} | grep -v "know$"`
do
	echo "${file}"
	let count=count+1
	category=`head -1 ${input}/${file} | cut -f 4 | cut -d "${delim}" -f ${field}`
	echo -e "${count}\t${file}\t${category}" >> ${output}
done
