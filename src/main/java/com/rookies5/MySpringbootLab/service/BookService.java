package com.rookies5.MySpringbootLab.service;

import com.rookies5.MySpringbootLab.dto.BookDTO;
import com.rookies5.MySpringbootLab.entity.Book;
import com.rookies5.MySpringbootLab.entity.BookDetail;
import com.rookies5.MySpringbootLab.exception.BusinessException;
import com.rookies5.MySpringbootLab.repository.BookDetailRepository;
import com.rookies5.MySpringbootLab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;

    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public BookDTO.Response getBookById(Long id) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntity(book);
    }

    public BookDTO.Response getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다: " + isbn, HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntity(book);
    }

    public List<BookDTO.Response> searchByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author).stream()
                .map(BookDTO.Response::fromEntity).collect(Collectors.toList());
    }

    public List<BookDTO.Response> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(BookDTO.Response::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("이미 존재하는 ISBN 입니다: " + request.getIsbn(), HttpStatus.CONFLICT);
        }

        // 1. Book 기본 정보 조립
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        // 2. 상세 정보가 들어왔다면 BookDetail 조립
        if (request.getDetailRequest() != null) {
            BookDetail detail = BookDetail.builder()
                    .description(request.getDetailRequest().getDescription())
                    .language(request.getDetailRequest().getLanguage())
                    .pageCount(request.getDetailRequest().getPageCount())
                    .publisher(request.getDetailRequest().getPublisher())
                    .coverImageUrl(request.getDetailRequest().getCoverImageUrl())
                    .edition(request.getDetailRequest().getEdition())
                    .build();
            // 양방향 세팅! (이 코드가 있어야 DB에 외래키가 정상적으로 들어갑니다)
            book.setBookDetail(detail);
        }

        Book savedBook = bookRepository.save(book); // Cascade 옵션 덕분에 한 번에 저장!
        return BookDTO.Response.fromEntity(savedBook);
    }

    // 통째로 덮어씌우는 PUT 수정
    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.Request request) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));

        validateIsbnDuplicate(book, request.getIsbn());

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());

        if (request.getDetailRequest() != null && book.getBookDetail() != null) {
            book.getBookDetail().setDescription(request.getDetailRequest().getDescription());
            book.getBookDetail().setLanguage(request.getDetailRequest().getLanguage());
            book.getBookDetail().setPageCount(request.getDetailRequest().getPageCount());
            book.getBookDetail().setPublisher(request.getDetailRequest().getPublisher());
            book.getBookDetail().setCoverImageUrl(request.getDetailRequest().getCoverImageUrl());
            book.getBookDetail().setEdition(request.getDetailRequest().getEdition());
        }

        return BookDTO.Response.fromEntity(book);
    }

    // 💡 부분 수정 PATCH (Null이 아닌 것만 덮어씌움)
    @Transactional
    public BookDTO.Response patchBook(Long id, BookDTO.PatchRequest request) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));

        if (request.getIsbn() != null) validateIsbnDuplicate(book, request.getIsbn());

        if (request.getTitle() != null) book.setTitle(request.getTitle());
        if (request.getAuthor() != null) book.setAuthor(request.getAuthor());
        if (request.getIsbn() != null) book.setIsbn(request.getIsbn());
        if (request.getPrice() != null) book.setPrice(request.getPrice());
        if (request.getPublishDate() != null) book.setPublishDate(request.getPublishDate());

        if (request.getDetailRequest() != null && book.getBookDetail() != null) {
            BookDTO.BookDetailPatchRequest detailReq = request.getDetailRequest();
            if (detailReq.getDescription() != null) book.getBookDetail().setDescription(detailReq.getDescription());
            if (detailReq.getLanguage() != null) book.getBookDetail().setLanguage(detailReq.getLanguage());
            if (detailReq.getPageCount() != null) book.getBookDetail().setPageCount(detailReq.getPageCount());
            if (detailReq.getPublisher() != null) book.getBookDetail().setPublisher(detailReq.getPublisher());
            if (detailReq.getCoverImageUrl() != null) book.getBookDetail().setCoverImageUrl(detailReq.getCoverImageUrl());
            if (detailReq.getEdition() != null) book.getBookDetail().setEdition(detailReq.getEdition());
        }

        return BookDTO.Response.fromEntity(book);
    }

    // 💡 ISBN 중복 체크 분리 메서드
    private void validateIsbnDuplicate(Book book, String newIsbn) {
        if (!book.getIsbn().equals(newIsbn) && bookRepository.existsByIsbn(newIsbn)) {
            throw new BusinessException("이미 사용중인 ISBN 입니다: " + newIsbn, HttpStatus.CONFLICT);
        }
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND));
        bookRepository.delete(book);
    }
}