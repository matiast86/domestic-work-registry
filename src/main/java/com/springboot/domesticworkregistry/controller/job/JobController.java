package com.springboot.domesticworkregistry.controller.job;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.domesticworkregistry.dto.job.CreateJobDto;
import com.springboot.domesticworkregistry.dto.job.JobsMonthlyReportDto;
import com.springboot.domesticworkregistry.dto.job.JobsReportDto;
import com.springboot.domesticworkregistry.dto.job.groups.ExtraJobValidation;
import com.springboot.domesticworkregistry.dto.job.groups.RegularJobValidation;
import com.springboot.domesticworkregistry.entities.Job;
import com.springboot.domesticworkregistry.service.job.JobService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/jobs")
public class JobController {

    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/add/{id}")
    public String jobForm(@PathVariable("id") int contractId, Model model) {
        CreateJobDto jobForm = new CreateJobDto();
        model.addAttribute("jobForm", jobForm);
        model.addAttribute("contractId", contractId); // pass contractId to the form
        return "jobs/job-form";
    }

    @PostMapping("/create/{id}")
    public String createJob(
            @PathVariable("id") int contractId,
            @Validated(RegularJobValidation.class) @ModelAttribute("jobForm") CreateJobDto form,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("contractId", contractId);
            return "jobs/job-form";
        }

        jobService.save(form, contractId);

        // Redirect to avoid resubmission
        return "redirect:/jobs/jobList/" + contractId;
    }

    @GetMapping("/add-extra-hours/{id}")
    public String extraHoursForm(@PathVariable("id") int contractId, Model model) {
        CreateJobDto jobForm = new CreateJobDto();
        model.addAttribute("jobForm", jobForm);
        model.addAttribute("contractId", contractId);

        return "jobs/extra-hours-form";
    }

    @PostMapping("/extra-hours/{id}")
    public String createExtraHours(@PathVariable("id") int contractId,
            @Validated(ExtraJobValidation.class) @ModelAttribute("jobForm") CreateJobDto form,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("contractId", contractId);
            return "jobs/extra-hours-form";
        }
        form.setExtraHours(true);
        jobService.save(form, contractId);

        return "redirect:/jobs/jobList/" + contractId;
    }

    @GetMapping("/jobList/{id}")
    public String getJobsList(@PathVariable("id") int contractId, Model model) {
        JobsReportDto jobs = this.jobService.getJobsByContracDto(contractId);
        model.addAttribute("jobs", jobs);
        return "jobs/job-table";
    }

    @GetMapping("/monthlyDetails/{id}")
    public String showMonthlyDetails(@PathVariable("id") int contractId, @RequestParam("year") int year,
            @RequestParam("month") int month, Model model) {
        JobsMonthlyReportDto jobs = this.jobService.getMonthlyJobsByContract(contractId, year, month);
        model.addAttribute("jobs", jobs);
        model.addAttribute("contractId", contractId);

        return "jobs/monthly-table";
    }

    @GetMapping("/updateForm/{id}")
    public String updateForm(@PathVariable("id") int jobId, Model model) {
        CreateJobDto dto = jobService.getJobDto(jobId);
        model.addAttribute("jobForm", dto);
        model.addAttribute("jobId", jobId);

        return "jobs/job-form";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("jobForm") CreateJobDto form,
            BindingResult bindingResult,
            Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("jobForm", form);
            return "jobs/job-form";
        }

        model.addAttribute("jobForm", form);

        Job updatedJob = jobService.update(form);

        redirectAttributes.addFlashAttribute("successMessage", "Tarea actualizada con éxito");

        // build redirect with contractId, year, month
        int contractId = updatedJob.getContract().getId();
        int year = updatedJob.getDate().getYear();
        int month = updatedJob.getDate().getMonthValue();

        return "redirect:/jobs/monthlyDetails/" + contractId +
                "?year=" + year +
                "&month=" + month;
    }

}
