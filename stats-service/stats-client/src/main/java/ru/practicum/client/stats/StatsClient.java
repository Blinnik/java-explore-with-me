package ru.practicum.client.stats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.common.dto.EndpointHit;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatsClient extends BaseClient {
    @Autowired
    public StatsClient(String serverUrl) {
        super(
                new RestTemplateBuilder()
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> saveHit(HttpServletRequest request, String appName) {
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

    public ResponseEntity<Object> getStats(LocalDateTime start,
                                           LocalDateTime end,
                                           Boolean unique) {

        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "unique", unique
        );

        ResponseEntity<Object> response = get("/stats", parameters);
        log.debug("the response to the statistics output request was received");

        return response;
    }

    public ResponseEntity<Object> getStats(LocalDateTime start,
                                           LocalDateTime end,
                                           List<Short> uris) {

        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris
        );

        ResponseEntity<Object> response = get("/stats", parameters);
        log.debug("the response to the statistics output request was received");

        return response;
    }

    public ResponseEntity<Object> getStats(LocalDateTime start,
                                      LocalDateTime end,
                                      List<Short> uris,
                                      Boolean unique) {

        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );

        ResponseEntity<Object> response = get("/stats", parameters);
        log.debug("the response to the statistics output request was received");

        return response;
    }
}
