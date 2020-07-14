package org.thingsboard.server.controller.idscontroller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransportDataEntity {
    String entityType;
    String entityId;
//    List<Map<String,String>> keys;
}
