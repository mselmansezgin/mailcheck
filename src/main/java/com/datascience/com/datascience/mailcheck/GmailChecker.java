package com.datascience.com.datascience.mailcheck;

import com.datascience.AppProperties;
import com.datascience.com.datascience.connection.OracleDbConnection;
import com.datascience.com.datascience.dboperations.DbOperation;
import com.datascience.com.datascience.dboperations.OracleDbUserOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.mail.*;
import javax.mail.search.FlagTerm;
import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class GmailChecker extends AbstractMailChecker {
    private final Logger log = LoggerFactory.getLogger(GmailChecker.class);

    private AppProperties props;
    Store store;
    Folder inbox;
    Message[] messages;

    public GmailChecker(){}

    public GmailChecker(AppProperties props){
        this.props = props;
    }


    public void initialize()  {

        Session session = Session.getDefaultInstance(new Properties( ));
        try {

            try {
                doTrustToCertificates();
            } catch (Exception e) {
                e.printStackTrace();
            }
            store = session.getStore("imaps");
            store.connect(props.getHost(), Integer.parseInt(props.getPort()), props.getUserName(), props.getPassword());
            inbox = store.getFolder( "INBOX" );
            inbox.open( Folder.READ_WRITE );
            // Fetch unseen messages from inbox folder
            messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            for ( Message message : messages ) {
                try {

                    if (message.getSubject().contains("trivago_hotel_repor_ej") ){ //TODO
                        Connection conn = OracleDbConnection.getConnection(props);
                        OracleDbUserOperation odo = new OracleDbUserOperation(conn);
                        callDbProcedure(odo);

                    }else{
                        //AnotherDbOperation aaa = AnotherDbOperation();
                        //callDbProcedure(aaa);
                    }


                    message.setFlag(Flags.Flag.SEEN,true);
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            inbox.close(true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void checkMail() {

        initialize();


    }

    private void callDbProcedure(DbOperation dop) {
        try {
            dop.call();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    // trusting all certificate
    public void doTrustToCertificates() throws Exception {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                    System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
                }
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }






}
