package com.rookies5.MySpringbootLab.repository;

import com.rookies5.MySpringbootLab.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

// JpaRepository<관리할 엔티티 클래스, 그 엔티티의 PK(ID) 타입> 을 상속받습니다.
public interface BookRepository extends JpaRepository<Book, Long> {

    // 1. ISBN으로 도서 찾기 (결과가 없거나 1개이므로 Optional 사용)
    Optional<Book> findByIsbn(String isbn);

    // 2. 저자명으로 도서 찾기 (홍길동이 쓴 책이 여러 권일 수 있으므로 List 사용)
    List<Book> findByAuthor(String author);
}