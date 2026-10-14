package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Only seed if users don't exist yet
        if (userRepository.count() == 0) {
            String hashedPassword = passwordEncoder.encode("password123");
            
            User endUser = new User();
            endUser.setUsername("enduser");
            endUser.setPassword(hashedPassword);
            endUser.setRole("USER");
            userRepository.save(endUser);

            User agent = new User();
            agent.setUsername("agent");
            agent.setPassword(hashedPassword);
            agent.setRole("AGENT");
            userRepository.save(agent);

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(hashedPassword);
            admin.setRole("ADMIN");
            userRepository.save(admin);

            System.out.println("✅ SEEDER: Created 3 test users with fresh BCrypt hashes!");
        }
    }
}