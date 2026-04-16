package com.example.leetboardPro.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UserStats {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private Users user;

    private String leetcodeUsername;
    private int easyCount;
    private int mediumCount;
    private int hardCount;
    private LocalDateTime lastSync;
}
