package com.example.demo.product.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "PRODUCT")
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int key;

    @Column(name = "name")
    private String name;

    @Column(name = "total_count")
    private int totalCount;

    @Column(name = "remain_count")
    private int remainCount;

    public void buy(int buyCount) {

        this.remainCount = this.remainCount - buyCount;
    }

    public String remainStatus() {

        return this.remainCount <= 0? "재고가 없습니다." : String.format("%s 개 남았습니다.", this.remainCount);
    }

    public boolean possibleBuy(int buyCount) {

        return this.remainCount != 0 && this.remainCount - buyCount >= 0;
    }
}
