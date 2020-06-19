package org.thingsboard.server.dao.model.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
//@Table(name = "acidEvent")//表名
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(value = DoubleKeys.class)
public class AcideventEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="cid")
    private Integer cid;
    @Id
    @Column(name="sid")
    private Integer sid;


    private Integer signature;
    private String  sigName; //

    private Integer sigClassId;

    private Integer sigPriority; //
    private Date timestamp;
    //	@Column(columnDefinition = "int(20) not null")
    private Long ipSrc;

    private Long ipDst;

    private Integer ipProto;
    private String  layer4Sport; //
    private String  layer4Dport; //
}
