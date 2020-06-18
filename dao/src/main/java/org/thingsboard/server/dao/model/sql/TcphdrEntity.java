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

    private String tcp_sport;
    private String tcp_dport;
    private Long tcp_seq;
    private Long tcp_ack;
    private Integer tcp_off;
    private Integer tcp_res;
    private Integer tcp_flags;
    private Integer tcp_win;
    private Integer tcp_csum;
    private Integer tcp_urp;
}
