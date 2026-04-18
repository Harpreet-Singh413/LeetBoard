package com.example.leetboardPro.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatsDTO {
    private String leetcodeUsername;
    private int easyCount;
    private int mediumCount;
    private int hardCount;
    private LocalDateTime lastSync;

}
