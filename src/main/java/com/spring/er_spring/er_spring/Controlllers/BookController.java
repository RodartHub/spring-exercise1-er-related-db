package com.spring.er_spring.er_spring.Controlllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.er_spring.er_spring.Entities.Book;
import com.spring.er_spring.er_spring.Entities.Library;
import com.spring.er_spring.er_spring.Repositories.BookRepository;
import com.spring.er_spring.er_spring.Repositories.LibraryRepository;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LibraryRepository LibraryRepository;

    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Optional<Library> libraryOptional = LibraryRepository.findById(book.getLibrary().getId());

        if (!libraryOptional.isPresent()) return ResponseEntity.unprocessableEntity().build();

        book.setLibrary(libraryOptional.get());

        Book newBook = bookRepository.save(book);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newBook.getId()).toUri();

        return ResponseEntity.created(location).body(newBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Integer id ,@Valid @RequestBody Book book) {

        Optional<Library> libraryOptional = LibraryRepository.findById(book.getLibrary().getId());
        Optional<Book> bookOptional = bookRepository.findById(id);

        if (!libraryOptional.isPresent() || !bookOptional.isPresent()) return ResponseEntity.unprocessableEntity().build();     
        
        book.setLibrary(libraryOptional.get());
        book.setId(bookOptional.get().getId());

        bookRepository.save(book);
        
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable Integer id) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        if (!bookOptional.isPresent()) return ResponseEntity.unprocessableEntity().build();

        bookRepository.delete(bookOptional.get());
        
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        if (!bookOptional.isPresent()) return ResponseEntity.unprocessableEntity().build();

        return ResponseEntity.ok(bookOptional.get());
    }

    @GetMapping
    public ResponseEntity<Page<Book>> getAllBooks(Pageable pageable) {
        return ResponseEntity.ok(bookRepository.findAll(pageable));
    }

}
