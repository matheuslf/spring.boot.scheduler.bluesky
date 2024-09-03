package spring.boot.scheduler.bluesky.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class BlueskyApiService {

    private String baseUrl = "https://bsky.social/xrpc";
    private String username = "ADICIONAR_USERNAME";
    private String password = "ADICIONAR_PASSWORD";

    private final RestTemplate restTemplate;
    private String accessJwt;
    private String did;

    public BlueskyApiService() {
        this.restTemplate = new RestTemplate();
        authenticate();
    }

    /**
     * Autentica na API do Bluesky e obtém o JWT e DID necessários para futuras requisições.
     */
    public void authenticate() {
        String url = baseUrl + "/com.atproto.server.createSession";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("identifier", username);
        requestBody.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                this.accessJwt = (String) response.getBody().get("accessJwt");
                this.did = (String) response.getBody().get("did");
                log.info("Autenticado com sucesso no Bluesky API.");
            } else {
                log.error("Falha na autenticação do Bluesky API: {}", response.getBody());
            }
        } catch (Exception e) {
            log.error("Erro durante a autenticação no Bluesky API: {}", e.getMessage());
        }
    }

    /**
     * Publica uma postagem no Bluesky.
     *
     * @param content Conteúdo da postagem.
     * @return true se a postagem for publicada com sucesso, false caso contrário.
     */
    public boolean createPost(String content) {
        String url = baseUrl + "/com.atproto.repo.createRecord";

        Map<String, Object> record = new HashMap<>();
        record.put("text", content);
        record.put("createdAt", OffsetDateTime.now().toString());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("collection", "app.bsky.feed.post");
        requestBody.put("repo", did);
        requestBody.put("record", record);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessJwt);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Postagem criada com sucesso no Bluesky.");
                return true;
            } else {
                log.error("Falha ao criar postagem no Bluesky: {}", response.getBody());
                return false;
            }
        } catch (Exception e) {
            log.error("Erro ao criar postagem no Bluesky: {}", e.getMessage());
            return false;
        }
    }
}
