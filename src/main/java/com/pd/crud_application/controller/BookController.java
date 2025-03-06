package com.pd.crud_application.controller;

import com.pd.crud_application.model.Book;
import com.pd.crud_application.repository.BookRepository;
import com.sun.source.tree.LambdaExpressionTree;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BookController {


    private final BookRepository repository;

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<Book>> getAllBooks(){
        try {

            List<Book> books = new ArrayList<>();
            repository.findAll().forEach(books::add);

            if(books.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(books,HttpStatus.OK);

        }catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getBookById/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id){

        Optional<Book> bookData = repository.findById(id);
        if(bookData.isPresent()){
            return new ResponseEntity<>(bookData.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addBook")
    public ResponseEntity<Book> addBook(@RequestBody Book book){

        Book bookData = repository.save(book);

        return new ResponseEntity<>(bookData,HttpStatus.OK);
    }

    @PostMapping("/updateBokById/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable Long id, @RequestBody Book updatedBookData){

        Optional<Book> bookByIdOpt = repository.findById(id);
        return bookByIdOpt.map(book -> {
            book.setTitle(updatedBookData.getTitle());
            book.setAuthor(updatedBookData.getAuthor());
            return new ResponseEntity<>(repository.save(book),HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/deleteBokById/{id}")
    public ResponseEntity<HttpStatus> deleteBookById(@PathVariable Long id){

        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
