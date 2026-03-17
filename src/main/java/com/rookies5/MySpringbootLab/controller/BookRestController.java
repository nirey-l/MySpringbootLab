package com.rookies5.MySpringbootLab.controller;

import com.rookies5.MySpringbootLab.dto.BookDTO;
import com.rookies5.MySpringbootLab.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDTO.Response>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.Response> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO.Response> getBookByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getBookByIsbn(isbn));
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<BookDTO.Response>> searchByAuthor(@RequestParam String author) {
        return ResponseEntity.ok(bookService.searchByAuthor(author));
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BookDTO.Response>> searchByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.searchByTitle(title));
    }

    @PostMapping
    public ResponseEntity<BookDTO.Response> createBook(@Valid @RequestBody BookDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO.Response> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO.Request request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    // 💡 부분 수정 요청(PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<BookDTO.Response> patchBook(@PathVariable Long id, @RequestBody BookDTO.PatchRequest request) {
        return ResponseEntity.ok(bookService.patchBook(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }
}