package com.pashonokk.dvdrental.batch;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "batch_init_version")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BatchInit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long version;

    public BatchInit(Long version) {
        this.version = version;
    }
}
