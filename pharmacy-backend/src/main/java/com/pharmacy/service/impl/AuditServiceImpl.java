package com.pharmacy.service.impl;

import com.pharmacy.entity.AuditLog;
import com.pharmacy.repository.AuditLogRepository;
import com.pharmacy.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void log(
            String action,
            String entityType,
            Long entityId,
            String details
    ) {

        String performedBy = "SYSTEM";

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            performedBy = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();
        }

        AuditLog log = AuditLog.builder()
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .performedBy(performedBy)
                .details(details)
                .build();

        auditLogRepository.save(log);
    }
}