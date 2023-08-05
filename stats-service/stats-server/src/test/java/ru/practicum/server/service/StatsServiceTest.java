package ru.practicum.server.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.dto.EndpointHit;
import ru.practicum.common.dto.ViewStats;
import ru.practicum.server.model.dto.GetStatsParams;
import ru.practicum.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class StatsServiceTest {
    StatsService statsService;
    StatsRepository statsRepository;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void saveHit_whenOneHitCreated_thenIdNotNull() {
        long countOfRecords = statsRepository.count();

        EndpointHit hit = EndpointHit.builder()
                .ip("192.0.0.0")
                .uri("/event/1")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now()))
                .build();

        statsService.saveHit(hit);

        Long updatedCountOfRecords = statsRepository.count();
        assertEquals(countOfRecords + 1, updatedCountOfRecords);
    }

    @Test
    void getStats_whenIpNotUniqueAndTwoUris_thenReturnListOfViewStats() {
        long countOfRecords = statsRepository.count();

        EndpointHit hit = EndpointHit.builder()
                .ip("192.0.0.0")
                .uri("/event/1")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now()))
                .build();

        EndpointHit hit2 = EndpointHit.builder()
                .ip("192.1.1.1")
                .uri("/event/2") // another id of URI
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now().plusDays(2)))
                .build();

        EndpointHit hit3 = EndpointHit.builder()
                .ip("192.2.2.2")
                .uri("/event/1")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now().plusDays(3)))
                .build();

        statsService.saveHit(hit);
        statsService.saveHit(hit2);
        statsService.saveHit(hit3);

        GetStatsParams getStatsParams = new GetStatsParams();
        getStatsParams.setStart(LocalDateTime.now().minusDays(1));
        getStatsParams.setEnd(LocalDateTime.now().plusDays(2));
        getStatsParams.setUris(List.of("/event/1", "/event/2"));
        getStatsParams.setUnique(false);

        List<ViewStats> viewStatsList = statsService.getStats(getStatsParams);
        Long updatedCountOfRecords = statsRepository.count();

        assertEquals(countOfRecords + 3, updatedCountOfRecords);

        assertEquals(2, viewStatsList.size());

        assertEquals(1, viewStatsList.get(0).getHits());
        assertEquals("/event/1", viewStatsList.get(0).getUri());
        assertEquals(1, viewStatsList.get(1).getHits());
        assertEquals("/event/2", viewStatsList.get(1).getUri());
    }

    @Test
    void getStats_whenIpUniqueAndTwoUris_thenReturnListOfViewStats() {
        long countOfRecords = statsRepository.count();

        EndpointHit hit = EndpointHit.builder()
                .ip("192.1.1.1")
                .uri("/event/1")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now()))
                .build();

        EndpointHit hit2 = EndpointHit.builder()
                .ip("192.1.1.1")
                .uri("/event/2") // another id of URI
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now().plusDays(2)))
                .build();

        EndpointHit hit3 = EndpointHit.builder()
                .ip("192.0.0.0")
                .uri("/event/1")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now().plusDays(3)))
                .build();

        EndpointHit hit4 = EndpointHit.builder()
                .ip("192.1.1.1")
                .uri("/event/1")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now().plusDays(4)))
                .build();

        statsService.saveHit(hit);
        statsService.saveHit(hit2);
        statsService.saveHit(hit3);
        statsService.saveHit(hit4);

        GetStatsParams getStatsParams = new GetStatsParams();
        getStatsParams.setStart(LocalDateTime.now().minusDays(1));
        getStatsParams.setEnd(LocalDateTime.now().plusDays(5));
        getStatsParams.setUris(List.of("/event/1", "/event/2"));
        getStatsParams.setUnique(true);

        List<ViewStats> viewStatsList = statsService.getStats(getStatsParams);
        Long updatedCountOfRecords = statsRepository.count();

        assertEquals(countOfRecords + 4, updatedCountOfRecords);

        assertEquals(2, viewStatsList.size());

        assertEquals(2, viewStatsList.get(0).getHits());
        assertEquals("/event/1", viewStatsList.get(0).getUri());
        assertEquals(1, viewStatsList.get(1).getHits());
        assertEquals("/event/2", viewStatsList.get(1).getUri());
    }

    @Test
    void getStats_whenUrisNotMentionedAndIpNotUnique_thenReturnAllUrisWithNotUniqueIp() {
        long countOfRecords = statsRepository.count();

        EndpointHit hit = EndpointHit.builder()
                .ip("192.1.1.1")
                .uri("/event/1")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now()))
                .build();

        EndpointHit hit2 = EndpointHit.builder()
                .ip("192.1.1.1")
                .uri("/event/1")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now().plusDays(2)))
                .build();

        EndpointHit hit3 = EndpointHit.builder()
                .ip("192.0.0.0")
                .uri("/event/2")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now().plusDays(3)))
                .build();

        EndpointHit hit4 = EndpointHit.builder()
                .ip("192.1.1.1")
                .uri("/event/3")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now().plusDays(4)))
                .build();

        statsService.saveHit(hit);
        statsService.saveHit(hit2);
        statsService.saveHit(hit3);
        statsService.saveHit(hit4);

        GetStatsParams getStatsParams = new GetStatsParams();
        getStatsParams.setStart(LocalDateTime.now().minusDays(1));
        getStatsParams.setEnd(LocalDateTime.now().plusDays(5));
        getStatsParams.setUris(List.of());
        getStatsParams.setUnique(false);

        List<ViewStats> viewStatsList = statsService.getStats(getStatsParams);
        Long updatedCountOfRecords = statsRepository.count();

        assertEquals(countOfRecords + 4, updatedCountOfRecords);

        assertEquals(3, viewStatsList.size());

        assertEquals(2, viewStatsList.get(0).getHits());
        assertEquals(1, viewStatsList.get(1).getHits());
        assertEquals(1, viewStatsList.get(2).getHits());
    }

    @Test
    void getStats_whenUrisNotMentionedAndIpUnique_thenReturnAllUrisWithUniqueIp() {
        long countOfRecords = statsRepository.count();

        EndpointHit hit = EndpointHit.builder()
                .ip("192.1.1.1")
                .uri("/event/1")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now()))
                .build();

        EndpointHit hit2 = EndpointHit.builder()
                .ip("192.1.1.1")
                .uri("/event/1")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now().plusDays(2)))
                .build();

        EndpointHit hit3 = EndpointHit.builder()
                .ip("192.0.0.0")
                .uri("/event/2")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now().plusDays(3)))
                .build();

        EndpointHit hit4 = EndpointHit.builder()
                .ip("192.1.1.1")
                .uri("/event/3")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now().plusDays(4)))
                .build();

        statsService.saveHit(hit);
        statsService.saveHit(hit2);
        statsService.saveHit(hit3);
        statsService.saveHit(hit4);

        GetStatsParams getStatsParams = new GetStatsParams();
        getStatsParams.setStart(LocalDateTime.now().minusDays(1));
        getStatsParams.setEnd(LocalDateTime.now().plusDays(5));
        getStatsParams.setUris(List.of());
        getStatsParams.setUnique(true);

        List<ViewStats> viewStatsList = statsService.getStats(getStatsParams);
        Long updatedCountOfRecords = statsRepository.count();

        assertEquals(countOfRecords + 4, updatedCountOfRecords);

        assertEquals(3, viewStatsList.size());

        assertEquals(1, viewStatsList.get(0).getHits());
        assertEquals(1, viewStatsList.get(1).getHits());
        assertEquals(1, viewStatsList.get(2).getHits());
    }

    @Test
    void getStats_whenUriNotFoundOrNotCorrect_thenReturnEmptyList() {
        long countOfRecords = statsRepository.count();

        EndpointHit hit = EndpointHit.builder()
                .ip("192.1.1.1")
                .uri("/event/1")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now()))
                .build();

        EndpointHit hit2 = EndpointHit.builder()
                .ip("192.1.1.1")
                .uri("/event/1")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now().plusDays(2)))
                .build();

        EndpointHit hit3 = EndpointHit.builder()
                .ip("192.0.0.0")
                .uri("/event/2")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now().plusDays(3)))
                .build();

        EndpointHit hit4 = EndpointHit.builder()
                .ip("192.1.1.1")
                .uri("/event/3")
                .app("ewm-service")
                .timestamp(formatter.format(LocalDateTime.now().plusDays(4)))
                .build();

        statsService.saveHit(hit);
        statsService.saveHit(hit2);
        statsService.saveHit(hit3);
        statsService.saveHit(hit4);

        GetStatsParams getStatsParams = new GetStatsParams();
        getStatsParams.setStart(LocalDateTime.now().minusDays(1));
        getStatsParams.setEnd(LocalDateTime.now().plusDays(5));
        getStatsParams.setUris(List.of("/not_existing_endpoint"));
        getStatsParams.setUnique(false);

        List<ViewStats> viewStatsList = statsService.getStats(getStatsParams);
        Long updatedCountOfRecords = statsRepository.count();

        assertEquals(countOfRecords + 4, updatedCountOfRecords);

        assertEquals(0, viewStatsList.size());
    }
}