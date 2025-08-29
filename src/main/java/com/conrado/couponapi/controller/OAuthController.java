package com.conrado.couponapi.controller;

import com.conrado.couponapi.config.MLProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    private final MLProperties mlProps;
    private final RestTemplate rest;

    @Autowired
    public OAuthController(MLProperties mlProps) {
        this.mlProps = mlProps;
        this.rest = new RestTemplate();
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code, @RequestParam(required = false) String state) {
        // Trade code for access_token
        Map<String, String> tokenResponse = requestAccessToken(code);
        if (tokenResponse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al obtener access token");
        }

        String accessToken = tokenResponse.get("access_token");
        String userId = fetchUserId(accessToken);

        return ResponseEntity.ok(Map.of(
                "access_token", accessToken,
                "user_id", userId
        ));
    }

    private Map<String, String> requestAccessToken(String code) {
        String url = "https://api.mercadolibre.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", mlProps.getClientId());
        body.add("client_secret", mlProps.getClientSecret());
        body.add("code", code);
        body.add("redirect_uri", mlProps.getRedirectUri());

        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> resp = rest.postForEntity(url, req, Map.class);
            return (Map<String, String>) resp.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String fetchUserId(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> req = new HttpEntity<>(headers);

        ResponseEntity<Map> resp = rest.exchange(
                "https://api.mercadolibre.com/users/me",
                HttpMethod.GET, req, Map.class
        );
        Map body = resp.getBody();
        return body != null ? String.valueOf(body.get("id")) : null;
    }

    @GetMapping("/auth")
    public ResponseEntity<Void> redirectToAuth() {
        String url = UriComponentsBuilder.fromHttpUrl("https://auth.mercadolibre.com.ar/authorization")
                .queryParam("response_type", "code")
                .queryParam("client_id", mlProps.getClientId())
                .queryParam("redirect_uri", mlProps.getRedirectUri())
                .toUriString();
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, url).build();
    }
}
