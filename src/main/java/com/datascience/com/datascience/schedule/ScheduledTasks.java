package com.datascience.com.datascience.schedule;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.datascience.com.datascience.mailcheck.GmailChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    @Scheduled(fixedRate = 8000)
    public void checkMailAndCallService() throws IOException {
        log.info("Mail Checker is running - " + dateFormat.format(new Date()));
        GmailChecker gmc = new GmailChecker();

        try {
            if (gmc.checkMail() ) {
                //call webservice
                //TODO
                log.info("WS Call will be here!!! - " + dateFormat.format(new Date()));
            }
        } catch (MessagingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
