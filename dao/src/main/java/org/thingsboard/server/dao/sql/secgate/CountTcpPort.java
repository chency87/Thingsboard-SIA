package org.thingsboard.server.dao.sql.secgate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountTcpPort {
   private long count;
   private Integer tcp_dport;
}
