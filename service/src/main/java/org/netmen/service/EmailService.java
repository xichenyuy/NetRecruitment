package org.netmen.service;

public interface EmailService {
    boolean send(String to, String title, String content);
}
