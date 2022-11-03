package com.prominent.title.entity.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "document_binaries")
public class DocumentBinaries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int documentId;
    @Column(nullable = false)
    private String documentData;

}
