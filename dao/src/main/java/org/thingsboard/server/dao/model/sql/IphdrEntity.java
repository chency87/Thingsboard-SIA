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
    private String ipSrc;

    @Column(name = "ip_dst")
    private String ipDst;

    @Column(name = "ip_ver")
    private Integer ipVer;

    @Column(name = "ip_hlen")
    private Integer ipHlen;

    @Column(name = "ip_tos")
    private Integer ipTos;

    @Column(name = "ip_len")
    private Integer ipLen;

    @Column(name = "ip_id")
    private Integer ipId;

    @Column(name = "ip_flags")
    private Integer ipFlags;

    @Column(name = "ip_off")
    private Integer ipOff;

    @Column(name = "ip_ttl")
    private Integer ipTtl;

    @Column(name = "ip_proto")
    private Integer ipProto;

    @Column(name = "ip_csum")
    private Integer ipCsum;


}
