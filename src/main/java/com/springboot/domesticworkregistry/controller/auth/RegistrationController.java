package com.springboot.domesticworkregistry.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.domesticworkregistry.dto.employer.RegisterEmployerDto;
import com.springboot.domesticworkregistry.exceptions.EmailAlreadyExistsException;
import com.springboot.domesticworkregistry.service.employer.EmployerService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final EmployerService employerService;

    @Autowired
    public RegistrationController(EmployerService employerService) {
        this.employerService = employerService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/registrationForm")
    public String registrationForm(Model theModel) {
        theModel.addAttribute("registerEmployerDto", new RegisterEmployerDto());
        return "registration-form";
    }

    @PostMapping("/processRegistrationForm")
    public String processRegistrationForm(
            @Valid @ModelAttribute("registerEmployerDto") RegisterEmployerDto registerEmployerDto,
            BindingResult bindingResult,
            HttpSession session,
            Model theModel) {

        if (bindingResult.hasErrors()) {
            return "registration-form";
        }

        if (!registerEmployerDto.getPassword().equals(registerEmployerDto.getRepeatPassword())) {
            bindingResult.rejectValue("repeatPassword", "error.repeatPassword", "Passwords must match.");
            return "registration-form";
        }

        try {
            employerService.save(registerEmployerDto);
        } catch (EmailAlreadyExistsException e) {
            // Save the DTO in session so the exception handler can access it
            session.setAttribute("registerEmployerDto", registerEmployerDto);
            throw e;
        }

        return "redirect:/login?registered";

    }

    @GetMapping("/registrationConfirmation")
    public String showConfirmationPage() {
        return "registration-confirmation";
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public String handleEmailExists(EmailAlreadyExistsException ex, Model model, HttpSession session) {
        model.addAttribute("errorMessage", ex.getMessage());

        RegisterEmployerDto dto = (RegisterEmployerDto) session.getAttribute("registerEmployerDto");
        if (dto != null) {
            model.addAttribute("registerEmployerDto", dto);
            session.removeAttribute("registerEmployerDto"); // clear after use
        } else {
            model.addAttribute("registerEmployerDto", new RegisterEmployerDto());
        }

        return "registration-form";
    }

}
