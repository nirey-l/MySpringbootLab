package com.rookies5.MySpringbootLab.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_details")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class BookDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String language;
    private Integer pageCount;
    private String publisher;
    private String coverImageUrl;
    private String edition;

    // FetchType.LAZY: 책 상세 정보는 무거우니까 필요할 때만 DB에서 가져와!
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", unique = true) // 실제 테이블에 book_id 컬럼을 만들고 유니크 키 설정
    private Book book;
}