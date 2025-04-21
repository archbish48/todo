package com.example.todo.dto;


import java.util.List;


public class SaveTodoRequest {

    private String date;
    private List<TodoDto> todos;

    public SaveTodoRequest() {}

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public List<TodoDto> getTodos() { return todos; }
    public void setTodos(List<TodoDto> todos) { this.todos = todos; }
}

