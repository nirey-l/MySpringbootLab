package com.rookies5.MySpringbootLab.repository;

import com.rookies5.MySpringbootLab.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // JPA 관련 설정만 쏙 뽑아서 가볍고 빠르게 DB 테스트를 진행하게 해줍니다.
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    // 1. 도서 등록 테스트
    @Test
    void testCreateBook() {
        // given (주어진 데이터)
        Book book = Book.builder()
                .title("스프링 부트 입문")
                .author("홍길동")
                .isbn("9788956746425")
                .price(30000)
                .publishDate(LocalDate.of(2025, 5, 7))
                .build();

        // when (실행)
        Book savedBook = bookRepository.save(book);

        // then (검증)
        assertThat(savedBook.getId()).isNotNull(); // DB에 들어가서 ID가 발급되었는가?
        assertThat(savedBook.getTitle()).isEqualTo("스프링 부트 입문");
    }

    // 2. ISBN으로 도서 조회 테스트
    @Test
    void testFindByIsbn() {
        // given
        Book book = Book.builder().title("JPA 프로그래밍").author("박둘리").isbn("9788956746432").price(35000).publishDate(LocalDate.of(2025, 4, 30)).build();
        bookRepository.save(book);

        // when
        Optional<Book> foundBook = bookRepository.findByIsbn("9788956746432");

        // then
        assertThat(foundBook).isPresent(); // 책이 존재하는가?
        assertThat(foundBook.get().getAuthor()).isEqualTo("박둘리"); // 그 책의 저자가 박둘리가 맞는가?
    }

    // 3. 저자명으로 도서 목록 조회 테스트
    @Test
    void testFindByAuthor() {
        // given (홍길동 책 1권, 박둘리 책 1권 저장)
        bookRepository.save(Book.builder().title("스프링 부트 입문").author("홍길동").build());
        bookRepository.save(Book.builder().title("JPA 프로그래밍").author("박둘리").build());

        // when
        List<Book> books = bookRepository.findByAuthor("홍길동");

        // then
        assertThat(books).hasSize(1); // 홍길동의 책은 1권만 나와야 함
        assertThat(books.get(0).getTitle()).isEqualTo("스프링 부트 입문");
    }

    // 4. 도서 정보 수정 테스트
    @Test
    void testUpdateBook() {
        // given
        Book book = bookRepository.save(Book.builder().title("스프링 부트 입문").price(30000).build());

        // when (가격을 35000원으로 인상)
        book.setPrice(35000);
        Book updatedBook = bookRepository.save(book);

        // then
        assertThat(updatedBook.getPrice()).isEqualTo(35000);
    }

    // 5. 도서 삭제 테스트
    @Test
    void testDeleteBook() {
        // given
        Book book = bookRepository.save(Book.builder().title("지워질 책").build());
        Long bookId = book.getId();

        // when
        bookRepository.delete(book);
        Optional<Book> deletedBook = bookRepository.findById(bookId);

        // then
        assertThat(deletedBook).isEmpty(); // 삭제되었으니 빈 값(Empty)이어야 함
    }
}