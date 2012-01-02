#!/bin/bash

BASE=https://kronos.ipi.ch/EWS
# add the login credentials to your .netrc file
# entry:>>  machine kronos.ipi.ch login [username] password [pwd]

mkdir -p orig
curl --basic --netrc $BASE/services.wsdl -o orig/services.wsdl
curl --basic --netrc $BASE/messages.xsd -o orig/messages.xsd
curl --basic --netrc $BASE/types.xsd -o orig/types.xsd

cp orig/messages.xsd messages.xsd
xsltproc fix-types.xsl orig/types.xsd > types.xsd
xsltproc fix-services.xsl orig/services.wsdl > services.wsdl

mkdir -p ../src/java
wsimport -Xnocompile -target 2.1 -s ../src/java services.wsdl
find ../src/java -name 'ExchangeWebService.java' -delete

mkdir -p ../src/resource
cp *.xsd *.wsdl ../src/resource