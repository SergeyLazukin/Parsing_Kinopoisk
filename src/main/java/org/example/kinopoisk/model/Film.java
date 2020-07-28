package org.example.kinopoisk.model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import java.time.Instant;

/**
 * Модель фильма
 */
@Entity
public class Film extends AbstractPersistable<Long> {

    /**
     * Номер позиции в рейтинге
     */
    private int position;

    /**
     * Оригинальное название фильма
     */
    private String name;

    /**
     * Год
     */
    private int year;

    /**
     * рейтинг фильма
     */
    private double rating;

    /**
     * количество голосов
     */
    private int numberOfVotes;

    /**
     * Дата составления рейтинга
     */
    private Instant createInstant;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    public Instant getCreateInstant() {
        return createInstant;
    }

    public void setCreateInstant(Instant createInstant) {
        this.createInstant = createInstant;
    }
}
