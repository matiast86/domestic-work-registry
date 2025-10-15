document.addEventListener("DOMContentLoaded", () => {
  const badges = document.querySelectorAll(".status-badge");

  const statusStyles = new Map([
    ["BORRADOR", ["bg-warning", "text-dark"]],
    ["FINALIZADO", ["bg-success"]],
    ["CANCELADO", ["bg-danger"]],

    // From enum keys (in case you ever print names instead of labels)
    ["DRAFT", ["bg-warning", "text-dark"]],
    ["FINALIZED", ["bg-success"]],
    ["CANCELLED", ["bg-danger"]],
  ]);

  badges.forEach((badge) => {
    const status = badge.textContent.trim().toUpperCase();
    const classes = statusStyles.get(status) || ["bg-secondary"];
    badge.classList.add(...classes);
  });
});
