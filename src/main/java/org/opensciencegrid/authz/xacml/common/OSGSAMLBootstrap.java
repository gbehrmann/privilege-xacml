package org.opensciencegrid.authz.xacml.common;

import org.opensaml.DefaultBootstrap;
import org.opensaml.xml.ConfigurationException;

public class OSGSAMLBootstrap extends DefaultBootstrap {

    /** List of default XMLTooling configuration files. */
    private static String[] OSGxmlToolingConfigs = {
            "/default-config.xml",
            "/schema-config.xml",
            "/signature-config.xml",
            "/signature-validation-config.xml",
            "/encryption-config.xml",
            "/encryption-validation-config.xml",
            "/soap11-config.xml",
            "/wsfed11-protocol-config.xml",
            "/saml1-assertion-config.xml",
            "/saml1-protocol-config.xml",
            "/saml1-core-validation-config.xml",
            "/saml2-assertion-config.xml",
            "/saml2-protocol-config.xml",
            "/saml2-core-validation-config.xml",
            "/saml1-metadata-config.xml",
            "/saml2-metadata-config.xml",
            "/saml2-metadata-validation-config.xml",
            "/saml2-protocol-thirdparty-config.xml",
            "/saml2-metadata-query-config.xml",
            "/xacml10-saml2-profile-config.xml",
            "/xacml11-saml2-profile-config.xml",
            "/xacml20-context-config.xml",
            "/xacml20-policy-config.xml",
            "/xacml2-saml2-profile-config.xml",
            "/xacml3-saml2-profile-config.xml",
            "/xacml20-context-config-osg.xml"
    };

    /** List of default XMLTooling configuration files. */
    private static String[] xmlToolingConfigs = {
            "/default-config.xml",
            "/schema-config.xml",
            "/signature-config.xml",
            "/signature-validation-config.xml",
            "/encryption-config.xml",
            "/encryption-validation-config.xml",
            "/soap11-config.xml",
            "/wsfed11-protocol-config.xml",
            "/saml1-assertion-config.xml",
            "/saml1-protocol-config.xml",
            "/saml1-core-validation-config.xml",
            "/saml2-assertion-config.xml",
            "/saml2-protocol-config.xml",
            "/saml2-core-validation-config.xml",
            "/saml1-metadata-config.xml",
            "/saml2-metadata-config.xml",
            "/saml2-metadata-validation-config.xml",
            "/saml2-protocol-thirdparty-config.xml",
            "/saml2-metadata-query-config.xml",
            "/xacml10-saml2-profile-config.xml",
            "/xacml11-saml2-profile-config.xml",
            "/xacml20-context-config.xml",
            "/xacml20-policy-config.xml",
            "/xacml2-saml2-profile-config.xml",
            "/xacml3-saml2-profile-config.xml",
    };

    /**
     * Initializes the OpenSAML library, loading OSG configurations.
     *
     * @throws org.opensaml.xml.ConfigurationException thrown if there is a problem initializing the OpenSAML library
     */
    public static synchronized void bootstrap() throws ConfigurationException {

        initializeXMLSecurity();

        initializeVelocity();

        initializeXMLTooling(OSGxmlToolingConfigs);

        initializeArtifactBuilderFactories();

        initializeGlobalSecurityConfiguration();

        initializeParserPool();
    }

}
