document.addEventListener("DOMContentLoaded", () => {
  const employerTab = document.querySelector("#employer-tab");
  const employeeTab = document.querySelector("#employee-tab");

  // Load employer contracts immediately
  fetch("/dashboard/employer-contracts")
    .then((res) => res.text())
    .then((html) => (document.getElementById("employer").innerHTML = html));

  if (employerTab) {
    employerTab.addEventListener("shown.bs.tab", () => {
      fetch("/dashboard/employer-contracts")
        .then((res) => res.text())
        .then((html) => (document.getElementById("employer").innerHTML = html));
    });
  }

  if (employeeTab) {
    employeeTab.addEventListener("shown.bs.tab", () => {
      fetch("/dashboard/employee-contracts")
        .then((res) => res.text())
        .then((html) => (document.getElementById("employee").innerHTML = html));
    });
  }
});
