package org.thingsboard.server.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thingsboard.server.common.data.SearchTextBased;
import org.thingsboard.server.common.data.id.UUIDBased;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Acidevent{
    private int cid;
    private int sid;
    private int signature;
    private Date timestamp;
    private long ipDst;
    private int ipProto;
    private long ipSrc;
    private int sigClassId;
    private String  sigName; //

    private long sigPriority; //

    //	@Column(columnDefinition = "int(20) not null")
    private int TDport;
    private int TSport;
    private int UDport;
    private int USport;




//    private int  layer4Sport; //
//    private int  layer4Dport; //
}
