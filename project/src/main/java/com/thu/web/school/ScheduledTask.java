package com.thu.web.school;

/**
 * Created by zyf on 2016/12/4.
 */

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling
public class ScheduledTask {
    TokenMap tokenMap = new TokenMap();

    @Scheduled(fixedRate = 5000)
    //@Scheduled(cron = "0 0 2 * * ?")
    public void reportCurrentTime(){
        //for (Map.Entry<String, Date> map:token_time.)
        //tokenMap.clear();
        //tokenMap.setToken("123","123");
        if (tokenMap.isEmpty()){
            System.out.println("ScheduledTask: token pool is already empty!");
        }
        else {
            tokenMap.clear();
            System.out.println("SacheduledTask: token pool is cleared!" );
        }
    }

}
