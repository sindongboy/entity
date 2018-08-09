#!/bin/bash

while read line 
do 
	cat ${line}.eval | grep "Precision\|Recall\|F-Score" > ${line}.sum
done < $1 
