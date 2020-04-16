package br.com.ldap.http;

import br.com.ldap.service.LdapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class LdapController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LdapService ldapService;

    public ResponseEntity  getLdapInformation(){


        return new LdapService().retrieveLdapInformation();

    }

}
