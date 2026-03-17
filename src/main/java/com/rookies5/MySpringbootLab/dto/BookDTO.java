package com.rookies5.MySpringbootLab.dto; // 💡 회원님의 패키지 경로에 맞게 확인해 주세요!

import com.rookies5.MySpringbootLab.entity.Book;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class BookDTO {

    // ==========================================
    // 1. 도서 등록(POST) 및 전체 수정(PUT)용 DTO
    // ==========================================
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Book title is required")
        private String title;

        @NotBlank(message = "Author name is required")
        private String author;

        @NotBlank(message = "ISBN is required")
        @Pattern(regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$",
                message = "ISBN must be valid (10 or 13 digits, with or without hyphens)")
        private String isbn;

        @PositiveOrZero(message = "Price must be positive or zero")
        private Integer price;

        @Past(message = "Publish date must be in the past")
        private LocalDate publishDate;

        @Valid
        private BookDetailDTO detailRequest;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailDTO {
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }

    // ==========================================
    // 2. 도서 응답(GET, Return)용 DTO
    // ==========================================
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private BookDetailResponse detail;

        // Entity 2개(Book, BookDetail)를 하나의 DTO 상자로 합쳐주는 마법의 메서드!
        public static Response fromEntity(Book book) {
            BookDetailResponse detailResponse = book.getBookDetail() != null
                    ? BookDetailResponse.builder()
                    .id(book.getBookDetail().getId())
                    .description(book.getBookDetail().getDescription())
                    .language(book.getBookDetail().getLanguage())
                    .pageCount(book.getBookDetail().getPageCount())
                    .publisher(book.getBookDetail().getPublisher())
                    .coverImageUrl(book.getBookDetail().getCoverImageUrl())
                    .edition(book.getBookDetail().getEdition())
                    .build()
                    : null;

            return Response.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .detail(detailResponse)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailResponse {
        private Long id;
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }

    // =================================================================
    // ✨ 3. 도서 부분 수정(PATCH)용 DTO (이번 실습의 핵심 추가 부분!)
    // =================================================================
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PatchRequest {
        // 일부 필드만 넘어올 수 있으므로 @NotBlank 같은 깐깐한 제약조건을 빼서 유연하게 만듭니다.
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private BookDetailPatchRequest detailRequest;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailPatchRequest {
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }
}