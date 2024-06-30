package com.lenarsharipov.listener;

import com.lenarsharipov.entity.Audit;
import org.hibernate.event.spi.*;

import java.io.Serializable;

public class AuditTableListener implements PreDeleteEventListener, PreInsertEventListener {

    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        auditEntity(event, Audit.Operation.DELETE);
        return false;
    }

    private static void auditEntity(AbstractPreDatabaseOperationEvent event,
                                    Audit.Operation operation) {
        if (event.getEntity().getClass() != Audit.class) {
            var audit = Audit.builder()
                    .entityId((Serializable) event.getId())
                    .entityName(event.getPersister().getEntityName())
                    .entityContent(event.getEntity().toString())
                    .operation(operation)
                    .build();
            event.getSession().persist(audit);
        }
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        auditEntity(event, Audit.Operation.INSERT);
        return false;
    }
}
