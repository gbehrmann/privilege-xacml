package org.opensciencegrid.authz.xacml.common;

import java.util.Hashtable;
import java.util.Map;

public class XACMLConstants {

    /**
     * String data type
     */
    public static final String STRING_DATATYPE =
            "http://www.w3.org/2001/XMLSchema#string";

    /**
     * Integer data type
     */
    public static final String INTEGER_DATATYPE =
            "http://www.w3.org/2001/XMLSchema#integer";

    /**
     * Base 64 data type
     */
    public static final String BASE64_DATATYPE =
            "http://www.w3.org/2001/XMLSchema#base64Binary";

    /**
     * Base namespace for the profile
     */
    public static final String BASE_NS = "http://authz-interop.org/xacml/";

    /**
     * Base namespace for subject attributes in the profile
     */
    public static final String SUBJECT_BASE_NS = BASE_NS + "subject/";

    /**
     * X509 Certificate DN id
     */
    public static final String SUBJECT_X509_ID =
            SUBJECT_BASE_NS + "subject-x509-id";

    /**
     * X509 Certificate DN id
     */
    public static final String SUBJECT_CONDOR_CANONICAL_NAME_ID =
            SUBJECT_BASE_NS + "subject-condor-canonical-name-id";

    /**
     * Id of Certificate issuer DN attribute
     */
    public static final String SUBJECT_X509_ISSUER =
            SUBJECT_BASE_NS + "subject-x509-issuer";

    /**
     * X509 Certificate DN id
     */
    public static final String SUBJECT_VO_ID =
            SUBJECT_BASE_NS + "vo";

    /**
     * X509 Certificate DN id
     */
    public static final String SUBJECT_VOMS_SIGNING_SUBJECT_ID =
            SUBJECT_BASE_NS + "voms-signing-subject";

    /**
     * X509 Certificate DN id
     */
    public static final String SUBJECT_VOMS_SIGNING_ISSUER_ID =
            SUBJECT_BASE_NS + "voms-signing-issuer";

    /**
     * VOMS FQAN id
     */
    public static final String SUBJECT_VOMS_FQAN_ID =
            SUBJECT_BASE_NS + "voms-fqan";

    /**
     * VOMS FQAN id
     */
    public static final String SUBJECT_VOMS_PRIMARY_FQAN_ID =
            SUBJECT_BASE_NS + "voms-primary-fqan";

    /**
     * VOMS FQAN id
     */
    public static final String SUBJECT_CERTIFICATE_SERIAL_NUMBER_ID =
            SUBJECT_BASE_NS + "certificate-serial-number";

    /**
     * Cert chain not-before
     */
    public static final String SUBJECT_CERTIFICATE_NOT_BEFORE_ID =
            SUBJECT_BASE_NS + "validity-not-before";

    /**
     * Cert chain not-after
     */
    public static final String SUBJECT_CERTIFICATE_NOT_AFTER_ID =
            SUBJECT_BASE_NS + "validity-not-after";

    /**
     * VOMS FQAN id
     */
    public static final String SUBJECT_CA_SERIAL_NUMBER_ID =
            SUBJECT_BASE_NS + "ca-serial-number";

    /**
     * VOMS FQAN id
     */
    public static final String SUBJECT_VOMS_DNS_PORT_ID =
            SUBJECT_BASE_NS + "voms-dns-port";

    /**
     * VOMS FQAN id
     */
    public static final String SUBJECT_CA_POLICY_OID_ID =
            SUBJECT_BASE_NS + "ca-policy-oid";

    /**
     * VOMS FQAN id
     */
    public static final String SUBJECT_CERT_CHAIN_ID =
            SUBJECT_BASE_NS + "cert-chain";

    /**
     * ID for resource
     */
    public static final String RESOURCE_ID =
            "urn:oasis:names:tc:xacml:1.0:resource:resource-id";

    /**
     * Base namespace for resource attributes in the profile
     */
    public static final String RESOURCE_BASE_NS = BASE_NS + "resource/";

    /**
     * ID for compute element resource
     */
    public static final String RESOURCE_CE =
            RESOURCE_BASE_NS + "resource-type/ce";

    /**
     * ID for worker node resource
     */
    public static final String RESOURCE_WN =
            RESOURCE_BASE_NS + "resource-type/wn";

    /**
     * ID for storage element resource
     */
    public static final String RESOURCE_SE =
            RESOURCE_BASE_NS + "resource-type/se";

    /**
     * ID for resource dns host name
     */
    public static final String RESOURCE_DNS_HOST_NAME_ID =
            RESOURCE_BASE_NS + "dns-host-name";

    /**
     * ID for resource x509 id
     */
    public static final String RESOURCE_X509_ID =
            RESOURCE_BASE_NS + "resource-x509-id";

    /**
     * ID for resource dns host name
     */
    public static final String RESOURCE_X509_ISSUER_ID =
            RESOURCE_BASE_NS + "resource-x509-issuer";

    /**
     * ID for action
     */
    public static final String ACTION_ID =
            "urn:oasis:names:tc:xacml:1.0:action:action-id";

    /**
     * Base namespace for action attributes
     */
    public static final String ACTION_BASE_NS = BASE_NS + "action/";

    /**
     * ID for queue action
     */
    public static final String ACTION_QUEUE =
            ACTION_BASE_NS + "action-type/queue";

    /**
     * ID for execute now action
     */
    public static final String ACTION_EXECUTE_NOW =
            ACTION_BASE_NS + "action-type/execute-now";

    /**
     * ID for access action
     */
    public static final String ACTION_ACCESS =
            ACTION_BASE_NS + "action-type/access";

    /**
     * ID for action RSKL string
     */
    public static final String ACTION_RSL_STRING =
            ACTION_BASE_NS + "rsl-string";

    /**
     * Base namespace for environment attributes
     */
    public static final String ENVIRONMENT_BASE_NS = BASE_NS + "environment/";

    /**
     * Id of attribute that lists all supported obligations
     */
    public static final String SUPPORTED_OBLIGATIONS =
            ENVIRONMENT_BASE_NS + "pep-oblig-supported";

    /**
     * Base namespace for supported obligations
     */
    public static final String OBLIGATION_BASE_NS = BASE_NS + "obligation/";

    /**
     * Id of obligation uidgid
     */
    public static final String OBLIGATION_UIDGID =
            OBLIGATION_BASE_NS + "uidgid";

    /**
     * Id of obligation secondary gids
     */
    public static final String OBLIGATION_SECONDARY_GIDS =
            OBLIGATION_BASE_NS + "secondary-gids";

    /**
     * Id of obligation username
     */
    public static final String OBLIGATION_USERNAME =
            OBLIGATION_BASE_NS + "username";

    /**
     * Id of obligation afs token
     */
    public static final String OBLIGATION_AFS_TOKEN =
            OBLIGATION_BASE_NS + "afs-token";

    /**
     * Id of obligation root and home paths
     */
    public static final String OBLIGATION_PATHS =
            OBLIGATION_BASE_NS + "root-and-home-paths";

    /**
     * Id of obligation storage access priority
     */
    public static final String OBLIGATION_STORAGE_PRIORITY =
            OBLIGATION_BASE_NS + "storage-access-priority";

    /**
     * Id of obligation storage access permissions
     */
    public static final String OBLIGATION_ACCESS_PERMISSIONS =
            OBLIGATION_BASE_NS + "access-permissions";

    /**
     * Id of attribute for pilot jobs
     */
    public static final String ENVIRONMENT_PILOT_JOB =
            ENVIRONMENT_BASE_NS + "pilot-job";

    /**
     * Base namespace for attributes
     */
    public static final String ATTRIBUTE_BASE_NS = BASE_NS + "attribute/";

    /**
     * Length of ATTRIBUTE_BASE_NS string
     */
    public static final int ATTRIBUTE_BASE_NS_STR_LENGTH = ATTRIBUTE_BASE_NS.length();

    /**
     * Hashtable to lookup attribute variable names. Actual attribute names may not comply with Java "enum" type.
     */
    public static Map<String, String> AttrIDHash = new Hashtable<String, String>();

    /**
     * Id of attribute posix uid
     */
    public static final String ATTRIBUTE_POSIX_UID_ID =
            ATTRIBUTE_BASE_NS + "posix-uid";
    static {
        AttrIDHash.put( "posix-uid" , "ATTRIBUTE_POSIX_UID_ID" ) ;
    }

    /**
     * Id of attribute posix gid
     */
    public static final String ATTRIBUTE_POSIX_GID_ID =
            ATTRIBUTE_BASE_NS + "posix-gid";
    static {
        AttrIDHash.put( "posix-gid" , "ATTRIBUTE_POSIX_GID_ID" ) ;
    }

    /**
     * Id of attribute username
     */
    public static final String ATTRIBUTE_USERNAME_ID =
            ATTRIBUTE_BASE_NS + "username";
    static {
        AttrIDHash.put( "username" , "ATTRIBUTE_USERNAME_ID" ) ;
    }

    /**
     * Id of attribute afs token
     */
    public static final String ATTRIBUTE_AFS_TOKEN_ID =
            ATTRIBUTE_BASE_NS + "afs-token";
    static {
        AttrIDHash.put( "afs-token" , "ATTRIBUTE_AFS_TOKEN_ID" ) ;
    }

    /**
     * Id of attribute rootpath
     */
    public static final String ATTRIBUTE_ROOTPATH_ID =
            ATTRIBUTE_BASE_NS + "rootpath";
    static {
        AttrIDHash.put( "rootpath" , "ATTRIBUTE_ROOTPATH_ID" ) ;
    }

    /**
     * Id of attribute homepath
     */
    public static final String ATTRIBUTE_HOMEPATH_ID =
            ATTRIBUTE_BASE_NS + "homepath";
    static {
        AttrIDHash.put( "homepath" , "ATTRIBUTE_HOMEPATH_ID" ) ;
    }

    /**
     * Id of attribute storage priority
     */
    public static final String ATTRIBUTE_STORAGE_PRIORITY_ID =
            ATTRIBUTE_BASE_NS + "storage-priority";
    static {
        AttrIDHash.put( "storage-priority" , "ATTRIBUTE_STORAGE_PRIORITY_ID" ) ;
    }

    /**
     * Id of attribute access permissions
     */
    public static final String ATTRIBUTE_ACCESS_PERMISSIONS_ID =
            ATTRIBUTE_BASE_NS + "access-permissions";
    static {
        AttrIDHash.put( "access-permissions" , "ATTRIBUTE_ACCESS_PERMISSIONS_ID" ) ;
    }

    /**
     * Id of the primary group name attribute
     */
    public static final String ATTRIBUTE_PRIMARY_GROUPNAME_ID =
            ATTRIBUTE_BASE_NS + "primary-groupname";
    static {
        AttrIDHash.put( "primary-groupname", "ATTRIBUTE_PRIMARY_GROUPNAME_ID" ) ;
    }

    /**
     * Id of the secondary group names attribute
     */
    public static final String ATTRIBUTE_SECONDARY_GROUPNAMES_ID =
            ATTRIBUTE_BASE_NS + "secondary-groupnames";
    static {
        AttrIDHash.put( "secondary-groupnames", "ATTRIBUTE_SECONDARY_GROUPNAMES_ID" ) ;
    }
}
