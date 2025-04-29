package com.example.todo.controller;


import com.example.todo.domain.Todo;
import com.example.todo.domain.User;
import com.example.todo.dto.SaveTodoRequest;
import com.example.todo.dto.TodoDto;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    private User getLoginUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("loginUser");
        if(userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자 정보를 찾을 수 없습니다"));
    }

    // ?date=2025-04-21 와같이 조회하면 해당 날짜 모든 todo 조회 가능
    @GetMapping
    public List<TodoDto> getTodos(@RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, HttpSession session) {
        User user = getLoginUser(session);

        return todoRepository.findByUserAndDate(user, date)  //날짜로 찾기
                .stream()
                .map(todo -> new TodoDto(todo.getId(), todo.getText(), todo.isLiked(), todo.getDate().toString()))
                .collect(Collectors.toList());
    }


    // 날짜별 todo 전체 저장 덮어쓰기 (하트 토글, 텍스트 입력, 삭제 등 모든 변경 처리)
    @PutMapping
    @Transactional  //PUT 은 원자성 보장해야함 transactional 필요
    public void saveTodos(@RequestBody SaveTodoRequest request, HttpSession session) {
        User user = getLoginUser(session);
        LocalDate date = LocalDate.parse(request.getDate());

        // 해당 날짜의 기존 todo 모두 삭제
        todoRepository.deleteByUserAndDate(user, date);

        // 새로 저장
        List<Todo> todos = request.getTodos().stream()
                .map(dto -> new Todo(dto.getText(), dto.isLiked(), date, user))
                .collect(Collectors.toList());

        todoRepository.saveAll(todos);
    }

    //  ID에 해당하는 TODO의 하트를 반대로 바꿈(TRUE -> FALSE, FALSE -> TRUE)
    @PatchMapping("/{id}/like")
    @Transactional
    public void toggleLike(@PathVariable("id") Long id, HttpSession session) {
        User user = getLoginUser(session);
        Todo todo = todoRepository.findByIdAndUser(id, user);
        if(todo == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo 찾지 못하였음");
        }
        todo.setLiked(!todo.isLiked());
    }

    //   단일 todo 추가
    @PostMapping
    public TodoDto addTodo(@RequestBody TodoDto dto, HttpSession session) {
        User user = getLoginUser(session);
        Todo newTodo = new Todo(dto.getText(), dto.isLiked(), LocalDate.parse(dto.getDate()), user);
        Todo saved = todoRepository.save(newTodo);
        return new TodoDto(saved.getId(), saved.getText(), saved.isLiked(), saved.getDate().toString());
    }


    //  ID에 해당하는 TODO 삭제
    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable("id") Long id, HttpSession session) {
        User user = getLoginUser(session);
        Todo todo = todoRepository.findByIdAndUser(id, user);
        if (todo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found");
        }
        todoRepository.delete(todo);
    }

}




