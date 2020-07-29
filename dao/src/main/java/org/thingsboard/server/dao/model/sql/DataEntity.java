package org.thingsboard.server.dao.model.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thingsboard.server.dao.model.sql.DoubleKeys;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "data")//表名
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(value = DoubleKeys.class)
public class DataEntity implements Serializable {
    @Id
    @Column(name="cid")
    private Integer cid;
    @Id
    @Column(name="sid")
    private Integer sid;
    @Column(name = "data_payload")
    private String dataPayload;
}
