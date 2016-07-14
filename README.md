Sunrise Java Email :sunrise: :email:
==================

[![Build Status](https://travis-ci.org/commercetools/commercetools-sunrise-java-email.png?branch=master)](https://travis-ci.org/commercetools/commercetools-sunrise-java-email) [![Stories in Ready](https://badge.waffle.io/commercetools/commercetools-sunrise-java-email.png?label=ready&title=Ready)](https://waffle.io/commercetools/commercetools-sunrise-java-email)

Module for [Sunrise Java](https://github.com/sphereio/commercetools-sunrise-java) with different supported services to send e-mail. 

* [Javadoc](https://commercetools.github.io/commercetools-sunrise-java-email/javadoc/index.html)


# Examples

In all of the following examples, debug output from the underlying [Java Mail API](https://java.net/projects/javamail/pages/Home)
might be obtained by setting the `mail.debug` system property to `true`. Either programmatically via
`System.setProperty("mail.debug", ""+true);` or by specifying the option `-Dmail.debug=true` at JVM startup.

## Creating a service instance

There are two service implementations: `SmtpAuthEmailSender` and `GmailSmtpEmailSender`. While the former is used
to connect to arbitrary SMTP servers, the latter is partly preconfigured to connect to Gmail.

Below example shows how to connect to a server using STARTTLS to secure the SMTP connection. Have a look at the
[JavaDoc of SmtpAuthEmailSender](https://commercetools.github.io/commercetools-sunrise-java-email/javadoc/index.html?io/commercetools/sunrise/email/smtp/SmtpAuthEmailSender.html)
for a description of the configuration options.

    import io.commercetools.sunrise.email.smtp.SmtpAuthEmailSender;
    import java.util.concurrent.*;

    [...]
    System.setProperty("mail.debug", ""+true);

    final String host = YOUR_CHOICE;
    final int port = YOUR_CHOICE;
    final String username = YOUR_CHOICE;
    final String password = YOUR_CHOICE;
    final ForkJoinPool executor = new ForkJoinPool(5); // 5 pool threads
    final int timeoutMs = 10*1000; // 10 seconds
    final SmtpAuthEmailSender sender = new SmtpAuthEmailSender(executor, host, port,
                                            SmtpAuthEmailSender.TransportSecurity.STARTTLS,
                                            username, password, timeoutMs);

The creation of a `GmailSmtpEmailSender` is slightly shorter.

    import io.commercetools.sunrise.email.smtp.SmtpAuthEmailSender;
    import java.util.concurrent.*;

    [...]
    System.setProperty("mail.debug", ""+true);

    final String gmailEmailAddress = YOUR_CHOICE;
    final String password = YOUR_CHOICE;
    final ForkJoinPool executor = new ForkJoinPool(5); // 5 pool threads
    final int timeoutMs = 10*1000; // 10 seconds
    final GmailSmtpEmailSender sender = new GmailSmtpEmailSender(executor, gmailEmailAddress, password, timeoutMs);

## Sending a plain-text e-mail

Both `SmtpAuthEmailSender` and `GmailSmtpEmailSender` implement the `EmailSender` functional interface that provides
a single method for sending e-mail.

To send an e-mail, pass a `MessageEditor` to the e-mail sender. `MessageEditor` is a functional interface so you
may use a lambda expression. The `MessageEditor` is invoked before the `send` method returns and is passed a
`MimeMessage` created by the e-mail sender. Sending happens asynchronously, which is why the `send` method returns a
`CompletionStage`.

    final CompletionStage<String> completionStage = sender.send(msg -> {
        msg.setFrom("me@domain.com");
        msg.setRecipients(Message.RecipientType.TO, "you@domain.com");
        msg.setSubject("The new mail service", "UTF-8");
        msg.setText("Hi,\n" +
            "\n" +
            "have you seen the new mail service?!\n" +
            "\n" +
            "Sebastian", "UTF-8");
    });

In case you want to obtain the MessageID string of the e-mail that was sent, it can be obtained from the returned
`CompletionStage` like in the following (or using any other means of combining `CompletionStage`s offered by the
methods declared by the `CompletionStage` interface).

    final String messageID = completionStage.toCompletableFuture().join();

## Including an attachment

It is also possible to create multi-part messages and messages with attachments. The attachment data can be loaded
from a URL (using a `URLDataSource`) or from a file (using a `FileDataSource`).

    final BodyPart textPart = new MimeBodyPart();
    textPart.setText("Have a look at the attachment!");

    final BodyPart attachmentPart = new MimeBodyPart();
    final String attachmentFileName = "sampleAttachment.txt";
    final URL attachmentURL = getClass().getClassLoader().getResource(attachmentFileName);

    if(attachmentURL == null)
        throw new IllegalStateException("Attachment could not be found: "+attachmentFileName;

    attachmentPart.setDataHandler(new DataHandler(new URLDataSource(attachmentURL)));
    attachmentPart.setFileName(attachmentFileName);
    final MimeMultipart content = new MimeMultipart(textPart, attachmentPart);

    sender.send(msg -> {
        msg.setFrom("me@domain.com");
        msg.addRecipients(Message.RecipientType.TO, "you@domain.com");
        msg.setSubject("Test with attachment", "UTF-8");
        msg.setContent(content);
    });