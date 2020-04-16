package br.com.ldap;

import br.com.ldap.config.LdapConfig;
import br.com.ldap.http.LdapController;
import br.com.ldap.service.LdapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@SpringBootApplication
@ComponentScan(basePackages = {"br.com.ldap.service"})
public class LdapConnectApplication {

    private static final String SEARCH_FILTER = "(&(objectClass=user))";

    private static final String[] RETURNED_ATTRS =
            {
                    "distinguishedName",
                    "cn",
                    "name",
                    "sn",
                    "givenName",
                    "memberOf",
                    "sAMAccountName",
                    "userPrincipalName"
            };


    public static void main(String[] args) {

        SpringApplication.run(LdapConnectApplication.class, args);

        ResponseEntity<DirContext> responseEntity = new LdapService().retrieveLdapInformation();
        System.out.println(responseEntity);
        disableSslVerification();

      //  ldap();
    }

    private static void disableSslVerification() {
        try{
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }


    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate();
    }




    public static void ldap() {

        try {

            final String searchBase = "";
            final LdapContext ldapContext = new InitialLdapContext(LdapConfig.createConnectionParamsKeyrus(), null);
            final SearchControls searchCtls = new SearchControls();
            searchCtls.setReturningAttributes(RETURNED_ATTRS);
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            final NamingEnumeration answer = ldapContext.search(searchBase, SEARCH_FILTER, searchCtls);


            if (answer.hasMoreElements()) {
                System.out.println(answer);

            }

        } catch (Exception cause) {

            System.out.println(cause);
        }
    }


}
