package com.springboot.domesticworkregistry.controller.attendance;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.domesticworkregistry.entities.AttendanceRecord;
import com.springboot.domesticworkregistry.enums.AttendanceStatus;
import com.springboot.domesticworkregistry.service.attendance.AttendanceRecordService;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceRecordService attendanceService;

    public AttendanceController(AttendanceRecordService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/view/{scheduleId}")
    public String viewAttendance(@PathVariable("scheduleId") int scheduleId, @RequestParam("year") int year,
            @RequestParam("month") int month, Model model) {
        List<AttendanceRecord> records = attendanceService.findByScheduleAndMonth(scheduleId, year, month);

        model.addAttribute("attendanceRecords", records);
        model.addAttribute("monthName", Month.of(month).getDisplayName(TextStyle.FULL, Locale.of("es")));
        model.addAttribute("year", year);
        return "attendance/attendance-table";
    }

    @PostMapping("/updateStatus")
    public String updateStatus(@RequestParam("recordId") int recordId,
            @RequestParam("attendanceStatus") AttendanceStatus newStatus, @RequestParam("scheduleId") int scheduleId,
            RedirectAttributes redirectAttributes) {
        attendanceService.updateStatus(recordId, newStatus);
        redirectAttributes.addFlashAttribute("successMessage", "Asistencia actualizada correctamente");
        return "redirect:/attendance/view/" + scheduleId;
    }

}
