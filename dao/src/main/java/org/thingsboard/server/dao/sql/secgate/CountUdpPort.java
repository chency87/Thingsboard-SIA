package org.thingsboard.server.dao.sql.secgate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountUdpPort {
   private long count;
   private Integer udp_dport;
}
