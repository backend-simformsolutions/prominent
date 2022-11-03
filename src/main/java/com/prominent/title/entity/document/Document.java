package com.prominent.title.entity.document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int documentId;
    @Column(length = 500)
    private String documentType;
    @Column(nullable = false, length = 500)
    private String documentName;
    @Column(length = 50)
    private String documentExtension;
    private double sizeInMb;
    @Column(nullable = false)
    private int createUserId;
    private int updateUserId;
    private LocalDate createDate;
    private LocalDate updateDate;
    @Column(nullable = false, length = 100)
    private String internalKey;
    @Column(nullable = false, length = 10)
    private String concurrencyKey;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private DocumentBinaries documentBinaries;
}
