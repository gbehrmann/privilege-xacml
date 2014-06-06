#!/bin/sh
bindir=`dirname "$(cd "${0%/*}" 2>/dev/null; echo "$PWD"/"${0##*/}")"`

export      AUTHZ_SERVICE_URL="https://gums.fnal.gov:8443/gums/services/GUMSXACMLAuthorizationServicePort" 

export      SUBJECT_X509="/DC=org/DC=doegrids/OU=People/CN=Ted Hesselroth 898520"
export      SUBJECT_CONDOR_CANONICAL_NAME="condorname"
export	    SUBJECT_X509_ISSUER="/DC=org/DC=DOEGrids/OU=Certificate Authorities/CN=DOEGrids CA 1"
export	    SUBJECT_VO="/cms/uscms"
export	    SUBJECT_VOMS_SIGNING_SUBJECT="/DC=ch/DC=cern/CN=CERN Trusted Certification Authority"
export	    SUBJECT_VOMS_SIGNING_ISSUER="/DC=ch/DC=cern/OU=computers/CN=voms.cern.ch"
export	    SUBJECT_VOMS_PRIMARY_FQAN="/cms/uscms"
export	    SUBJECT_VOMS_SECONDARY_FQAN="/cms/cmsuser"
unset       SUBJECT_VOMS_TERTIARY_FQAN
export	    SUBJECT_CERTIFICATE_SERIAL_NUMBER="1"
export	    SUBJECT_CERTIFICATE_NOT_BEFORE="2002-05-30T09:00:00"
export	    SUBJECT_CERTIFICATE_NOT_AFTER="2012-05-30T09:00:00"
export	    SUBJECT_CA_SERIAL_NUMBER="1"
export	    SUBJECT_VOMS_DNS_PORT="99999"
#export	    SUBJECT_CA_POLICY_OID
unset	    SUBJECT_CA_POLICY_OID
#export	    SUBJECT_CERT_CHAIN
unset	    SUBJECT_CERT_CHAIN
	    	    
export	    RESOURCE="CE"
export	    RESOURCE_DNS_HOST_NAME="cd-97177.fnal.gov"
export	    RESOURCE_X509="/DC=org/DC=doegrids/OU=Services/CN=cd-97177.fnal.gov"
export	    RESOURCE_X509_ISSUER="/DC=org/DC=DOEGrids/OU=Certificate Authorities/CN=DOEGrids CA 1"
	    
#export	    ACTION_RSL_STRING
unset	    ACTION_RSL_STRING

#MAVEN_REPOSITORY=/home/tdh/maven/repository
#MAVEN_REPOSITORY=/tmp/m2-repository

java \
-DsslKey=/etc/grid-security/hostkey.pem \
-DsslCertFile=/etc/grid-security/hostcert.pem \
-DsslCAFiles=/etc/grid-security/certificates/*.0 \
-classpath \
$bindir:\
`build-classpath privilege-xacml`:\
/usr/lib/privilege-xacml/*:\
`build-classpath jakarta-commons-logging`:\
`build-classpath log4j`:\
`build-classpath joda-time`:\
`build-classpath slf4j/api.jar`:\
`build-classpath slf4j/simple.jar`:\
`build-classpath bcprov`:\
`build-classpath jakarta-commons-codec`:\
`build-classpath jakarta-commons-discovery`:\
`build-classpath jakarta-commons-lang`:\
`build-classpath jglobus`:\
`build-classpath trustmanager`:\
`build-classpath trustmanager-axis`:\
`build-classpath voms-api-java`:\
`build-classpath axis`:\
`build-classpath axis/jaxrpc.jar`:\
`build-classpath wsdl4j` \
org.opensciencegrid.authz.xacml.client.XACMLClientTest




#-Djava.endorsed.dirs=/opt/apache-tomcat-5.5.20/common/endorsed \
#-DcrlEnabled=true -DcrlUpdateIntervalXXXXXXX=1800s \
