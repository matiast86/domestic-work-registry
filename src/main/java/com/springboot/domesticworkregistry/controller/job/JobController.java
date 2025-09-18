package com.springboot.domesticworkregistry.controller.job;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.domesticworkregistry.dto.job.CreateJobDto;
import com.springboot.domesticworkregistry.dto.job.JobsReportDto;
import com.springboot.domesticworkregistry.service.job.JobService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/job")
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

    @GetMapping("/add")
    public String jobForm(@RequestParam("contractId") int contractId, Model model) {
        CreateJobDto jobForm = new CreateJobDto();
        model.addAttribute("jobForm", jobForm);
        model.addAttribute("contractId", contractId); // pass contractId to the form
        return "jobs/job-form";
    }

    @PostMapping("/create")
    public String createJob(
            @RequestParam("contractId") int contractId,
            @Valid @ModelAttribute("jobForm") CreateJobDto form,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("contractId", contractId);
            return "jobs/job-form";
        }
        

        jobService.save(form, contractId);

        // Redirect to avoid resubmission
        return "redirect:/job/jobList?contractId=" + contractId;
    }

    @GetMapping("/jobList")
    public String getJobsList(@RequestParam("contractId") int contractId, Model model) {
        JobsReportDto jobs = this.jobService.getJobsByContracDto(contractId);
        model.addAttribute("jobs", jobs);
        return "jobs/job-table";
    }

}
