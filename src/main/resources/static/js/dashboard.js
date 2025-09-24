document.addEventListener("DOMContentLoaded", () => {
  const employerTab = document.querySelector("#employer-tab");
  const employeeTab = document.querySelector("#employee-tab");

  if (employerTab) {
    employerTab.addEventListener("shown.bs.tab", () => {
      fetch("/dashboard/employer-contracts") //Change when endpoint created
        .then((res) => res.text())
        .then((html) => (document.getElementById("employer").innerHTML = html));
    });
  }

  if (employeeTab) {
    employeeTab.addEventListener("shown.bs.tab", () => {
      fetch("/dashboard/employee-contracts") //Change when endpoint created
        .then((res) => res.text())
        .then((html) => (document.getElementById("employee").innerHTML = html));
    });
  }
});
