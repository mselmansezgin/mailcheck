package com.datascience.com.datascience.mailcheck;

import com.datascience.com.datascience.schedule.ScheduledTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import java.io.IOException;

public abstract class AbstractMailChecker implements MailChecker {
    public final Logger log = LoggerFactory.getLogger(AbstractMailChecker.class);

    public String handleMultipart(Message msg) {

        String content = null;
        try {
            String disposition;
            BodyPart part;
            Multipart mp = (Multipart) msg.getContent();

            int mpCount = mp.getCount();
            for (int m = 0; m < mpCount; m++) {
                part = mp.getBodyPart(m);

                disposition = part.getDisposition();
                if (disposition != null && disposition.equals(Part.INLINE)) {
                    content = part.getContent().toString();
                } else {
                    content = part.getContent().toString();

                }
            }
        } catch (IOException ex) {
            log.error("Mail delivered {}", "IOException :" + ex.getMessage());

        } catch (MessagingException ex) {
            log.error("Mail delivered {}", "MessagingException :" + ex.getMessage());

        }
        return content;
    }

    public String convertBody2String (Message message) throws IOException, MessagingException {
        String messageContent = null;
        Object content=message.getContent();

        if(message.isMimeType("text/plain")){
            messageContent = message.getContent().toString();
            log.info("Mail delivered {}", "Text Plain : " + messageContent);
        }else if (content instanceof  Multipart){
            messageContent = handleMultipart(message);
            log.info("Mail delivered {}", "Multipart : " + messageContent);
        }else{
            messageContent = null;
            log.info("Mail delivered {}", " No Content : " + messageContent);
        }

        return messageContent;

    }
}
