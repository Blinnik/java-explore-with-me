package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.common.dto.ViewStats;
import ru.practicum.server.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {
    @Query("SELECT new ru.practicum.common.dto.ViewStats(s.app, s.uri, COUNT(s.ip)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN ?1 and ?2 AND " +
            "s.uri IN (?3) " +
            "GROUP BY s.uri, s.app " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStats> findAllByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uriList);

    @Query("SELECT new ru.practicum.common.dto.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN ?1 and ?2 AND " +
            "s.uri IN (?3) " +
            "GROUP BY s.uri, s.app " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStats> findAllByTimestampBetweenAndUriInAndIpUnique(LocalDateTime start,
                                                                   LocalDateTime end,
                                                                   List<String> uriList);

    @Query("SELECT s.uri " +
            "FROM Stats AS s")
    List<String> findAllUris();
}
