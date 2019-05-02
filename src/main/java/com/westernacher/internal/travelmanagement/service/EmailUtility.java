package com.westernacher.internal.travelmanagement.service;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

@Service
@PropertySource("classpath:/message.properties")
public class EmailUtility {

    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    String from;

    @Autowired
    private ConfigUtility configUtil;

    public void send( String to,
                           String subjectKey,
                           String bodyKey,
                           String[] subjectParameters,
                           String[] bodyParameters) throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String subject = MessageFormat.format(configUtil.getProperty(subjectKey),
                                Arrays.copyOf(subjectParameters,subjectParameters.length, Object[].class));
        String body = MessageFormat.format(configUtil.getProperty(bodyKey),
                                Arrays.copyOf(bodyParameters,bodyParameters.length, Object[].class));

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);

        sender.send(message);
    }

    public void send(List<String> toList,
                      String subjectKey,
                      String bodyKey,
                      String[] subjectParameters,
                      String[] bodyParameters){
        try{
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            String subject = MessageFormat.format(configUtil.getProperty(subjectKey),
                    Arrays.copyOf(subjectParameters,subjectParameters.length, Object[].class));
            String body = MessageFormat.format(configUtil.getProperty(bodyKey),
                    Arrays.copyOf(bodyParameters,bodyParameters.length, Object[].class));

            helper.setFrom(from);
            helper.setBcc(InternetAddress.parse(StringUtils.join(toList, ',')));
            helper.setSubject(subject);
            helper.setText(body);

            sender.send(message);
        }catch(Exception e){

        }
    }
}

@Configuration
class ConfigUtility {

    @Autowired
    private Environment environment;

    public String getProperty(String pPropertyKey) {
        return environment.getProperty(pPropertyKey);
    }
}
