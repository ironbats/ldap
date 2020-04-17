package br.com.ldap.service;

import br.com.ldap.config.LdapConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.net.URL;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;


@Service("ldapservice")
public class LdapService {


    private static final String SEARCH_FILTER = "(&(objectClass=groupOfNames))";


    //this should be returned according fields created in AD SERVER
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


    private static String URLL;
    private static String LDAP_CONSULT;

    public ResponseEntity retrieveLdapInformation() {

        HttpEntity<Map> httpEntity = null;


        if (Boolean.valueOf(LDAP_CONSULT)) {
            ldap();
        } else {
            httpEntity = getMapHttpEntity();
        }

        return ResponseEntity.ok(httpEntity.getBody());
    }

    private HttpEntity<Map> getMapHttpEntity() {
        HttpEntity<Map> httpEntity = null;
        try {

            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            RestTemplate restTemplate = getRestTemplateWihtoutCertificate();
            httpEntity = restTemplate.exchange(URLL, HttpMethod.POST, new HttpEntity<>(headers), Map.class);

        } catch (Exception cause) {
            System.out.println(cause);
        }
        return httpEntity;
    }


    public void ldap() {

        try {

            final String searchBase = "";
            final LdapContext ldapContext = new InitialLdapContext(LdapConfig.createConnectionParamsKeyrus(), null);
            final SearchControls searchCtls = new SearchControls();
            // searchCtls.setReturningAttributes(RETURNED_ATTRS);
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            final NamingEnumeration answer = ldapContext.search(searchBase, SEARCH_FILTER, searchCtls);

            if (answer.hasMoreElements()) {

                SearchResult searchResult = (SearchResult) answer.next();
                Attributes attrs = searchResult.getAttributes();
                Attribute atribute = attrs.get("member");
                Attribute atribute1 = attrs.get("cn");
                Attribute atribute2 = attrs.get("objectclass");

                System.out.println(attrs);

            }

        } catch (Exception cause) {

            System.out.println(cause);
        }
    }

    public RestTemplate getRestTemplateWihtoutCertificate() {

        try {

            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, (chain, authType) -> true).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, new NoopHostnameVerifier());
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            requestFactory.setHttpClient(httpclient);
            RestTemplate restTemplate = new RestTemplate(requestFactory);


            return restTemplate;

        } catch (Exception cause) {

            System.out.println(cause);
        }

        return null;

    }

    @Value("${ldap.consult.node}")
    public void setLdapConsult(String ldapConsult) {
        LDAP_CONSULT = ldapConsult;
    }

    @Value("${hybris.url.security}")
    public void setURL(String URLL) {
        this.URLL = URLL;
    }
}
