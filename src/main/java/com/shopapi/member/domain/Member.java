package com.shopapi.member.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    private String email;

    private String name;

    private String password;

    @OneToMany(mappedBy = "member")
    private List<Address> addressList;

    // 주소

    // 회원의 쿠폰 리스트

    // 회원의 포인트

    // 주문 정보

    // 장바구니

    // 좋아요 목록

    // 회원 사이즈 정보

    // 이메일 등 광고 메시지 수신 여부

    // 최근 본 상품
}
