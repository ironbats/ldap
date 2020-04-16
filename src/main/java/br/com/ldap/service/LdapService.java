package br.com.ldap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service("ldapservice")
public class LdapService {

    private  static String URL;

    public ResponseEntity retrieveLdapInformation() {

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Object> httpEntity = restTemplate.exchange(URL, HttpMethod.POST, null, Object.class);
        return ResponseEntity.ok(httpEntity.getBody());

    }

    @Value("${hybris.url.security}")
    public void setURL(String URL) {
        this.URL = URL;
    }
}
