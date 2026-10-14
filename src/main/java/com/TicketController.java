package com;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired private TicketService ticketService;

    @PostMapping
    public ResponseEntity<?> createTicket(@Valid @RequestBody Ticket ticket) {
        return ResponseEntity.ok(ticketService.createTicket(ticket));
    }

    @GetMapping
    public List<Ticket> getAllTickets() { 
        return ticketService.getAllTickets(); 
    }

    @GetMapping("/filter/priority")
    public List<Ticket> getByPriority(@RequestParam String priority) { 
        return ticketService.getByPriority(priority); 
    }

    @GetMapping("/filter/category")
    public List<Ticket> getByCategory(@RequestParam Long categoryId) { 
        return ticketService.getByCategory(categoryId); 
    }

    @PostMapping("/{id}/comments")
    public TicketComment addComment(@PathVariable Long id, @RequestBody TicketComment comment) {
        return ticketService.addComment(id, comment.getAuthor(), comment.getComment());
    }

    @GetMapping("/{id}/comments")
    public List<TicketComment> getComments(@PathVariable Long id) { 
        return ticketService.getComments(id); 
    }

    @PutMapping("/{id}/status")
    public Ticket updateStatus(@PathVariable Long id, @RequestParam String status) { 
        return ticketService.updateStatus(id, status); 
    }

    @PostMapping("/escalate")
    public String escalateOverdueTickets() {
        int count = ticketService.escalateOverdueTickets();
        return "Escalated " + count + " ticket(s).";
    }
}