package org.thingsboard.server.dao.model.sql;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.TypeDef;
import org.thingsboard.server.dao.model.ModelConstants;
import org.thingsboard.server.dao.util.mapping.JsonStringType;

import javax.persistence.*;

@Data
@EqualsAndHashCode()
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name ="iphdr")
@IdClass(value = DoubleKeys.class)
public class IphdrEntity {

    @Id
    @Column(name="cid")
    private Integer cid;
    @Id
    @Column(name="sid")
    private Integer sid;

    @Column(name = "ip_src")
    private long ipSrc;

    @Column(name = "ip_dst")
    private long ipDst;

    @Column(name = "ip_ver")
    private int ipVer;

    @Column(name = "ip_hlen")
    private int ipHlen;

    @Column(name = "ip_tos")
    private int ipTos;

    @Column(name = "ip_len")
    private int ipLen;

    @Column(name = "ip_id")
    private int ipId;

    @Column(name = "ip_flags")
    private int ipFlags;

    @Column(name = "ip_off")
    private int ipOff;

    @Column(name = "ip_ttl")
    private int ipTtl;

    @Column(name = "ip_proto")
    private int ipProto;

    @Column(name = "ip_csum")
    private int ipCsum;


}
