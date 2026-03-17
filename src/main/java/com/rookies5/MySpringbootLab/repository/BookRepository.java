package com.rookies5.MySpringbootLab.repository;

import com.rookies5.MySpringbootLab.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    // JOIN FETCH를 사용하여 Book을 찾을 때 BookDetail도 한 번의 쿼리로 같이 가져옵니다.
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.bookDetail WHERE b.id = :id")
    Optional<Book> findByIdWithBookDetail(@Param("id") Long id);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.bookDetail WHERE b.isbn = :isbn")
    Optional<Book> findByIsbnWithBookDetail(@Param("isbn") String isbn);

    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn); // ISBN 중복 체크용

    // 저자나 제목에 특정 단어가 포함된 책 찾기 (대소문자 무시)
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByTitleContainingIgnoreCase(String title);
}