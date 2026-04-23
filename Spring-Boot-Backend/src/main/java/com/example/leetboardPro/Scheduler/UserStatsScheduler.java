package com.example.leetboardPro.Scheduler;
import com.example.leetboardPro.Model.Users;
import com.example.leetboardPro.Repository.UserRepo;
import com.example.leetboardPro.Service.LeetCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class UserStatsScheduler {
    private final UserRepo userRepo;
    private final LeetCodeService leetCodeService;

    public UserStatsScheduler(UserRepo userRepo, LeetCodeService leetCodeService) {
        this.userRepo = userRepo;
        this.leetCodeService = leetCodeService;
    }


    @Scheduled(cron = "0 0 */6 * * *")
    public void refreshGlobalLeaderBoard(){
        List<Users> users = userRepo.findAll();

        for(Users user : users){
            try {
                leetCodeService.syncAndSaveUserStats(user.getLeetUsername());
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            catch (Exception e){
                log.error("Error Syncing {} : {}" , user.getLeetUsername() , e.getMessage());
            }
        }
    }
}
