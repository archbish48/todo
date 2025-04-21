package com.example.todo.controller;


import com.example.todo.domain.Todo;
import com.example.todo.dto.SaveTodoRequest;
import com.example.todo.dto.TodoDto;
import com.example.todo.repository.TodoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    // ?date=2025-04-21 와같이 조회하면 해당 날짜 모든 todo 조회 가능
    @GetMapping
    public List<TodoDto> getTodos(@RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return todoRepository.findByDate(date)
                .stream()
                .map(todo -> new TodoDto(todo.getId(), todo.getText(), todo.isLiked(), todo.getDate().toString()))
                .collect(Collectors.toList());
    }


    // 날짜별 todo 전체 저장 덮어쓰기 (하트 토글, 텍스트 입력, 삭제 등 모든 변경 처리)
    @PutMapping
    @Transactional
    public void saveTodos(@RequestBody SaveTodoRequest request) {
        LocalDate date = LocalDate.parse(request.getDate());

        // 해당 날짜의 기존 todo 모두 삭제
        todoRepository.deleteByDate(date);

        // 새로 저장
        List<Todo> todos = request.getTodos().stream()
                .map(dto -> new Todo(dto.getText(), dto.isLiked(), date))
                .collect(Collectors.toList());

        todoRepository.saveAll(todos);
    }

    //  ID에 해당하는 TODO의 하트를 반대로 바꿈(TRUE -> FALSE, FALSE -> TRUE)
    @PatchMapping("/{id}/like")
    @Transactional
    public void toggleLike(@PathVariable("id") Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));
        todo.setLiked(!todo.isLiked());
    }

    //   단일 todo 추가
    @PostMapping
    public TodoDto addTodo(@RequestBody TodoDto dto) {
        Todo newTodo = new Todo(dto.getText(), dto.isLiked(), LocalDate.parse(dto.getDate()));
        Todo saved = todoRepository.save(newTodo);
        return new TodoDto(saved.getId(), saved.getText(), saved.isLiked(), saved.getDate().toString());
    }


    // 현재 코드 기준으로 이 API는 필요없어보임
    //  ID에 해당하는 TODO 삭제
    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable("id") Long id) {
        if (!todoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found");
        }
        todoRepository.deleteById(id);
    }
}




