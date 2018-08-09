#!/bin/bash


function usage() {
	echo "usage: $0 [options]"
	echo "-p	training template"
	echo "-t	training set"
	echo "-m	model"
	echo "-f	cutoff value"
	echo "-c	cost factor"
	exit 1
}

if [[ $# -eq 0 ]]; then
	usage
fi

CRF_LEARN="crf_learn"
TEMPLATE=""
TRAINSET=""
MODEL=""
CUTOFF=""
COST=""

while test $# -gt 0; do
	case "$1" in
		-p)
			shift
			TEMPLATE=$1
			shift ;;
		-t)
			shift
			TRAINSET=$1
			shift ;;
		-m)
			shift
			MODEL=$1
			shift ;;
		-f)
			shift
			CUTOFF=$1
			shift ;;
		-c)
			shift
			COST=$1
			shift ;;
		*)
			break
			;;
	esac
done

if [[ -z ${CUTOFF} ]]; then
	CUTOFF=1
fi

if [[ -z ${COST} ]]; then
	COST=1
fi

if [[ -z ${TEMPLATE} ]] || [[ -z ${TRAINSET} ]] || [[ -z ${MODEL} ]]; then
	usage
fi

# ======================= #
# Create CRF Model
# ======================= #

${CRF_LEARN} -f ${CUTOFF} -c ${COST} ${TEMPLATE} ${TRAINSET} ${MODEL}
