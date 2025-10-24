// ===== ATTENDANCE.JS - Calendar Interactions =====

/**
 * Build calendar grid with proper alignment
 */
document.addEventListener("DOMContentLoaded", function () {
  const calendarGrid = document.getElementById("calendarGrid");
  if (!calendarGrid) return;

  // Get all calendar days (hidden by default)
  const calendarDays = Array.from(document.querySelectorAll(".calendar-day"));

  if (calendarDays.length === 0) return;

  // Create a map of days by date
  const daysMap = new Map();
  calendarDays.forEach((day) => {
    const dayOfMonth = parseInt(day.dataset.dayOfMonth);
    daysMap.set(dayOfMonth, day);
  });

  // Get year and month from first record
  const firstDate = new Date(calendarDays[0].dataset.date + "T12:00:00");
  const year = firstDate.getFullYear();
  const month = firstDate.getMonth(); // 0-based

  // Get first day of month (0=Sunday, 1=Monday, ..., 6=Saturday)
  const firstDayDate = new Date(year, month, 1);
  const firstDayOfWeek = firstDayDate.getDay(); // 0=Sunday, 1=Monday, etc.

  // Convert to Monday=0, Tuesday=1, ..., Sunday=6
  const firstDayAdjusted = firstDayOfWeek === 0 ? 6 : firstDayOfWeek - 1;

  console.log("First day of month:", firstDayDate.toDateString());
  console.log("Day of week (0=Sun):", firstDayOfWeek);
  console.log("Adjusted (0=Mon):", firstDayAdjusted);

  // Get total days in month
  const daysInMonth = new Date(year, month + 1, 0).getDate();

  // Clear grid
  calendarGrid.innerHTML = "";

  // Add empty cells before first day of month
  for (let i = 0; i < firstDayAdjusted; i++) {
    const emptyCell = document.createElement("div");
    emptyCell.className = "calendar-day-empty";
    calendarGrid.appendChild(emptyCell);
  }

  // Add all days of the month
  for (let dayNum = 1; dayNum <= daysInMonth; dayNum++) {
    if (daysMap.has(dayNum)) {
      // This day has attendance record
      const dayElement = daysMap.get(dayNum);
      dayElement.style.display = "flex";
      calendarGrid.appendChild(dayElement);
    } else {
      // This day has no attendance record (non-working day)
      const emptyCell = document.createElement("div");
      emptyCell.className = "calendar-day-empty";
      calendarGrid.appendChild(emptyCell);
    }
  }
});

/**
 * Toggle dropdown for changing attendance status
 */
const toggleDropdown = (dayElement) => {
  // Close all other dropdowns first
  document.querySelectorAll(".calendar-day.active").forEach((day) => {
    if (day !== dayElement) {
      day.classList.remove("active");
    }
  });

  // Toggle current dropdown
  dayElement.classList.toggle("active");
};

/**
 * Close dropdown when clicking outside
 */
document.addEventListener("click", (event) => {
  const calendarDays = document.querySelectorAll(".calendar-day");

  calendarDays.forEach((day) => {
    if (!day.contains(event.target)) {
      day.classList.remove("active");
    }
  });
});

/**
 * Prevent dropdown from closing when clicking inside it
 */
document.addEventListener("DOMContentLoaded", () => {
  const dropdowns = document.querySelectorAll(".status-dropdown");

  dropdowns.forEach((dropdown) => {
    dropdown.addEventListener("click", (event) => {
      event.stopPropagation();
    });
  });
});

/**
 * Optional: Keyboard navigation
 */
document.addEventListener("keydown", (event) => {
  // Close dropdown on ESC key
  if (event.key === "Escape") {
    document.querySelectorAll(".calendar-day.active").forEach((day) => {
      day.classList.remove("active");
    });
  }
});

/**
 * Optional: Add animation when hovering over calendar days
 */
document.addEventListener("DOMContentLoaded", () => {
  const calendarDays = document.querySelectorAll(".calendar-day");

  calendarDays.forEach((day) => {
    day.addEventListener("mouseenter", () => {
      this.style.transition = "all 0.3s ease";
    });
  });
});

/////////////////////////////////////////////////////////////////////

// document.getElementById("month").addEventListener("change", () => {
//   document.getElementById("filterForm").submit();
// });

// document.addEventListener("DOMContentLoaded", () => {
//   const badges = document.querySelectorAll(".status-badge");

//   const statusStyles = new Map([
//     ["PRESENTE", ["bg-success"]],
//     ["AUSENCIA JUSTIFICADA", ["bg-warning", "text-dark"]],
//     ["AUSENCIA INJUSTIFICADA", ["bg-danger"]],
//     ["FERIADO NACIONAL", ["bg-info", "text-dark"]],
//     ["LLEGADA TARDE", ["bg-primary"]],
//     ["MODIFICACIÓN", ["bg-secondary", "text-light"]],
//     ["OTRO", ["bg-secondary"]],
//     // From enum keys (in case you ever print names instead of labels)
//     ["PRESENT", ["bg-success"]],
//     ["JUSTIFIED_ABSCENCE", ["bg-warning", "text-dark"]],
//     ["UNJUSTIFIED_ABSCENCE", ["bg-danger"]],
//     ["NATIONAL_HOLIDAY", ["bg-info", "text-dark"]],
//     ["LATE", ["bg-primary"]],
//     ["EXTRA_JOB", ["bg-secondary", "text-light"]],
//     ["OTHER", ["bg-secondary"]],
//   ]);

//   badges.forEach((badge) => {
//     const status = badge.textContent.trim().toUpperCase();
//     const classes = statusStyles.get(status) || ["bg-secondary"];
//     badge.classList.add(...classes);
//   });
// });
