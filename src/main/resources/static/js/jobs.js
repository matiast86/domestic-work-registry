// ===== JOBS.JS - Consolidated JavaScript for job-list, monthly-details, job-form =====

document.addEventListener("DOMContentLoaded", () => {
  // ============================================
  // JOB LIST - Filter functionality
  // ============================================

  // Simple filter functionality
  document.getElementById("searchInput")?.addEventListener("input", (e) => {
    const searchTerm = e.target.value.toLowerCase();
    const rows = document.querySelectorAll(".tasks-table tbody tr");

    rows.forEach((row) => {
      const text = row.textContent.toLowerCase();
      row.style.display = text.includes(searchTerm) ? "" : "none";
    });
  });

  document.getElementById("yearFilter")?.addEventListener("change", (e) => {
    const year = e.target.value;
    const rows = document.querySelectorAll(".tasks-table tbody tr");

    rows.forEach((row) => {
      const rowYear = row
        .querySelector(".month-badge span")
        .textContent.split(" ")[1];
      row.style.display = !year || rowYear === year ? "" : "none";
    });
  });

  // ============================================
  // MONTHLY DETAILS - Delete confirmation
  // ============================================

  window.confirmDelete = function (button) {
    const jobId = button.getAttribute("data-job-id");
    const deleteForm = document.getElementById("deleteForm");

    if (deleteForm) {
      deleteForm.action = `/jobs/delete/${jobId}`;
      const modal = new bootstrap.Modal(document.getElementById("deleteModal"));
      modal.show();
    }
  };

  // Monthly details search
  const monthlySearchInput = document.querySelector("#searchInput");
  if (monthlySearchInput && document.querySelector(".tasks-detail-table")) {
    monthlySearchInput.addEventListener("input", (e) => {
      const searchTerm = e.target.value.toLowerCase();
      const rows = document.querySelectorAll(".tasks-detail-table tbody tr");

      rows.forEach((row) => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(searchTerm) ? "" : "none";
      });
    });
  }

  // ============================================
  // JOB FORM - Real-time calculation
  // ============================================

  // Real-time calculation
  function calculateTotal() {
    const startTime = document.getElementById("startTime").value;
    const endTime = document.getElementById("endTime").value;
    const hourlyRate =
      parseFloat(document.getElementById("hourlyRate").value) || 0;
    const transportation =
      parseFloat(document.getElementById("transportationFee").value) || 0;

    let hoursWorked = 0;
    if (startTime && endTime) {
      const [startHour, startMin] = startTime.split(":").map(Number);
      const [endHour, endMin] = endTime.split(":").map(Number);

      const startMinutes = startHour * 60 + startMin;
      const endMinutes = endHour * 60 + endMin;

      hoursWorked = (endMinutes - startMinutes) / 60;
      hoursWorked = Math.max(0, hoursWorked);
    }

    const subtotal = hoursWorked * hourlyRate;
    const total = subtotal + transportation;

    document.getElementById("hoursWorked").textContent =
      hoursWorked.toFixed(1) + " hs";
    document.getElementById("rateDisplay").textContent =
      "$" + hourlyRate.toFixed(2);
    document.getElementById("subtotal").textContent = "$" + subtotal.toFixed(2);
    document.getElementById("transportDisplay").textContent =
      "$" + transportation.toFixed(2);
    document.getElementById("totalAmount").textContent = "$" + total.toFixed(2);
  }

  // Add event listeners
  document
    .getElementById("startTime")
    ?.addEventListener("change", calculateTotal);
  document
    .getElementById("endTime")
    ?.addEventListener("change", calculateTotal);
  document
    .getElementById("hourlyRate")
    ?.addEventListener("input", calculateTotal);
  document
    .getElementById("transportationFee")
    ?.addEventListener("input", calculateTotal);

  // Initial calculation
  calculateTotal();

  // Set today's date by default
  const dateInput = document.querySelector('input[type="date"]');
  if (dateInput && !dateInput.value) {
    dateInput.valueAsDate = new Date();
    calculateTotal();
  }

  // ============================================
  // VALIDATION - Time range validation
  // ============================================

  if (endTimeInput && startTimeInput) {
    endTimeInput.addEventListener("change", function () {
      const startTime = startTimeInput.value;
      const endTime = endTimeInput.value;

      if (startTime && endTime && endTime <= startTime) {
        endTimeInput.setCustomValidity(
          "La hora de fin debe ser posterior a la hora de inicio"
        );
        endTimeInput.reportValidity();
      } else {
        endTimeInput.setCustomValidity("");
      }
    });
  }

  // ============================================
  // UTILITY - Format currency inputs
  // ============================================

  const currencyInputs = document.querySelectorAll(
    'input[type="number"][step="0.01"]'
  );
  currencyInputs.forEach((input) => {
    input.addEventListener("blur", function () {
      if (this.value && !isNaN(this.value)) {
        this.value = parseFloat(this.value).toFixed(2);
      }
    });
  });
});
