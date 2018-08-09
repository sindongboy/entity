#!/bin/bash

while read line
do
	diff ${line}.sum ./backup/${line}.sum > ${line}.diff
done < $1
