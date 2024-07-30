package com.gruptiga.smartlaundry.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class ServiceCustomId implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        String prefix = "S";
        String query = "SELECT COALESCE(MAX(CAST(SUBSTRING(service_id, 2) AS INTEGER)), 0) FROM m_service";
        Integer max = (Integer) session.createNativeQuery(query).getSingleResult();
        int nextId = (max == null ? 1 : max + 1);
        return prefix + String.format("%03d", nextId);
    }
}
