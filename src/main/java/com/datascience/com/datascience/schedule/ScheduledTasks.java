package com.datascience.com.datascience.schedule;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.datascience.AppProperties;
import com.datascience.com.datascience.mailcheck.GmailChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private AppProperties appConf;

    @Scheduled(fixedRate = 8000)
    public void checkMailAndCallService() throws IOException {
        log.info("Mail Checker is running - " + dateFormat.format(new Date()));
        GmailChecker gmc = new GmailChecker(appConf);
        gmc.checkMail();

    }
    @Autowired
    public void setAppConf(AppProperties appConf) {
        this.appConf = appConf;
    }
}
