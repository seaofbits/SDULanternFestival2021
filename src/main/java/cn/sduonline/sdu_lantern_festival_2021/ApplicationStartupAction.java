package cn.sduonline.sdu_lantern_festival_2021;

import cn.sduonline.sdu_lantern_festival_2021.service.riddle.RiddleService;
import cn.sduonline.sdu_lantern_festival_2021.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
public class ApplicationStartupAction implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartupAction.class);

    @Autowired
    RiddleService riddleService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        riddleService.loadAllRiddlesToRedis();
        logger.info("Riddles loaded into Redis");
    }
}
