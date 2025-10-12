document.getElementById("month").addEventListener("change", () => {
  document.getElementById("filterForm").submit();
});

document.addEventListener("DOMContentLoaded", () => {
  const badges = document.querySelectorAll(".status-badge");

  const statusStyles = new Map([
    ["PRESENTE", ["bg-success"]],
    ["AUSENCIA JUSTIFICADA", ["bg-warning", "text-dark"]],
    ["AUSENCIA INJUSTIFICADA", ["bg-danger"]],
    ["FERIADO NACIONAL", ["bg-info", "text-dark"]],
    ["LLEGADA TARDE", ["bg-primary"]],
    ["MODIFICACIÓN", ["bg-secondary", "text-light"]],
    ["OTRO", ["bg-secondary"]],
    // From enum keys (in case you ever print names instead of labels)
    ["PRESENT", ["bg-success"]],
    ["JUSTIFIED_ABSCENCE", ["bg-warning", "text-dark"]],
    ["UNJUSTIFIED_ABSCENCE", ["bg-danger"]],
    ["NATIONAL_HOLIDAY", ["bg-info", "text-dark"]],
    ["LATE", ["bg-primary"]],
    ["EXTRA_JOB", ["bg-secondary", "text-light"]],
    ["OTHER", ["bg-secondary"]],
  ]);

  badges.forEach((badge) => {
    const status = badge.textContent.trim().toUpperCase();
    const classes = statusStyles.get(status) || ["bg-secondary"];
    badge.classList.add(...classes);
  });
});
