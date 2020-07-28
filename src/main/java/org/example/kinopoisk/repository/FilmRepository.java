package org.example.kinopoisk.repository;

import org.example.kinopoisk.model.Film;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface FilmRepository extends CrudRepository<Film, Long> {

    @Query("SELECT COUNT(*) > 0 FROM Film WHERE DAY(:instant - createInstant) = 0")
    boolean existByDate(@Param("instant") Instant instant);
}
