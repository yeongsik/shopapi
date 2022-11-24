package com.shopapi.member.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADDRESS_ID")
    private Long id;

    private String city;
    private String street;
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}

