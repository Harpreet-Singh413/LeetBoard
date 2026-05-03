package com.example.leetboardPro.Service;
import com.example.leetboardPro.DTO.UserStatsDTO;
import com.example.leetboardPro.Mapper.UserStatsMapper;
import com.example.leetboardPro.Model.UserStats;
import com.example.leetboardPro.Repository.UserStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.leetboardPro.Mapper.UserStatsMapper.toUserStatsDto;

@Service
public class UserStatsService {
    @Autowired
    private UserStatsRepository userStatsRepository;
    public List<UserStatsDTO> getAllStats(){
        return userStatsRepository.findAll()
                .stream()
                .map(stats -> new UserStatsDTO(
                        stats.getLeetcodeUsername(),
                        stats.getUser(),
                        stats.getEasyCount(),
                        stats.getMediumCount(),
                        stats.getHardCount(),
                        stats.getLastSync()
                ))
                .toList();
    }

    public UserStatsDTO getUserStats(Long id) {
        UserStats stats =  userStatsRepository.findByUserId(id).orElse(null);
        UserStatsDTO dto = toUserStatsDto(stats);
        return dto;
    }

    public List<UserStatsDTO> getLeaderBoard() {
        return userStatsRepository.findAllByOrderByHardCountDescMediumCountDescEasyCountDesc()
                .stream()
                .map(UserStatsMapper::toUserStatsDto)
                .toList();
    }
}
