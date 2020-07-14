package org.thingsboard.server.dao.model.sql;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.TypeDef;
import org.thingsboard.server.dao.util.mapping.JsonStringType;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode()
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name ="event")
@IdClass(value = DoubleKeys.class)
public class EventSnortEntity {

    @Id
    @Column(name="cid")
    private Integer cid;
    @Id
    @Column(name="sid")
    private Integer sid;

    @Column(name = "signature")
    private Integer signature;

    @Column(name = "timestamp")
    private Date timestamp;
}
