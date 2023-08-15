package ru.practicum.ewm.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    String description;

    @Column(name = "event_date")
    LocalDateTime eventDate;

    @Column(name = "location_lat")
    Float locationLat ;

    @Column(name = "location_lon")
    Float locationLon;

    boolean paid;

    @Column(name = "participant_limit")
    int participantLimit;

    @Column(name = "request_moderation")
    boolean requestModeration;

    String title;

    @Column(name = "confirmed_requests")
    int confirmedRequests;

    @Column(name = "created_on")
    LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    User initiator;

    @Column(name = "published_on")
    LocalDateTime publishedOn;

    @Enumerated(EnumType.STRING)
    EventState state = EventState.PENDING;

    long views;
}
