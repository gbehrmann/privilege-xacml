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

MAVEN_REPOSITORY=/home/tdh/maven/repository

java \
-DsslKey=/etc/grid-security/hostkey.pem \
-DsslCertFile=/etc/grid-security/hostcert.pem \
-DsslCAFiles=/etc/grid-security/certificates/*.0 \
-classpath \
$bindir:\
$bindir/../../target/privilege-xacml-2.6.1.jar:\
$MAVEN_REPOSITORY/opensaml/opensaml/2.4.1/opensaml-2.4.1.jar:\
$MAVEN_REPOSITORY/opensaml/xmltooling/1.3.1/xmltooling-1.3.1.jar:\
$MAVEN_REPOSITORY/commons-logging/commons-logging/1.1/commons-logging-1.1.jar:\
$MAVEN_REPOSITORY/xml-security/xml-security/1.4.1/xml-security-1.4.1.jar:\
$MAVEN_REPOSITORY/sun/sunxacml/1.2/sunxacml-1.2.jar:\
$MAVEN_REPOSITORY/log4j/log4j/1.2.16/log4j-1.2.16.jar:\
$MAVEN_REPOSITORY/joda-time/joda-time/1.6.1/joda-time-1.6.1.jar:\
$MAVEN_REPOSITORY/org/slf4j/slf4j-log4j12/1.6.1/slf4j-log4j12-1.6.1.jar:\
$MAVEN_REPOSITORY/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar:\
$MAVEN_REPOSITORY/org/apache/velocity/velocity/1.6.4/velocity-1.6.4.jar:\
$MAVEN_REPOSITORY/commons-collections/commons-collections/3.2.1/commons-collections-3.2.1.jar:\
$MAVEN_REPOSITORY/commons-lang/commons-lang/2.6/commons-lang-2.6.jar:\
$MAVEN_REPOSITORY/org/opensaml/openws/1.4.1/openws-1.4.1.jar:\
$MAVEN_REPOSITORY/org/bouncycastle/bcprov-jdk15/1.45/bcprov-jdk15-1.45.jar:\
$MAVEN_REPOSITORY/commons-discovery/commons-discovery/0.5/commons-discovery-0.5.jar:\
$MAVEN_REPOSITORY/globus/cog-jglobus/1.8.0/cog-jglobus-1.8.0.jar:\
$MAVEN_REPOSITORY/emi/emi-security-trustmanager/3.0.3/emi-security-trustmanager-3.0.3.jar:\
$MAVEN_REPOSITORY/emi/emi-trustmanager-axis/1.0.1/emi-trustmanager-axis-1.0.1.jar:\
$MAVEN_REPOSITORY/voms/vomsjapi/2.0.0/vomsjapi-2.0.0.jar:\
$MAVEN_REPOSITORY/junit/junit/4.8.1/junit-4.8.1.jar:\
$MAVEN_REPOSITORY/commons-cli/commons-cli/1.2/commons-cli-1.2.jar:\
$MAVEN_REPOSITORY/axis/axis/1.4/axis-1.4.jar:\
$MAVEN_REPOSITORY/axis/axis-wsdl4j/1.5.1/axis-wsdl4j-1.5.1.jar:\
$MAVEN_REPOSITORY/javax/servlet/servlet-api/2.5/servlet-api-2.5.jar:\
$MAVEN_REPOSITORY/commons-httpclient/commons-httpclient/3.1/commons-httpclient-3.1.jar:\
$MAVEN_REPOSITORY/org/apache/xmlbeans/xmlbeans/2.4.0/xmlbeans-2.4.0.jar:\
$MAVEN_REPOSITORY/axis/axis/1.4/axis-1.4.jar:\
$MAVEN_REPOSITORY/org/apache/axis/axis-jaxrpc/1.4/axis-jaxrpc-1.4.jar \
org.opensciencegrid.authz.xacml.client.XACMLClientTest



#-Djava.endorsed.dirs=/opt/apache-tomcat-5.5.20/common/endorsed \
#-DcrlEnabled=true -DcrlUpdateIntervalXXXXXXX=1800s \
