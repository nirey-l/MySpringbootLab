package com.rookies5.MySpringbootLab.service;

import com.rookies5.MySpringbootLab.dto.BookDTO;
import com.rookies5.MySpringbootLab.entity.Book;
import com.rookies5.MySpringbootLab.exception.BusinessException;
import com.rookies5.MySpringbootLab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // final이 붙은 필드의 생성자를 알아서 만들어줍니다. (의존성 주입)
@Transactional(readOnly = true) // 기본적으로 읽기 전용으로 설정하여 성능을 높입니다.
public class BookService {

    private final BookRepository bookRepository;

    // 1. 모든 도서 조회
    public List<BookDTO.BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(book -> BookDTO.BookResponse.from(book))
                .collect(Collectors.toList());
    }

    // 2. ID로 도서 조회
    public BookDTO.BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("해당 ID의 도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));
        return BookDTO.BookResponse.from(book);
    }

    // 3. ISBN으로 도서 조회
    public BookDTO.BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("해당 ISBN의 도서를 찾을 수 없습니다: " + isbn, HttpStatus.NOT_FOUND));
        return BookDTO.BookResponse.from(book);
    }

    // 4. 새 도서 등록 (데이터가 변경되므로 @Transactional 필수)
    @Transactional
    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request) {
        Book book = request.toEntity();
        Book savedBook = bookRepository.save(book);
        return BookDTO.BookResponse.from(savedBook);
    }

    // 5. 도서 정보 수정 (요구사항 반영: 입력값이 있는 경우에만 수정!)
    @Transactional
    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request) {
        // 1. 먼저 DB에서 기존 책을 꺼내옵니다.
        Book existBook = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("수정할 도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));

        // 2. 변경이 필요한 필드만 업데이트 (null이 아닐 때만 덮어씌움)
        if (request.getTitle() != null) {
            existBook.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            existBook.setAuthor(request.getAuthor());
        }
        if (request.getPrice() != null) {
            existBook.setPrice(request.getPrice());
        }
        if (request.getPublishDate() != null) {
            existBook.setPublishDate(request.getPublishDate());
        }

        // JPA의 변경 감지(Dirty Checking) 기능 덕분에 save()를 따로 호출하지 않아도 
        // 트랜잭션이 끝날 때 알아서 DB에 UPDATE 쿼리가 날아갑니다!
        return BookDTO.BookResponse.from(existBook);
    }

    // 6. 도서 삭제
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("삭제할 도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));
        bookRepository.delete(book);
    }
}