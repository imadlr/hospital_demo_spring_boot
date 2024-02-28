package com.hopital;

import com.hopital.entities.Patient;
import com.hopital.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class HopitalApplication implements CommandLineRunner {

    @Autowired
    private PatientRepository patientRepository;

    public static void main(String[] args) {
        SpringApplication.run(HopitalApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Trois méthode pour créer un objet de type objet
        /*Patient patient = new Patient();
        patient.setNom("Fahim");
        patient.setDateNaissance(new Date());
        patient.setScore(33);
        patient.setMalade(false);

        Patient patient1 = new Patient(null, "Flan", new Date(), true, 10);

        Patient patient2 = Patient.builder()
                .nom("Hanane")
                .dateNaissance(new Date())
                .score(21)
                .malade(false)
                .build();*/

       // patientRepository.save(new Patient(null, "Flan", new Date(), true, 110));
       // patientRepository.save(new Patient(null, "Fahim", new Date(), false, 115));
       // patientRepository.save(new Patient(null, "Fatiha", new Date(), true, 210));
       // patientRepository.save(new Patient(null, "Amine", new Date(), false, 215));

    }

}