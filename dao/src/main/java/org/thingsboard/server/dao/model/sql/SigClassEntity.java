package org.thingsboard.server.dao.model.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "sig_class")//表名
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(value = DoubleKeys.class)
public class SigClassEntity {
    @Id
    @Column(name="cid")
    private Integer cid;
    @Id
    @Column(name="sid")
    private Integer sid;

    private String sig_class_name;
}
