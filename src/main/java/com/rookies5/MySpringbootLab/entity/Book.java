package com.rookies5.MySpringbootLab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String isbn;
    private Integer price;
    private LocalDate publishDate;

    // mappedBy = "book": "나는 주인이 아니야. 저쪽 BookDetail의 'book' 변수가 진짜 외래키를 관리해"
    // CascadeType.ALL: Book을 저장/삭제할 때 짝꿍인 BookDetail도 알아서 같이 저장/삭제해!
    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BookDetail bookDetail;

    // 💡 양방향 관계를 안전하게 맺어주기 위한 편의 메서드입니다.
    public void setBookDetail(BookDetail bookDetail) {
        this.bookDetail = bookDetail;
        if (bookDetail != null) {
            bookDetail.setBook(this); // 서로를 바라보게 세팅!
        }
    }
}