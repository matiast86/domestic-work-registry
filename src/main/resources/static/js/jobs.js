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

  const startTimeInput = document.getElementById("startTime");
  const endTimeInput = document.getElementById("endTime");
  const hourlyRateInput = document.getElementById("hourlyRate");
  const transportationInput = document.getElementById("transportationFee");

  function calculateTotal() {
    const startTime = startTimeInput?.value;
    const endTime = endTimeInput?.value;
    const hourlyRate = parseFloat(hourlyRateInput?.value) || 0;
    const transportation = parseFloat(transportationInput?.value) || 0;

    let hoursWorked = 0;

    if (startTime && endTime) {
      const [startHour, startMin] = startTime.split(":").map(Number);
      const [endHour, endMin] = endTime.split(":").map(Number);

      const startMinutes = startHour * 60 + startMin;
      const endMinutes = endHour * 60 + endMin;

      hoursWorked = (endMinutes - startMinutes) / 60;
      hoursWorked = Math.max(0, hoursWorked); // No negative hours
    }

    const subtotal = hoursWorked * hourlyRate;
    const total = subtotal + transportation;

    // Update display elements
    const hoursWorkedEl = document.getElementById("hoursWorked");
    const rateDisplayEl = document.getElementById("rateDisplay");
    const subtotalEl = document.getElementById("subtotal");
    const transportDisplayEl = document.getElementById("transportDisplay");
    const totalAmountEl = document.getElementById("totalAmount");

    if (hoursWorkedEl)
      hoursWorkedEl.textContent = hoursWorked.toFixed(1) + " hs";
    if (rateDisplayEl) rateDisplayEl.textContent = "$" + hourlyRate.toFixed(2);
    if (subtotalEl) subtotalEl.textContent = "$" + subtotal.toFixed(2);
    if (transportDisplayEl)
      transportDisplayEl.textContent = "$" + transportation.toFixed(2);
    if (totalAmountEl) totalAmountEl.textContent = "$" + total.toFixed(2);
  }

  // Add event listeners for calculation
  if (startTimeInput) startTimeInput.addEventListener("change", calculateTotal);
  if (endTimeInput) endTimeInput.addEventListener("change", calculateTotal);
  if (hourlyRateInput)
    hourlyRateInput.addEventListener("input", calculateTotal);
  if (transportationInput)
    transportationInput.addEventListener("input", calculateTotal);

  // Initial calculation on page load
  if (
    startTimeInput ||
    endTimeInput ||
    hourlyRateInput ||
    transportationInput
  ) {
    calculateTotal();
  }

  // Set today's date by default in job form
  const dateInput = document.querySelector('input[type="date"]');
  if (dateInput && !dateInput.value && document.getElementById("jobForm")) {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, "0");
    const day = String(today.getDate()).padStart(2, "0");
    dateInput.value = `${year}-${month}-${day}`;
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
