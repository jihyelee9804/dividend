package com.jihye.dividend.persist.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "COMPANY")
@Getter // 멤버변수의 값을 읽어오는 getter 메소드를 생성한다.
@ToString // 인스턴스 출력 시에 편의를 높여주는 toString 메소드를 overrride해서 생성한다.
@NoArgsConstructor // 기본 생성자를 만든다.
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) // 중복되지 않는다.
    private String ticker;
    private String name;
}
