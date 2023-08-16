package ru.practicum.client.stats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.common.dto.EndpointHit;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class StatsClient extends BaseClient {
    public StatsClient(String serverUrl) {
        super(
                new RestTemplateBuilder()
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> save(HttpServletRequest request, String appName) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        String uri = request.getRequestURI();

        EndpointHit endpointHit = EndpointHit.builder()
                .app(appName)
                .uri(uri)
                .ip(ip)
                .build();

        ResponseEntity<Object> response = post("/hit", endpointHit);
        log.debug("the response to the hit save request was received");

        return response;
    }

    public ResponseEntity<Object> getAll(String start,
                                         String end,
                                         Boolean unique) {

        return getAll(start, end, null, unique);
    }

    public ResponseEntity<Object> getAll(String start,
                                         String end,
                                         List<String> uris) {

        return getAll(start, end, uris, null);
    }

    public ResponseEntity<Object> getAll(String start,
                                         String end,
                                         List<String> uris,
                                         Boolean unique) {

        Map<String, Object> parameters = new HashMap<>(Map.of(
                "start", start,
                "end", end
        ));

        StringBuilder pathBuilder = new StringBuilder("/stats?start={start}&end={end}");

        if (uris != null) {
            String concatenatedUris = concatUris(uris);
            parameters.put("uris", concatenatedUris);
            pathBuilder.append("&uris=").append(concatenatedUris);
        }

        if (unique != null) {
            parameters.put("unique", unique);
            pathBuilder.append("&unique=").append(unique);
        }


        ResponseEntity<Object> response = get(pathBuilder.toString(), parameters);
        log.debug("the response to the statistics output request was received");

        return response;
    }

    private String concatUris(List<String> uris) {
        StringBuilder uriStringBuilder = new StringBuilder();

        for (int i = 0; i < uris.size(); i++) {
            uriStringBuilder.append(uris.get(i));

            if (i != uris.size() - 1) {
                uriStringBuilder.append(",");
            }
        }

        return uriStringBuilder.toString();
    }
}
