package com.example.Job.service;

public interface IEmailService {
    public void sendSimpleEmail();

    public void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml);

    public void sendEmailFromTemplateSync(String to, String subject, String templateName);
}
