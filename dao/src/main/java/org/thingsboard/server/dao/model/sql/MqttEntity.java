package org.thingsboard.server.dao.model.sql;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.TypeDef;
import org.thingsboard.server.dao.util.mapping.JsonStringType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@EqualsAndHashCode()
@Entity
@AllArgsConstructor
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name ="mqtt")
public class MqttEntity {

    @Id
    @Column(name="id")
    private String id;

    @Column(name="address")
    private String address;

    @Column(name="topic")
    private String topic;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="tenant_id")
    private String tenantId;
}
