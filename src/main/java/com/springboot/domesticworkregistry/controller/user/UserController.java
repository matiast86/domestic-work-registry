package com.springboot.domesticworkregistry.controller.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.domesticworkregistry.dto.user.ChangePasswordDto;
import com.springboot.domesticworkregistry.dto.user.UpdateUserDto;
import com.springboot.domesticworkregistry.entities.User;
import com.springboot.domesticworkregistry.exceptions.EmailAlreadyExistsException;
import com.springboot.domesticworkregistry.service.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/updateEmployer")
    public String updateEmployer(@RequestParam("employerId") String id, Model model) {
        UpdateUserDto form = userService.getUserDto(id);
        model.addAttribute("employer", form);
        return "employers/employer-form";
    }

    @PostMapping("/update")
    public String update(
            @Valid @ModelAttribute("employer") UpdateUserDto form,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("employer", form);
            return "employers/employer-form";
        }

        userService.update(form, form.getUserId());
        return "redirect:/dashboard"; // ✅ fixed missing slash
    }

    @ExceptionHandler({ EmailAlreadyExistsException.class, IllegalStateException.class })
    public String handleUpdateErrors(Exception ex,
            @ModelAttribute("employer") UpdateUserDto form,
            Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("employer", form); // repopulate form with submitted data
        return "employers/employer-form";
    }

    @GetMapping("/changePassword")
    public String changePasswordRequest(Model model) {
        model.addAttribute("passwordForm", new ChangePasswordDto());
        return "auth/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @AuthenticationPrincipal User user,
            @Valid @ModelAttribute("passwordForm") ChangePasswordDto form,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("passwordForm", form);
            return "auth/change-password";
        }

        userService.changePassword(user, form);

        // Invalidate session
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        return "redirect:/loginPage?passwordChanged";
    }

}
