package br.com.cashcontroller.config;

import br.com.cashcontroller.model.Role;
import br.com.cashcontroller.model.User;
import br.com.cashcontroller.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Component
@ConfigurationProperties(prefix = "app.admin")
@EnableConfigurationProperties
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    private String username;
    private String password;
    private boolean seedEnabled;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
//        if (!seedEnabled) {
//            System.out.println("Admin user seeding is disabled");
//            return;
//        }
//
//        // Check if admin user already exists
//        if (userRepository.findByUsername(username).isEmpty()) {
//            User adminUser = new User();
//            adminUser.setUsername(username);
//            adminUser.setPassword(passwordEncoder.encode(password));
//            adminUser.setRole(Role.ROLE_ADMIN);
//
//            userRepository.save(adminUser);
//            System.out.println("Admin user created successfully!");
//        } else {
//            System.out.println("Admin user already exists");
//        }
    }

    // Getters and setters for configuration properties
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSeedEnabled(boolean seedEnabled) {
        this.seedEnabled = seedEnabled;
    }
}
