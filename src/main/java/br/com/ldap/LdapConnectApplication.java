package br.com.ldap;

import br.com.ldap.config.LdapConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

@SpringBootApplication
@Slf4j
@ComponentScan
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
        ldap();
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
