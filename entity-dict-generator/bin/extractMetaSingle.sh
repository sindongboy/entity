#!/bin/bash



function usage() {
	echo "usage: $0 [options]"
	echo "-h	help"
	echo "-i	input file"
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


summary=`cat ${INPUT} | sed 's///g' | awk '{printf("%s ", $0)}' | sed 's/	/ /g' | sed 's/  */ /g' | grep -o "<\!-- summary_area.*\/\/summary_area"`
category=`echo "${summary}" | grep -o "cat_id=[0-9][0-9]*\"[^<]*" | sed 's/cat_id=[0-9][0-9]*\">//g' | awk '{printf("%s.", $0);}' | sed 's/\.$//g'` 
category_id=`echo "${summary}" | grep -o "cat_id=[0-9][0-9]*\"[^<]*" | grep -o "[0-9][0-9]*" | awk '{printf("%s.", $0);}' | sed 's/\.$//g'`
maker=`echo "${summary}" | grep -o "<span>제조사[^<]*" | sed 's/<span>제조사 //g'`
brand=`echo "${summary}" | grep -o "<span>브랜드[^<]*" | sed 's/<span>브랜드 //g'`
model=`echo "${summary}" | grep -o "<h2>[^<]*" | sed 's/<h2>//g'`

echo "${maker}	${brand}	${model}	${category}" >> ${OUTPUT}/${category_id}

