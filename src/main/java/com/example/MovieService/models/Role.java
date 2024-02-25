package com.example.MovieService.models;

import jakarta.validation.constraints.Min;
import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Min(value = 1)
    private String name;
}
