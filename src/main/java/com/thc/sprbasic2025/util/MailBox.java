package com.thc.sprbasic2025.util;

import jakarta.mail.*;
import jakarta.mail.search.FlagTerm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Properties;

@RequiredArgsConstructor
@Component
public class MailBox {

    final private UtilProperties utilProperties;

    public boolean phone(String phone, String code) {

        String tempPhone = phone.replaceAll("-", "");
        System.out.println("phone : " + tempPhone);

        String host = utilProperties.getHost();
        String username = utilProperties.getUsername();
        String password = utilProperties.getPassword(); // Gmail은 앱 비밀번호 필요

        Properties props = new Properties();

        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", host);
        props.put("mail.imaps.port", "993");
        props.put("mail.imaps.ssl.enable", "true");
        props.put("mail.imaps.ssl.trust", "*");

        props.put("mail.imaps.connectiontimeout", "15000");
        props.put("mail.imaps.timeout", "15000");
        props.put("mail.imaps.writetimeout", "15000");

        boolean result = false;

        try {
            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore("imaps");
            store.connect(host, username, password);

            Folder inbox = store.getFolder("INBOX");
            //inbox.open(Folder.READ_ONLY);
            inbox.open(Folder.READ_WRITE);

            // 읽지 않은 메일만 검색
            Message[] messages = inbox.search(
                    new FlagTerm(new Flags(Flags.Flag.SEEN), false)
            );

            System.out.println("읽지 않은 메일 개수: " + messages.length);
            for (Message msg : messages) {
                String from = msg.getFrom()[0].toString();
                System.out.println("from : " + from);

                Object content = msg.getContent();
                String text_content = null;
                if (content instanceof String) {
                    //System.out.println("본문: " + content);
                    text_content = content.toString();
                } else if (content instanceof Multipart) {
                    Multipart multipart = (Multipart) content;
                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart bodyPart = multipart.getBodyPart(i);
                        if (bodyPart.isMimeType("text/plain")) {
                            //System.out.println("텍스트 본문: " + bodyPart.getContent());
                            text_content = bodyPart.getContent() + "";
                        } else if (bodyPart.isMimeType("text/html")) {
                            //System.out.println("HTML 본문: " + bodyPart.getContent());
                            text_content = bodyPart.getContent() + "";
                        }
                    }
                }

                if(from.contains(tempPhone)){
                    System.out.println("true 1: " + true);
                    if(text_content != null && text_content.contains(code)){
                        System.out.println("true 2: " + true);
                        msg.setFlag(Flags.Flag.DELETED, true); // 삭제 플래그
                        result = true;
                        break;
                    }
                }
            }
            inbox.close(true);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
