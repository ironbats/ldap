package br.com.ldap.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import java.util.Hashtable;

@Component
public class LdapConfig {

    private static final String LDAP_URL = "ldap://%s";
    private static String INITIAL_FACTORY;
    private static String SECURITY_AUTHENTICATION;
    private static String SECURITY_PRINCIPAL;
    private static String SECURITY_CREDENTIALS;
    private static String PROVIDER_URL;


    public static Hashtable<String, String> createConnectionParamsKeyrus() {

        final Hashtable<String, String> envDC = new Hashtable<>();
        envDC.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        envDC.put(Context.SECURITY_AUTHENTICATION, "simple");
        envDC.put(Context.SECURITY_PRINCIPAL, "uid=admin,ou=system");
        envDC.put(Context.SECURITY_CREDENTIALS, "secret");
        envDC.put(Context.PROVIDER_URL, String.format(LDAP_URL, "localhost:10389"));

        return envDC;
    }

    @Value("${ldap.security.url}")
    public void setPROVIDER_URL(String PROVIDER_URL) {
        this.PROVIDER_URL = PROVIDER_URL;
    }

    @Value("${ldap.security.key}")
    public void setSECURITY_CREDENTIALS(String SECURITY_CREDENTIALS) {
        this.SECURITY_CREDENTIALS = SECURITY_CREDENTIALS;
    }

    @Value("${ldap.security.principal}")
    public void setSECURITY_PRINCIPAL(String SECURITY_PRINCIPAL) {
        this.SECURITY_PRINCIPAL = SECURITY_PRINCIPAL;
    }

    @Value("${ldap.security.authentication}")
    public void setSECURITY_AUTHENTICATION(String SECURITY_AUTHENTICATION) {
        this.SECURITY_AUTHENTICATION = SECURITY_AUTHENTICATION;
    }

    @Value("${ldap.context.factory}")
    public void setInitialFactory(String INITIAL_FACTORY) {
        this.INITIAL_FACTORY = INITIAL_FACTORY;
    }


}
