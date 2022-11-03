package com.prominent.title.entity.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "county_id", nullable = false)
    private int countyId;

    private String name;

    @Column(nullable = false)
    private int createUserId;
    private int updateUserId;
    @Column(nullable = false)
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    @Column(nullable = false, length = 100)
    private String internalKey;
    @Column(nullable = false, length = 10)
    private String concurrencyKey;

    public City(String name) {
        this.name = name;
    }
}
