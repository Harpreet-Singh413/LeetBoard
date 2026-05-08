package com.example.leetboardPro.Controller;

import com.example.leetboardPro.DTO.UserStatsDTO;
import com.example.leetboardPro.Model.Users;
import com.example.leetboardPro.Service.UserStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.leetboardPro.Service.LeetCodeService;

import java.util.List;

@RestController
@RequestMapping("api")
public class UserStatsController {

    @Autowired
    private UserStatsService userStatsService;

    @Autowired
    private LeetCodeService leetCodeService;

    @GetMapping("/stats")
    public ResponseEntity<List<UserStatsDTO>> getAllStats(){
        List<UserStatsDTO> stats = userStatsService.getAllStats();
        if(stats.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(stats);
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

    @GetMapping("/stats/{id}")
    public ResponseEntity<UserStatsDTO> getUserStats(@PathVariable Long id){
        UserStatsDTO user = userStatsService.getUserStats(id);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }



    @GetMapping("/stats/me")
    public ResponseEntity<UserStatsDTO> getMyStats(@AuthenticationPrincipal Users currentUser) {
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserStatsDTO stats = userStatsService.getUserStats(currentUser.getId());
        if (stats == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @PostMapping("/stats/sync")
    public ResponseEntity<UserStatsDTO> syncMyStats(@AuthenticationPrincipal Users currentUser) {
        if (currentUser == null || currentUser.getLeetUsername() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserStatsDTO updated = leetCodeService.syncAndSaveUserStats(currentUser);
        return ResponseEntity.ok(updated);
    }
}
