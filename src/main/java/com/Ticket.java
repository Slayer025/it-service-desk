package com;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Priority is required")
    private String priority;
    
    private String status;

    @Column(name = "assigned_team")
    private String assignedTeam;

    @Column(name = "sla_deadline")
    private LocalDateTime slaDeadline;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "category_id")
    private Long categoryId;

    public Ticket() {
        this.createdAt = LocalDateTime.now();
        this.status = "Open";
        this.assignedTeam = "L1 Support";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { 
        this.priority = priority;
        calculateSlaDeadline();
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAssignedTeam() { return assignedTeam; }
    public void setAssignedTeam(String assignedTeam) { this.assignedTeam = assignedTeam; }

    public LocalDateTime getSlaDeadline() { return slaDeadline; }
    public void setSlaDeadline(LocalDateTime slaDeadline) { this.slaDeadline = slaDeadline; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    private void calculateSlaDeadline() {
        LocalDateTime now = LocalDateTime.now();
        if (this.priority == null) return;
        
        switch (this.priority.toLowerCase()) {
            case "critical": this.slaDeadline = now.plusHours(2); break;
            case "high":     this.slaDeadline = now.plusHours(4); break;
            case "medium":   this.slaDeadline = now.plusHours(24); break;
            case "low":      this.slaDeadline = now.plusHours(72); break;
            default:         this.slaDeadline = now.plusHours(24);
        }
    }
}