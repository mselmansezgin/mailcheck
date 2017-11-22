package com.datascience.com.datascience.mailcheck;

import javax.mail.MessagingException;
import java.io.IOException;

public interface MailChecker {

    public void checkMail() throws MessagingException, IOException;
}
