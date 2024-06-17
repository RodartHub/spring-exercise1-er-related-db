package com.spring.er_spring.er_spring.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.er_spring.er_spring.Entities.Book;

public interface BookRepository extends JpaRepository<Book, Integer>{

}
