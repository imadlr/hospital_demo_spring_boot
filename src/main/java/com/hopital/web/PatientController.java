package com.hopital.web;

import com.hopital.entities.Patient;
import com.hopital.repositories.PatientRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
public class PatientController {
    private PatientRepository patientRepository;

    @GetMapping("/")
    //@PreAuthorize("hasRole('user')")
    public String home() {
        return "redirect:/user/index";
    }

    @GetMapping("/user/index")
    //@PreAuthorize("hasRole('user')")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size,
                        @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<Patient> pagePatients = patientRepository.findByNomContains(keyword, PageRequest.of(page, size));
        model.addAttribute("pages", new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("patients", pagePatients.getContent());
        return "patients";
    }

    @GetMapping("/admin/delete")
    //@PreAuthorize("hasRole('admin')")
    public String delete(Long id, String keyword, int page) {
        patientRepository.deleteById(id);
        return "redirect:/user/index?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/admin/formPatient")
    //@PreAuthorize("hasRole('admin')")
    public String formPatient(Model model) {
        model.addAttribute("patient", new Patient());
        return "formPatient";
    }

    @PostMapping("/admin/savePatient")
    //@PreAuthorize("hasRole('admin')")
    public String savePatient(@Valid Patient patient, BindingResult bindingResult) { // BindingResult : pour stocker les messages d'erreurs
        if (bindingResult.hasErrors()) {
            return "formPatient";
        }
        patientRepository.save(patient);
        return "redirect:/user/index?keyword="+patient.getNom();
    }

    @GetMapping("/admin/editPatient")
    //@PreAuthorize("hasRole('admin')")
    public String editPatient(Model model, @RequestParam(name = "id") Long id) {
        Patient patient = patientRepository.findById(id).get();
        model.addAttribute("patient", patient);
        return "editPatient";
    }
}
