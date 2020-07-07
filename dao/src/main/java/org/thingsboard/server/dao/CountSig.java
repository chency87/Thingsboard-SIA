package org.thingsboard.server.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountSig {
    long count;
    Integer signature;
}
