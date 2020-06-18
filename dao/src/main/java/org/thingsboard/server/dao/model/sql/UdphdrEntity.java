package org.thingsboard.server.dao.model.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "udphdr")//表名
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(value = DoubleKeys.class)
public class UdphdrEntity extends DoubleKeys{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="cid")
    private Integer cid;
    @Id
    @Column(name="sid")
    private Integer sid;

    private String udp_sport;
    private String udp_dport;
    private Long udp_len;
    private Integer udp_csum;
}
