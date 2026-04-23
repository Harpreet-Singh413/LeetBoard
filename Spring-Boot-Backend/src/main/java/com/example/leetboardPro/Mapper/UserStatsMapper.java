package com.example.leetboardPro.Mapper;

import com.example.leetboardPro.DTO.UserStatsDTO;
import com.example.leetboardPro.Model.UserStats;

public class UserStatsMapper {
    public static UserStats toUserStatsEntity(UserStatsDTO dto){
        UserStats stats = new UserStats();
        stats.setLeetcodeUsername(dto.getLeetcodeUsername());
        stats.setEasyCount(dto.getEasyCount());
        stats.setMediumCount(dto.getMediumCount());
        stats.setHardCount(dto.getHardCount());
        stats.setLastSync(dto.getLastSync());
        return stats;
    }

    public static UserStatsDTO toUserStatsDto(UserStats stats){
        UserStatsDTO dto = new UserStatsDTO();
        dto.setLeetcodeUsername(stats.getLeetcodeUsername());
        dto.setEasyCount(stats.getEasyCount());
        dto.setMediumCount(stats.getMediumCount());
        dto.setHardCount(stats.getHardCount());
        dto.setLastSync(stats.getLastSync());
        return dto;
    }
}
