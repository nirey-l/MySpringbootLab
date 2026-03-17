package com.rookies5.MySpringbootLab.dto;

import com.rookies5.MySpringbootLab.entity.Book;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

public class BookDTO {

    // 1. 도서 생성 시 사용하는 DTO (입력값 검증 깐깐하게!)
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookCreateRequest {
        @NotBlank(message = "도서 제목은 필수입니다.")
        private String title;

        @NotBlank(message = "저자는 필수입니다.")
        private String author;

        @NotBlank(message = "ISBN은 필수입니다.")
        private String isbn;

        @NotNull(message = "가격은 필수입니다.")
        @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
        private Integer price;

        private LocalDate publishDate;

        // DTO를 진짜 Entity로 변환해주는 편의 메서드입니다.
        public Book toEntity() {
            return Book.builder()
                    .title(this.title)
                    .author(this.author)
                    .isbn(this.isbn)
                    .price(this.price)
                    .publishDate(this.publishDate)
                    .build();
        }
    }

    // 2. 도서 수정 시 사용하는 DTO (수정할 값만 들어오므로 검증을 약간 느슨하게 할 수 있습니다)
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookUpdateRequest {
        private String title;
        private String author;
        
        @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
        private Integer price;
        
        private LocalDate publishDate;
    }

    // 3. 클라이언트에게 응답할 때 사용하는 DTO
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class BookResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;

        // Entity를 응답용 DTO로 예쁘게 포장해주는 편의 메서드입니다.
        public static BookResponse from(Book book) {
            return BookResponse.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .build();
        }
    }
}