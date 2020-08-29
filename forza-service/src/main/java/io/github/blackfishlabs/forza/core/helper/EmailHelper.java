package io.github.blackfishlabs.forza.core.helper;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class EmailHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailHelper.class);

    public static void sendEmailSendGrid(String emailFrom,
                                         String emailTo,
                                         String subject,
                                         String messageText) {

        Email from = new Email(emailFrom);
        Email to = new Email(emailTo);

        Content content = new Content("text/html", messageText);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            ex.printStackTrace();
            LOGGER.error(ex.getMessage());
        }
    }


}
