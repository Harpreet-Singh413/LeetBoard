package com.example.leetboardPro.Model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(name = "leet_Username" , unique = true)
    private String leetUsername;

    @Column(nullable = false, unique = true)
    private String email;

    private String course;
    private Integer semester;

    private boolean isOnboarded = false;
}
