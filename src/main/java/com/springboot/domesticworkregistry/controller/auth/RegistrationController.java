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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.domesticworkregistry.dto.user.ChangePasswordDto;
import com.springboot.domesticworkregistry.dto.user.RegisterUserDto;
import com.springboot.domesticworkregistry.dto.user.ResetPasswordDto;
import com.springboot.domesticworkregistry.dto.user.ResetPasswordEmailDto;
import com.springboot.domesticworkregistry.exceptions.EmailAlreadyExistsException;
import com.springboot.domesticworkregistry.service.user.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/registrationForm")
    public String registrationForm(Model theModel) {
        theModel.addAttribute("registerEmployerDto", new RegisterUserDto());
        return "auth/registration-form";
    }

    @PostMapping("/processRegistrationForm")
    public String processRegistrationForm(
            @Valid @ModelAttribute("registerEmployerDto") RegisterUserDto registerUserDto,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model theModel) {

        if (bindingResult.hasErrors()) {
            return "auth/registration-form";
        }

        if (!registerUserDto.getPassword().equals(registerUserDto.getRepeatPassword())) {
            bindingResult.rejectValue("repeatPassword", "error.repeatPassword", "Passwords must match.");
            return "auth/registration-form";
        }

        try {
            userService.registerEmployer(registerUserDto);
            System.out.println("Employer registration successful");
        } catch (EmailAlreadyExistsException e) {
            // Save the DTO in session so the exception handler can access it
            session.setAttribute("registerEmployerDto", registerUserDto);
            throw e;
        }

        redirectAttributes.addFlashAttribute("registered", true);
        return "redirect:/loginPage";

    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public String handleEmailExists(EmailAlreadyExistsException ex, Model model, HttpSession session) {
        model.addAttribute("errorMessage", ex.getMessage());

        RegisterUserDto dto = (RegisterUserDto) session.getAttribute("registerEmployerDto");
        if (dto != null) {
            model.addAttribute("registerEmployerDto", dto);
            session.removeAttribute("registerEmployerDto"); // clear after use
        } else {
            model.addAttribute("registerEmployerDto", new RegisterUserDto());
        }

        return "auth/registration-form";
    }

    @GetMapping("reset-password-request")
    public String resetPasswordRequest(Model model,
            RedirectAttributes redirectAttributes) {

        model.addAttribute("email", new ResetPasswordEmailDto());
        return "auth/reset-password-request";

    }

    @PostMapping("send-request-email")
    public String sendRequestEmail(@Valid @ModelAttribute("email") ResetPasswordEmailDto email,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("email", email);
            return "auth/reset-password-request";
        }
        model.addAttribute("email", email);
        userService.resetPasswordConfirmation(email.getEmail());
        redirectAttributes.addFlashAttribute("successMessage", "Solicitud creada con éxito. Revise su email");

        return "redirect:/loginPage";

    }

    @GetMapping("/reset-password")
    public String passwordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("passwordForm", new ChangePasswordDto());
        model.addAttribute("token", token);
        return "auth/reset-password"; // ✅ make sure your template is in `templates/auth/change-password.html`
    }

    @PostMapping("/reset-password-confirmation")
    public String resetPasswordConfirmation(@RequestParam("token") String token,
            @Valid @ModelAttribute("form") ResetPasswordDto form,
            Model model, RedirectAttributes redirectAttributes) {

        model.addAttribute("token", token);
        model.addAttribute("form", form);
        userService.resetPassword(token, form);
        redirectAttributes.addFlashAttribute("successMessage", "Contraseña guardada con éxito");
        return "redirect:/loginPage";

    }

}
