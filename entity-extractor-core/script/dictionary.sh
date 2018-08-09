#!/bin/bash

CONFIG="/Volumes/TimeMachine/workspace/entity-extractor-core/config"
OMP_CONFIG="/Users/sindongboy/.m2/repository/com/skplanet/omp-config/1.0.3-SNAPSHOT/omp-config-1.0.3-SNAPSHOT.jar"
TARGET="../target/entity-extractor-1.0.0-SNAPSHOT.jar"
NLP="/Users/sindongboy/.m2/repository/com/skplanet/nlp/nlp-indexterm/1.1.0-SNAPSHOT/nlp-indexterm-1.1.0-SNAPSHOT.jar"
NLPCONFIG="/Volumes/TimeMachine/workspace/nlp-indexterm/config"
NLPRESOURCE="/Volumes/TimeMachine/workspace/resources/nlp/kor/dictionary"
TRIE="/Users/sindongboy/.m2/repository/com/skplanet/nlp/trie/1.0.1-SNAPSHOT/trie-1.0.1-SNAPSHOT.jar"



java -Xmx4G -cp $TRIE:$NLP:$TARGET:$CONFIG:$NLPCONFIG:$NLPRESOURCE:$OMP_CONFIG com.skplnaet.nlp.driver.DictionaryTester

