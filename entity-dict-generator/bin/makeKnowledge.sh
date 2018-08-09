#!/bin/bash

# ------------------------------- #
# Make Combination of Model Names
# ------------------------------- #

function usage() {
	echo "usage: $0 [options]"
	echo "-h	help"
	echo "-i	input path"
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
		*)
			break
			;;
	esac
done

if [[ -z ${INPUT} ]]; then 
	usage
fi

if [[ ! -f ${INPUT} ]]; then 
	echo "input file doesn't exist"
	usage
fi

total=`ls -1 ${INPUT} | wc -l | sed 's/  *//g'`
count=0
while IFS= read line
do
	let count=count+1
	echo "processing (${count}/${total}): ${line}"
	errorline=`echo -e "${line}" | cut -f 1,2,3 | grep "\/"`
	if [[ ! -z ${errorline} ]]; then
		echo -e "[error] slash contained : ${errorline}" >> error.log
		continue
	fi

	maker=`echo -e "${line}" | cut -f 1 | sed 's/([^)]*//g' | sed 's/)//g' | sed 's/  *//g'`
	if [[ -z ${maker} ]]; then
		maker="null"
	fi

	brand=`echo -e "${line}" | cut -f 2 | sed 's/([^)]*//g' | sed 's/)//g' | sed 's/  *//g' | sed 's/'"${maker}"'//g'`
	if [[ -z ${brand} ]]; then
		brand="null"
	fi

	model=`echo -e "${line}" | cut -f 3 | sed 's/([^)]*//g' | sed 's/)//g' | sed 's/  *//g' | sed 's/'"${maker}"'//g' | sed 's/'"${brand}"'//g'`
	if [[ -z ${model} ]]; then
		model="null"
	fi

	# ----------------------- #
	# Maker | Brand | Model
	# ----------------------- #
	expression=`echo -e "${maker}${brand}${model}" | sed 's/null//g'`
	if [[ -z ${expression} ]]; then
		echo -e "[error] null name : ${line}" >> error.log
		continue
	fi
	echo -e "${expression}	${line}" >> ${INPUT}.tmp

	# ----------------------- #
	# Brand | Model
	# ----------------------- #
	expression=`echo -e "${brand}${model}" | sed 's/null//g'`
	echo -e "${expression}	${line}" >> ${INPUT}.tmp

	# ----------------------- #
	# Maker | Model
	# ----------------------- #
	expression=`echo -e "${maker}${model}" | sed 's/null//g'`
	echo -e "${expression}	${line}" >> ${INPUT}.tmp

	# ----------------------- #
	# Maker | Brand 
	# ----------------------- #
	expression=`echo -e "${maker}${brand}" | sed 's/null//g'`
	echo -e "${expression}	${line}" >> ${INPUT}.tmp

	# ----------------------- #
	# Model
	# ----------------------- #
	expression=`echo -e "${model}" | sed 's/null//g' | sed 's/^-//g'`
	echo -e "${expression}	${line}" >> ${INPUT}.tmp 
done < ${INPUT}

cat ${INPUT}.tmp | uniq > ${INPUT}.know
rm -f ${INPUT}.tmp

