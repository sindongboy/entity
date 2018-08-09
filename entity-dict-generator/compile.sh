#!/bin/bash

source ./env.sh

function usage() {
	echo "usage: $0 -m [run type] -p [number of threads]"
	echo "-- run type --"
	echo "AUTO	run all steps"
	echo "META	extract meta from page sources [ ./meta ]"
	echo "SPLIT	split meta for each category depth [ ./knowledge/depth-* ]"
	echo "MAP	create category mapping file [ ./map ]"
	echo "KNOW 	make knowledges [ ./knowledge/depth-*/*.know ]"
	exit 1
}

function auto() {
	meta
	category-split
	map
	knowledge
}

function meta() {
	echo "Extract Meta from Naver Shopping Page Sources ...."
	if [[ ! -f ${BIN}/${SEED} ]]; then
		find ${PAGES}/ -type f -name "*" > ${BIN}/${SEED}
	fi
	rm -f ${BASE}/threads/*
	split -l 10000 ${BIN}/${SEED} ${BASE}/threads/
	find ${BASE}/threads/ -type f -name "*" | xargs -P ${thread} -I _LINK_ ${BIN}/extractMeta.sh -i _LINK_ -o ${META}
	echo "Extract Meta from Naver Shopping Page Sources done!"
}

function category-split() {
	echo "Split Meta for each Category Depth ...."
	for (( i=1; i<=3; i++ )) 
	do
		echo " --> for depth ${i}"
		find ${KNOWLEDGE}/depth-${i}/ -type f -name "*" | xargs -I _LINK_ rm -f _LINK_
		${BIN}/category.sh -i ${META} -d ${i} -m . -o ${KNOWLEDGE}/depth-${i}/
	done
	echo "Split Meta for each Category Depth done!"
}

function knowledge() {
	echo "Make Knowledge ...."
	for (( i=1; i<=3; i++ ))
	do
		echo " --> for depth ${i}"
		find ${KNOWLEDGE}/depth-${i}/ -type f -name "*" | xargs -P ${thread} -I _LINK_ ${BIN}/makeKnowledge.sh -i _LINK_
	done
	echo "Make Knowledge done!"
}

function map() {
	echo "Create Category Mapping File ...."
	for (( i=1; i<=3; i++ ))
	do
		echo " --> for depth ${i}"
		${BIN}/mapCategory.sh -i ${KNOWLEDGE}/depth-${i}/ -o ${MAP}/${CATEGORY_MAP}.${i} -d ${i} -m .
	done
	echo "Create Category Mapping File done!"
}

while test $# -gt 0; do
	case "$1" in
		-h)
			shift
			usage
			;;
		-m)
			shift
			mode=$1
			shift ;;
		-p)
			shift
			thread=$1
			shift ;;
		*)
			break
			;;
	esac
done

if [[ ${mode} == AUTO ]]; then
	auto
elif [[ ${mode} == META ]]; then
	meta
elif [[ ${mode} == SPLIT ]]; then
	category-split
elif [[ ${mode} == MAP ]]; then
	map
elif [[ ${mode} == KNOW ]]; then
	knowledge
else
	usage
fi


