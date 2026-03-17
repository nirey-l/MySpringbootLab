package com.rookies5.MySpringbootLab.repository;

import com.rookies5.MySpringbootLab.entity.BookDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookDetailRepository extends JpaRepository<BookDetail, Long> {
    Optional<BookDetail> findByBookId(Long bookId);

    @Query("SELECT d FROM BookDetail d JOIN FETCH d.book WHERE d.id = :id")
    Optional<BookDetail> findByIdWithBook(@Param("id") Long id);

    List<BookDetail> findByPublisher(String publisher);
}