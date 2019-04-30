package applica.feneal.services.impl;

import applica.feneal.services.utils.CustomTemplatedMail;
import applica.framework.library.mail.MailException;
import applica.framework.library.mail.TemplatedMail;
import applica.framework.library.options.OptionsManager;

import javax.mail.MessagingException;

/**
 * Created by angelo on 27/05/16.
 */
public class MailRichiestaInfoTerritoriTask implements Runnable {

    private String content;
    private String provinceInfo;
    private OptionsManager options;
    private String recipient;
    private String mailFrom;


    public MailRichiestaInfoTerritoriTask(String content, String mailFrom, String recipient, String provinceInfo, OptionsManager options) {
        this.content = content;
        this.recipient = recipient;
        this.provinceInfo = provinceInfo;
        this.options = options;
        this.mailFrom = mailFrom;
    }

    @Override
    public void run() {

        try {
            CustomTemplatedMail tmail = new CustomTemplatedMail();

            tmail.setOptions(options);
            tmail.setMailFormat(TemplatedMail.HTML);
            tmail.setTemplatePath("templates/mail/richiesta_info_territori_mail.vm");
            tmail.setFrom(mailFrom);
            tmail.setSubject(options.get("richiestainfoliberi.mail.subject") + " " + provinceInfo);
            tmail.setTo(recipient);
            tmail.put("content", content);

            tmail.send();
        } catch (MailException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }


}
