package com.project.emailnotificationservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendEmailBodyDTO {

    private String to;
    private String from;
    private String subject;
    private String body;
}
