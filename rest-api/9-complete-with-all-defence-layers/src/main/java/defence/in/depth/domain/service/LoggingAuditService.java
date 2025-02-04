package defence.in.depth.domain.service;

import defence.in.depth.domain.model.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingAuditService implements AuditService {
    private final Logger logger = LoggerFactory.getLogger(LoggingAuditService.class);

    private final PermissionService permissionService;

    public LoggingAuditService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public void log(DomainEvent event, Object payload) {
        String source = permissionService.getUserName();
        logger.info("{} occurred by {}. Extra data: {}", event, source, payload);
    }
}
