#!/bin/bash

# env
CONFIG="/Volumes/TimeMachine/workspace/entity-extractor-core/config"
NLPCONFIG="/Volumes/TimeMachine/workspace/resources/nlp/kor/config"
NLPRESOURCE="/Volumes/TimeMachine/workspace/resources/nlp/kor/dictionary"

# libs
LOG4J="/Users/sindongboy/.m2/repository/log4j/log4j/1.2.7/log4j-1.2.7.jar"
OMP_CONFIG="/Users/sindongboy/.m2/repository/com/skplanet/nlp/omp-config/1.0.3-SNAPSHOT/omp-config-1.0.3-SNAPSHOT.jar"
NLP="/Users/sindongboy/.m2/repository/com/skplanet/nlp/nlp-indexterm/1.1.0-SNAPSHOT/nlp-indexterm-1.1.0-SNAPSHOT.jar"
TRIE="/Users/sindongboy/.m2/repository/com/skplanet/nlp/trie/1.0.1-SNAPSHOT/trie-1.0.1-SNAPSHOT.jar"
CLI="/Users/sindongboy/.m2/repository/com/skplanet/nlp/cli/1.0.0/cli-1.0.0.jar"
COMMONCLI="/Users/sindongboy/.m2/repository/commons-cli/commons-cli/1.2/commons-cli-1.2.jar"
HNLP="/Users/sindongboy/.m2/repository/com/skplanet/nlp/hnlp/1.9.3-SNAPSHOT/hnlp-1.9.3-SNAPSHOT.jar"
SPELLER="/Users/sindongboy/.m2/repository/com/skplanet/nlp/speller/1.0.3/speller-1.0.3.jar"
CNOUN="/Users/sindongboy/.m2/repository/com/skplanet/nlp/cnoun/1.6/cnoun-1.6.jar"
NER="/Users/sindongboy/.m2/repository/com/skplanet/nlp/ner/1.2/ner-1.2.jar"


TARGET="../target/entity-extractor-1.0.0-SNAPSHOT.jar"

function usage() {
	echo "Usage: $0 [category] [port]"
	exit 1
}

if [ $# -ne 2 ]; then
	usage
fi

CP="$TRIE:$NLP:$TARGET:$CONFIG:$NLPCONFIG:$NLPRESOURCE:$OMP_CONFIG:$CLI:$COMMONCLI:$LOG4J:$HNLP:$CNOUN:$SPELLER:$NER"

java -Xmx4G -cp $CP  com.skplanet.nlp.entityExtractor.driver.EntityExtractorServer $1 $2