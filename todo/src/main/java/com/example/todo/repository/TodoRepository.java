package com.example.todo.repository;

import com.example.todo.domain.Todo;
import com.example.todo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    //사용자 + 날짜로 특정 날짜 todo 조회
    List<Todo> findByUserAndDate(User user, LocalDate date);

    //사용자 + 날짜로 todo 삭제
    void deleteByUserAndDate(User user, LocalDate date);


    //id + 사용자로 todo 조회
    Todo findByIdAndUser(Long id, User user);
}
