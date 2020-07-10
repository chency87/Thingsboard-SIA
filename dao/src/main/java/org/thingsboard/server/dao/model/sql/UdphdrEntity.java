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

    @Column(name="udp_sport")
    private Integer udpSport;

    @Column(name="udp_dport")
    private Integer udpDport;

    @Column(name="udp_len")
    private Long udpLen;

    @Column(name="udp_csum")
    private int udpCsum;
}
