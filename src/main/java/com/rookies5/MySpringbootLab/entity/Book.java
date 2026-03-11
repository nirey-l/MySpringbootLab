package com.rookies5.MySpringbootLab.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter
@Setter // 테스트에서 값을 수정(Update)하기 위해 추가합니다.
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id // 이 필드가 테이블의 기본 키(Primary Key)임을 알립니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 번호를 자동으로 1씩 증가시키도록 합니다 (Auto Increment).
    private Long id;

    private String title;
    private String author;
    private String isbn;
    private Integer price;
    private LocalDate publishDate;
}