#!/bin/bash

function usage() {
	echo "usage: $0 [options]"
	echo "-c	category"
	exit 1
}

while test $# -gt 0;
do
	case "$1" in
		-h)
			usage
			;;
		-c)
			shift
			category=$1
			shift ;;
		*)
			break ;;
	esac
done

if [[ -z ${category} ]]; then
	usage
fi

cutoff="1 2 3"
cost="0.5 0.6 0.7 0.8 0.9 1.0 1.1 1.2 1.3 1.4 1.5"

for cutval in ${cutoff}
do
	for costval in ${cost}
	do
		echo "category : ${category}"
		echo "cutoff : ${cutval}"
		echo "cost factor : ${costval}"
		./validate.sh -c ${category} -f ${cutval} -t ${costval} > ./${category}-${cutval}-${costval}.param
	done
done


