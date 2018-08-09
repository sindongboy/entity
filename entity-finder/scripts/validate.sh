#!/bin/bash

# ====================================== #
# 10-fold Cross Validation
# 2014. Oct. 12
# ====================================== #

function usage() {
	echo "usage: $0 [options]"
	echo "-m	category id"
	echo "-f	cutoff value for crf learn"
	echo "-t	cost factor for crf learn"
	exit 1
}

if [[ $# -eq 0 ]]; then
	usage
fi

category=""
cost=""
cutoff=""
while test $# -gt 0;
do
	case "$1" in
		-h)
			shift
			usage ;;
		-m)
			shift
			category=$1
			shift ;;
		-f)
			shift
			cutoff=$1
			shift ;;
		-t)
			shift
			cost=$1
			shift ;;
		*)
			break ;;
	esac
done 

if [[ -z ${category} ]]; then
	usage
fi

TRAIN_HOME="../resource/train"
VALID_HOME="../resource/eval/train"
RESULT_HOME="../resource/eval/test"
MODEL_HOME="../resource/models"

rm -f *.diff

for fold in {0..9}
do 
	echo "========================"
	echo "> Fold : ${fold}"
	echo "========================"

	echo "training ...."
	# ---------------------------------- #
	# merge training source ( ./train.all )
	# ---------------------------------- #
	ls -1 ${VALID_HOME}/ | grep -v "${fold}.val" > ${RESULT_HOME}/files
	rm -f  ${RESULT_HOME}/train.all
	touch ${RESULT_HOME}/train.all
	while read line 
	do
		cat ${VALID_HOME}/${line} >> ${RESULT_HOME}/train.all
		echo "" >> ${RESULT_HOME}/train.all
	done < ${RESULT_HOME}/files

	# ---------------------------------- #
	# generate test/answer set
	# ---------------------------------- #
	./crf-trainset.sh -d ${category}.dict -i ${VALID_HOME}/${fold}.val -o ${RESULT_HOME}/answer.set 2>> ../log/eval.log
	echo "" >> ${RESULT_HOME}/answer.set
	cat ${RESULT_HOME}/answer.set | cut -d " " -f 1,2,3 > ${RESULT_HOME}/test.set

	# ---------------------------------- #
	# train (with ./train.all)
	# ---------------------------------- #
	./crf-trainset.sh -d ${category}.dict -i ${RESULT_HOME}/train.all -o ${RESULT_HOME}/trainset.all 2>> ${RESULT_HOME}/validation.log
	./crf-train.sh -f ${cutoff} -c ${cost} -p ${TRAIN_HOME}/${category}.template -t ${RESULT_HOME}/trainset.all -m ${MODEL_HOME}/${category}.model 2>> ../log/eval.log
	echo "training done"

	# test
	echo "testing ...."
	crf_test -m ${MODEL_HOME}/${category}.model -o ${RESULT_HOME}/result.tmp ${RESULT_HOME}/test.set 
	cat ${RESULT_HOME}/result.tmp | sed 's/	/ /g' > ${RESULT_HOME}/result.set
	rm -f ${RESULT_HOME}/result.tmp
	diff ${RESULT_HOME}/result.set ${RESULT_HOME}/answer.set > ${RESULT_HOME}/${category}.${fold}.diff
	echo "testing done"

	# get f-score
	echo "evaluate ...."
	precisionTotal=`cat ${RESULT_HOME}/result.set | grep "B$" | wc -l | sed 's/  *//g'`
	error=`cat ${RESULT_HOME}/${category}.${fold}.diff | grep "^---" | wc -l | sed 's/  *//g'`
	precisionCorrect=`expr ${precisionTotal} - ${error}`
	precision=`echo "scale=3;${precisionCorrect}/${precisionTotal}" | bc -l`

	recallTotal=`cat ${RESULT_HOME}/answer.set | grep "B$" | wc -l | sed 's/  *//g'`
	recallCorrect=`expr ${recallTotal} - ${error}`
	recall=`echo "scale=3;${recallCorrect}/${recallTotal}" | bc -l`

	echo "precision : ${precision}" >> ${RESULT_HOME}/${category}.${fold}.diff
	echo "recall : ${recall}" >> ${RESULT_HOME}/${category}.${fold}.diff
	fscore=`echo "scale=3;2*((${precision}*${recall})/(${precision} + ${recall}))" | bc -l`
	echo "f-1 : ${fscore}" >> ${RESULT_HOME}/${category}.${fold}.diff
	echo "evaluate done"
done
rm -f ${RESULT_HOME}/files

total=0
count=0
for num in `cat ${RESULT_HOME}/*.diff | grep "f-1" | cut -d " " -f 3`
do
	total=`echo "${total} + ${num}" | bc -l`
	count=`expr ${count} + 1`
done 

echo "=========================="
echo " Validation for ${category} Done"
echo "=========================="
echo -n "Mean Value: "
echo "scale=3;${total}/${count}" | bc -l

