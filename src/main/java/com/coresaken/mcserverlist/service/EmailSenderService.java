package com.coresaken.mcserverlist.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    JavaMailSender mailSender;
    @Value("${website.address}")
    String websiteAddress;

    //TODO Change emails preview
    public void sendActiveAccountEmail(String to, String activeAccountToken) throws MessagingException{
        String activeAccountLink = websiteAddress+"auth/active/"+activeAccountToken;

        String activeAccountEmail = "<html>"
                + "<body style='margin: 0; padding: 0; font-size: 1.2em; background-color: #e9e9e9; font-family: system-ui, -apple-system, BlinkMacSystemFont, \'Segoe UI\', Roboto, Oxygen, Ubuntu, Cantarell, \'Open Sans\', \'Helvetica Neue\', sans-serif;'>"
                + "    <div id='header' style='background-color: #1f1f7c; width: 100%; height: 250px;'></div>"
                + "    <div id='panel' style='background-color: white; width: 700px; margin-left: auto; margin-right: auto; margin-top: -100px; border-radius: 5px;'>"
                + "        <div id='logo' style='width: 250px; padding: 25px; margin-left: auto; margin-right: auto;'>"
                + "            <img src='https://zapodaj.net/images/f86bc94eb0cbe.png' style='width: 100%; height: auto;'>"
                + "        </div>"
                + "        <div id='container' style='padding: 25px'>"
                + "            Dziękujemy za zarejestrowanie się na <span style='font-weight: 800;'>MC-List.pl!</span><br><br><br>"
                + "            Aby zakończyć proces rejestracji i aktywować swoje konto, prosimy o kliknięcie poniższy przycisk:"
                + "            <a href='"+activeAccountLink+"' style='text-decoration: none;'>"
                + "                <div id='button' style='background-color: #ffbe4d; padding: 10px 50px; width: fit-content; border-radius: 5px; color: white; font-weight: 800; margin-top: 20px; margin-bottom: 30px; margin-left: auto; margin-right: auto;'>"
                + "                    Aktywuj konto"
                + "                </div>"
                + "            </a>"
                + "            Jeśli przycisk nie działa, przejdź na poniższy adres URL:<br>"
                + "            <span style='color: #ffbe4d'><a href='"+activeAccountLink+"'>"+activeAccountLink+"</a></span>"
                + "            <br><br><br>Jeśli nie rejestrowałeś/aś się na naszej stronie, zignoruj tę wiadomość."
                + "        </div>"
                + "    </div>"
                + "    <div id='panel-down' style='background-color: #d1d1d1; padding: 25px 50px; width: 600px; margin-left: auto; margin-right: auto; margin-top: 50px; margin-bottom: 150px; text-align: center;'>"
                + "        Jeśli potrzebujesz pomocy, prosimy o kontakt <br>"
                + "        <span style='font-weight: 800;'>support@coresaken.com</span>"
                + "    </div>"
                + "</body>"
                + "</html>";

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setFrom("MCList@mc-list.pl");
        helper.setSubject("MC-List.pl - Aktywacja konta użytkownika");
        helper.setText(activeAccountEmail, true);

        mailSender.send(message);
    }
    public void sendResetPasswordEmail(String to, String token) throws MessagingException {
        String resetPasswordLink = websiteAddress+"auth/resetPassword?token=" + token;

        String resetPasswordEmail = "<html>"
                + "<body style='margin: 0; padding: 0; font-size: 1.2em; background-color: #e9e9e9; font-family: system-ui, -apple-system, BlinkMacSystemFont, \'Segoe UI\', Roboto, Oxygen, Ubuntu, Cantarell, \'Open Sans\', \'Helvetica Neue\', sans-serif;'>"
                + "    <div id='header' style='background-color: #1f1f7c; width: 100%; height: 250px;'></div>"
                + "    <div id='panel' style='background-color: white; width: 700px; margin-left: auto; margin-right: auto; margin-top: -100px; border-radius: 5px;'>"
                + "        <div id='logo' style='width: 250px; padding: 25px; margin-left: auto; margin-right: auto;'>"
                + "            <img src='https://zapodaj.net/images/f86bc94eb0cbe.png' style='width: 100%; height: auto;'>"
                + "        </div>"
                + "        <div id='container' style='padding: 25px'>"
                + "            Otrzymaliśmy prośbę o zresetowanie hasła do Twojego konta na stronie <span style='font-weight: 800;'>MC-List.pl!</span><br><br><br>"
                + "            Jeśli chcesz zresetować hasło, kliknij poniższy przycisk:"
                + "            <a href='"+resetPasswordLink+"' style='text-decoration: none;'>"
                + "                <div id='button' style='background-color: #ffbe4d; padding: 10px 50px; width: fit-content; border-radius: 5px; color: white; font-weight: 800; margin-top: 20px; margin-bottom: 30px; margin-left: auto; margin-right: auto;'>"
                + "                    Zresetuj hasło"
                + "                </div>"
                + "            </a>"
                + "            Jeśli przycisk nie działa, przejdź na poniższy adres URL:<br>"
                + "            <span style='color: #ffbe4d'><a href='"+resetPasswordLink+"'>"+resetPasswordLink+"</a></span>"
                + "            <br><br><br>Jeśli nie resetowałeś hasła na naszej stronie, zignoruj tę wiadomość."
                + "        </div>"
                + "    </div>"
                + "    <div id='panel-down' style='background-color: #d1d1d1; padding: 25px 50px; width: 600px; margin-left: auto; margin-right: auto; margin-top: 50px; margin-bottom: 150px; text-align: center;'>"
                + "        Jeśli potrzebujesz pomocy, prosimy o kontakt <br>"
                + "        <span style='font-weight: 800;'>support@coresaken.com</span>"
                + "    </div>"
                + "</body>"
                + "</html>";

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setFrom("MCList@mc-list.pl");
        helper.setSubject("MC-List.pl - Resetowanie hasła");
        helper.setText(resetPasswordEmail, true);

        mailSender.send(message);
    }
}
