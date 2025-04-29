package com.example.todo.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "TODO")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private boolean liked;
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //  기본 생성자 추가
    public Todo(String text, boolean liked, LocalDate  date, User user) {
        this.text = text;
        this.liked = liked;
        this.date = date;
        this.user = user;
    }

    // JPA 용 기본 생성자
    protected Todo() {}


    public void setLiked(boolean liked) {
        this.liked = liked;
    }

}
