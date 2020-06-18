package org.thingsboard.server.dao.model.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "signature")//表名
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignatureEntity {
    @Id
    @GeneratedValue
    private Integer sig_id;

    private String sig_name;
    private Integer sig_class_id;
    private Integer sig_priority;
    private Integer sig_rev;
    private Integer sig_sid;
    private Integer sig_gid;
}

