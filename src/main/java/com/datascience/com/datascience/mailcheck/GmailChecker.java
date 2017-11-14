package com.datascience.com.datascience.mailcheck;

import com.datascience.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.Properties;

public class GmailChecker extends AbstractMailChecker {
    Store store;
    Folder inbox;
    Message[] messages;

    private static AppProperties props;


    public void initialize() throws MessagingException, IOException {

        Session session = Session.getDefaultInstance(new Properties( ));
        try {
            store = session.getStore("imaps");
            store.connect(props.getHost(), Integer.parseInt(props.getPort()), props.getUserName(), props.getPassword());
            inbox = store.getFolder( "INBOX" );
            inbox.open( Folder.READ_WRITE );
            // Fetch unseen messages from inbox folder
            messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        // Sort messages from recent to oldest
        /*
        Arrays.sort( messages, (m1, m2 ) -> {
            try {
                return m2.getSentDate().compareTo( m1.getSentDate() );
            } catch ( MessagingException e ) {
                throw new RuntimeException( e );
            }
        } );
        */
        for ( Message message : messages ) {
            log.info("Mail delivered {}", " subject:" + message.getSubject());

            String messageBody = convertBody2String(message);

            message.setFlag(Flags.Flag.SEEN,true);
        }
        inbox.close(true);
        store.close();

    }

    @Override
    public boolean checkMail() throws MessagingException, IOException {

        initialize();

        if (messages.length > 0){

            return true;

        }

        return false;
    }

    @Autowired
    public void setProps(AppProperties props){
        this.props  = props;
    }





}
