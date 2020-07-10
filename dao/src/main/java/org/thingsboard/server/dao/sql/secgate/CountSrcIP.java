package org.thingsboard.server.dao.sql.secgate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountSrcIP {
   private long count;
   private long ipSrc;
}
