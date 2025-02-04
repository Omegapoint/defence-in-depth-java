package defence.in.depth.domain.service;

import defence.in.depth.domain.model.DomainEvent;

public interface AuditService {
    void log(DomainEvent event, Object payload);
}
