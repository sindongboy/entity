#!/bin/bash

source ./env.sh

while :
do
	clear
	mode=`ps -ef | grep "compile\.sh" | head -1 | grep -o "[A-Z][A-Z]*$"`
	if [[ -z ${mode} ]]; then
		echo "nothing is runnig..."
	fi
	echo "# ====  Running : ${mode} ==== #"
	if [[ ${mode} == KNOW ]]; then
		d1total=`find ${KNOWLEDGE}/depth-1/ -type f -name "*" | grep -v "\.know" | grep -v "\.tmp" | wc -l | sed 's/  *//g'` 
		d2total=`find ${KNOWLEDGE}/depth-2/ -type f -name "*" | grep -v "\.know" | grep -v "\.tmp" | wc -l | sed 's/  *//g'` 
		d3total=`find ${KNOWLEDGE}/depth-3/ -type f -name "*" | grep -v "\.know" | grep -v "\.tmp" | wc -l | sed 's/  *//g'` 
		d4total=`find ${KNOWLEDGE}/depth-4/ -type f -name "*" | grep -v "\.know" | grep -v "\.tmp" | wc -l | sed 's/  *//g'` 

		d1count=`find ${KNOWLEDGE}/depth-1/ -type f -name "*" | grep "\.know" | wc -l | sed 's/  *//g'`
		d2count=`find ${KNOWLEDGE}/depth-2/ -type f -name "*" | grep "\.know" | wc -l | sed 's/  *//g'`
		d3count=`find ${KNOWLEDGE}/depth-3/ -type f -name "*" | grep "\.know" | wc -l | sed 's/  *//g'`
		d4count=`find ${KNOWLEDGE}/depth-4/ -type f -name "*" | grep "\.know" | wc -l | sed 's/  *//g'`

		echo " knowledge depth 1 # : ${d1count}/${d1total}"
		echo " knowledge depth 2 # : ${d2count}/${d2total}"
		echo " knowledge depth 3 # : ${d3count}/${d3total}"
		echo " knowledge depth 4 # : ${d4count}/${d4total}"
	fi
	sleep 5
done
