package com.lenarsharipov.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Serializable entityId;

    private String entityName;

    /*
    * Обычно на практике используется JSON,
    * так как его обрабатывать намного удобнее.
    * */
    private String entityContent;

    @Enumerated(EnumType.STRING)
    private Operation operation;

    public enum Operation {
        SAVE, UPDATE, DELETE, INSERT
    }
}
