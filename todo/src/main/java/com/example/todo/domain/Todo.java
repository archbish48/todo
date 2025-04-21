package com.example.todo.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private boolean liked;

    private LocalDate date;

    //  기본 생성자 (JPA용)
    public Todo() {}

    public Todo(String text, boolean liked, LocalDate date) {
        this.text = text;
        this.liked = liked;
        this.date = date;
    }

}
