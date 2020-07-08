package org.thingsboard.server.dao.model.sql;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.TypeDef;
import org.thingsboard.server.dao.util.mapping.JsonStringType;

import javax.persistence.*;
import java.io.Serializable;

@Data
@EqualsAndHashCode()
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name ="icmphdr")
@IdClass(value = DoubleKeys.class)
public class IcmphdrEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="cid")
    private Integer cid;
    @Id
    @Column(name="sid")
    private Integer sid;
    private int icmpType;
    private int icmpCode;
    private int icmpCsum;
    private int icmpId;
    private int icmpSeq;

}
