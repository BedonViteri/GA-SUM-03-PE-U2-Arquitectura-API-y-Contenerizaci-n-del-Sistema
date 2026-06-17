package ec.edu.uteq.sga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SgaPrincipalApplication {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("HASH: " + encoder.encode("Admin1234"));
        SpringApplication.run(SgaPrincipalApplication.class, args);
    }
}