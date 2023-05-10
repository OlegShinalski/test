package helmes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import helmes.persistence.repository.SectorRepository;

@SpringBootApplication
public class Application {

    @Autowired
    private SectorRepository sectorRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
