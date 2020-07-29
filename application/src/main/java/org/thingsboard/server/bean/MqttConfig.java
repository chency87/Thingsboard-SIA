package org.thingsboard.server.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttConfig {

    private int id;

    private int address;

    private int topic;

    private int username;

    private int password;

    private int tenantId;
}
