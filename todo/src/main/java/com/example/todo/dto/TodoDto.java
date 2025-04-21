package com.example.todo.dto;


public class TodoDto {

    private Long id;
    private String text;
    private boolean liked;
    private String date;

    public TodoDto() {}

    public TodoDto(Long id, String text, boolean liked, String date) {
        this.id = id;
        this.text = text;
        this.liked = liked;
        this.date = date;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public boolean isLiked() { return liked; }
    public void setLiked(boolean liked) { this.liked = liked; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}