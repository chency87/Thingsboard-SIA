package org.thingsboard.server.dao.model.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sensor")//表名
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorEntity {
    @Id
    @GeneratedValue
    Integer sid;

    private String hostname;
    private String Interface;
    private String filter;
    private Integer detail;
    private Integer encoding;
    private Integer last_cid;
}
