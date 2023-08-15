package ru.practicum.ewm.event.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class StatsParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Object> parseGetStatsToListOfObjects(ResponseEntity<Object> getStatsResponse) throws JsonProcessingException {
        Object body = getStatsResponse.getBody();
        String bodyJson = mapper.writeValueAsString(body);

        return mapper.readValue(bodyJson, List.class);
    }

    public static Map<String, Object> parseObjectToFields(Object object) throws JsonProcessingException {
        String objectJson = mapper.writeValueAsString(object);

        return mapper.readValue(objectJson, Map.class);
    }

    public static long parseViews(Map<String, Object> objectFields) throws JsonProcessingException {
        String fieldJson = mapper.writeValueAsString(objectFields.get("hits"));

        return mapper.readValue(fieldJson, Long.class);
    }
}
