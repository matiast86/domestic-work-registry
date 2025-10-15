package com.springboot.domesticworkregistry.controller.payslip;

import java.util.List;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.domesticworkregistry.dto.payslip.CreatePayslipDto;
import com.springboot.domesticworkregistry.dto.payslip.PayslipDetailsDto;
import com.springboot.domesticworkregistry.entities.Payslip;
import com.springboot.domesticworkregistry.service.payslip.PayslipService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/payslips")
public class PayslipController {

    private final PayslipService payslipService;

    public PayslipController(PayslipService payslipService) {
        this.payslipService = payslipService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/create/{contractId}")
    public String payslipForm(@PathVariable("contractId") int contractId, @RequestParam("year") int year,
            @RequestParam("month") int month,
            Model model) {
        CreatePayslipDto form = payslipService.fillForm(contractId, year, month);
        model.addAttribute("form", form);
        model.addAttribute("contractId", contractId);

        return "payslips/payslip-form";
    }

    @GetMapping("/list/{contractId}")
    public String getPayslips(@PathVariable("contractId") int contractId, Model model) {
        List<Payslip> payslips = payslipService.findAllByContractId(contractId);
        model.addAttribute("payslips", payslips);
        return "payslips/list-payslips";
    }

    @GetMapping("/details/{id}")
    public String getDetails(@PathVariable("id") int id, Model model) {
        PayslipDetailsDto payslip = payslipService.getDetails(id);
        model.addAttribute("payslip", payslip);
        return "payslips/payslip-details";
    }

    @PostMapping("/preview/{contractId}")
    public String previewPayslip(@PathVariable("contractId") int contractId,
            @Valid @ModelAttribute("form") CreatePayslipDto form, BindingResult bindingResult, Model model) {

        Payslip payslip = payslipService.buildPayslip(contractId, form);
        if (bindingResult.hasErrors()) {
            model.addAttribute("payslip", payslip);
            model.addAttribute("contractId", contractId);
            model.addAttribute("form", form);
            return "payslips/payslip-preview";
        }

        model.addAttribute("payslip", payslip);
        model.addAttribute("contractId", contractId);
        model.addAttribute("form", form);
        return "payslips/payslip-preview";
    }

    @PostMapping("/save/{contractId}")
    public String savePayslip(@PathVariable("contractId") int contractId,
            @ModelAttribute("form") CreatePayslipDto form, RedirectAttributes redirectAttributes) {
        Payslip saved = payslipService.save(contractId, form);
        redirectAttributes.addFlashAttribute("successMessage", "Recibo generado correctamente");

        return "redirect:/payslips/details/" + saved.getId();
    }

    @GetMapping("/pdf/{id}")
    public void downloadPayslipPdf(@PathVariable("id") int id, HttpServletResponse response) {
        // Generate PDF from PayslipDetailsDto
    }

    @GetMapping("/select/{contractId}")
    public String selectPeriod(@PathVariable("contractId") int contractId, Model model) {
        model.addAttribute("contractId", contractId);

        return "payslips/select-period";
    }

}
