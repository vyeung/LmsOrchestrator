package com.st.orchestrator.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orch")
public class Orchestrator {

    //Akbar's example code
//    @Autowired
//    RestTemplate restTemplate;
//    @RequestMapping("/authors/{authorId}")
//    public Author getAuthorById(@PathVariable("authorId") int authorId){
//        return restTemplate.getForEntity("http://author-service/lms/authors/"+authorId, Author.class).getBody();
//    }
//
//    @RequestMapping("/books/{bookId}")
//    public Book getBookById(@PathVariable("bookId") int bookId){
//        return restTemplate.getForEntity("http://book-service/lms/books/"+bookId, Book.class).getBody();
//    }
}
