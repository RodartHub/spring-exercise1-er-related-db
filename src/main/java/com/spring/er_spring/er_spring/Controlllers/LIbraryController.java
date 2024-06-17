package com.spring.er_spring.er_spring.Controlllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.er_spring.er_spring.Entities.Library;
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
@RequestMapping("/api/libraries")
public class LIbraryController {

    @Autowired
    private LibraryRepository libraryRepository;

    @PostMapping
    public ResponseEntity<Library> createLibrary(@Valid @RequestBody Library library) {
        Library newLibrary = libraryRepository.save(library);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newLibrary.getId()).toUri();

        return ResponseEntity.created(location).body(newLibrary);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Library> updateLibrary(@PathVariable Integer id ,@Valid @RequestBody Library library) {
        Optional<Library> libraryOptional = libraryRepository.findById(id);

        if (!libraryOptional.isPresent()) return ResponseEntity.unprocessableEntity().build();

        library.setId(libraryOptional.get().getId());

        libraryRepository.save(library);
        
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Library> deleteLibrary(@PathVariable Integer id) {
        Optional<Library> libraryOptional = libraryRepository.findById(id);

        if (!libraryOptional.isPresent()) return ResponseEntity.unprocessableEntity().build();

        libraryRepository.delete(libraryOptional.get());
        
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Library> getLibraryById(@PathVariable Integer id) {
        Optional<Library> libraryOptional = libraryRepository.findById(id);

        if (!libraryOptional.isPresent()) return ResponseEntity.unprocessableEntity().build();

        return ResponseEntity.ok(libraryOptional.get());
    }

    @GetMapping
    public ResponseEntity<Page<Library>> getAllLibraries(Pageable pageable) {
        return ResponseEntity.ok(libraryRepository.findAll(pageable));
    }

}