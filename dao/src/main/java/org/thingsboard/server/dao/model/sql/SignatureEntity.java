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
    @Column(name="sig_id")
    private Integer sig_id;
    @Column(name="sig_name")
    private String sig_name;
    @Column(name="sig_class_id")
    private Integer sig_class_id;
    @Column(name="sig_priority")
    private Integer sig_priority;
    @Column(name="sig_rev")
    private Integer sig_rev;
    @Column(name="sig_sid")
    private Integer sig_sid;
    @Column(name="sig_gid")
    private Integer sig_gid;
}

