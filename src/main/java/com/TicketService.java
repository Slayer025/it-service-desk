package com;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    @Autowired private TicketRepository ticketRepository;
    @Autowired private TicketCommentRepository commentRepository;
    @Autowired private AuditLogRepository auditLogRepository;

    private String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private void logAudit(String action, Long ticketId, String details) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setTicketId(ticketId);
        log.setActor(getCurrentUser());
        log.setDetails(details);
        auditLogRepository.save(log);
    }

    public Ticket createTicket(Ticket ticket) {
        Ticket saved = ticketRepository.save(ticket);
        logAudit("CREATED", saved.getId(), "Priority: " + ticket.getPriority() + ", Category: " + ticket.getCategoryId());
        return saved;
    }

    // Non-paginated for REST API & Admin stats
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    // Paginated for Dashboard UI
    public Page<Ticket> getAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + id));
    }

    public List<Ticket> getByPriority(String priority) {
        return ticketRepository.findByPriority(priority);
    }

    public List<Ticket> getByCategory(Long categoryId) {
        return ticketRepository.findByCategoryId(categoryId);
    }

    public List<Ticket> getByStatus(String status) { 
        return ticketRepository.findByStatus(status); 
    }

    public TicketComment addComment(Long ticketId, String author, String text) {
        TicketComment comment = new TicketComment();
        comment.setTicketId(ticketId);
        comment.setAuthor(author);
        comment.setComment(text);
        TicketComment saved = commentRepository.save(comment);
        logAudit("COMMENT_ADDED", ticketId, "Comment by " + author);
        return saved;
    }

    public List<TicketComment> getComments(Long ticketId) {
        return commentRepository.findByTicketId(ticketId);
    }

    public List<AuditLog> getAuditLogs(Long ticketId) {
        return auditLogRepository.findByTicketIdOrderByCreatedAtDesc(ticketId);
    }

    public Ticket updateStatus(Long id, String newStatus) {
        Ticket ticket = getTicketById(id);
        String oldStatus = ticket.getStatus();
        ticket.setStatus(newStatus);
        Ticket saved = ticketRepository.save(ticket);
        logAudit("STATUS_UPDATED", id, oldStatus + " → " + newStatus);
        return saved;
    }

    public int escalateOverdueTickets() {
        List<Ticket> allTickets = ticketRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        int escalatedCount = 0;

        for (Ticket ticket : allTickets) {
            if ("Resolved".equals(ticket.getStatus()) || "Closed".equals(ticket.getStatus())) continue;
            
            if (ticket.getSlaDeadline() != null && ticket.getSlaDeadline().isBefore(now)) {
                String currentTeam = ticket.getAssignedTeam();
                String newTeam = "L2 Support";
                if ("L2 Support".equals(currentTeam)) newTeam = "L3 Support";

                ticket.setAssignedTeam(newTeam);
                ticketRepository.save(ticket);
                logAudit("ESCALATED", ticket.getId(), currentTeam + " → " + newTeam + " (SLA BREACHED)");
                escalatedCount++;
            }
        }
        return escalatedCount;
    }
}