package org.thingsboard.server.dao.model.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "tcphdr")//表名
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(value = DoubleKeys.class)
public class TcphdrEntity extends DoubleKeys{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="cid")
    private Integer cid;
    @Id
    @Column(name="sid")
    private Integer sid;

    @Column(name="tcp_sport")
    private Integer tcpSport;

    @Column(name="tcp_dport")
    private Integer tcpDport;

    @Column(name="tcp_seq")
    private Long tcpSeq;

    @Column(name="tcp_ack")
    private Long tcpAck;

    @Column(name="tcp_off")
    private Integer tcpOff;

    @Column(name="tcp_res")
    private Integer tcpRes;

    @Column(name="tcp_flags")
    private Integer tcpFlags;

    @Column(name="tcp_win")
    private Integer tcpWin;

    @Column(name="tcp_csum")
    private Integer tcpCsum;

    @Column(name="tcp_urp")
    private Integer tcpUrp;
}
