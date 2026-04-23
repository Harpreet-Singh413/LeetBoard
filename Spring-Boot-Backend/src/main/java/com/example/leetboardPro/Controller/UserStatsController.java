package com.example.leetboardPro.Controller;

import com.example.leetboardPro.DTO.UserStatsDTO;
import com.example.leetboardPro.Service.UserStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class UserStatsController {

    @Autowired
    private UserStatsService userStatsService;

    @GetMapping("/stats")
    public ResponseEntity<List<UserStatsDTO>> getAllStats(){
        List<UserStatsDTO> stats = userStatsService.getAllStats();
        if(stats.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/{id}")
    public ResponseEntity<UserStatsDTO> getUserStats(@PathVariable Long id){
        UserStatsDTO user = userStatsService.getUserStats(id);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/stats/leaderboard")
    public ResponseEntity<List<UserStatsDTO>> getLeaderBoard(){
        List<UserStatsDTO> leaderboard = userStatsService.getLeaderBoard();
        if(leaderboard.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<List<UserStatsDTO>>(leaderboard,HttpStatus.OK);
        }
    }
}
