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


    public static void main(String[] args) {

        SpringApplication.run(LdapConnectApplication.class, args);

        ResponseEntity<DirContext> responseEntity = new LdapService().retrieveLdapInformation();
        System.out.println(responseEntity);
    }

    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate();
    }



}
