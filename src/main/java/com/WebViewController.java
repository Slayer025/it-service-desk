package com;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebViewController {

    @Autowired private TicketService ticketService;
    @Autowired private TicketCategoryRepository categoryRepository;

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, 
                       @RequestParam(required = false) String logout, Model model) {
        if (error != null) model.addAttribute("error", "Invalid username or password.");
        if (logout != null) model.addAttribute("message", "You have been logged out.");
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        Page<Ticket> ticketsPage = ticketService.getAllTickets(
            PageRequest.of(page, size, Sort.by("createdAt").descending())
        );
        
        model.addAttribute("tickets", ticketsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ticketsPage.getTotalPages());
        model.addAttribute("username", auth.getName());
        return "dashboard";
    }

    @GetMapping("/create-ticket")
    public String createTicketPage(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "create-ticket";
    }

    @PostMapping("/create-ticket")
    public String submitTicket(@ModelAttribute Ticket ticket, Model model) {
        try {
            ticketService.createTicket(ticket);
            return "redirect:/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", categoryRepository.findAll());
            return "create-ticket";
        }
    }

    @GetMapping("/ticket/{id}")
    public String ticketDetail(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.getTicketById(id);
        List<TicketComment> comments = ticketService.getComments(id);
        List<AuditLog> auditLogs = ticketService.getAuditLogs(id);
        
        model.addAttribute("ticket", ticket);
        model.addAttribute("comments", comments);
        model.addAttribute("auditLogs", auditLogs);
        return "ticket-detail";
    }

    @GetMapping("/admin")
    public String adminPanel(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("totalTickets", ticketService.getAllTickets(PageRequest.of(0, 1)).getTotalElements());
        model.addAttribute("openTickets", ticketService.getByStatus("Open").size());
        return "admin";
    }
}