document.addEventListener("DOMContentLoaded", () => {
  console.log("✅ Dashboard JS loaded");

  const employerTab = document.querySelector("#employer-tab");

  const employeeTab = document.querySelector("#employee-tab");

  const employerContainer = document.getElementById("employer");
  const employeeContainer = document.getElementById("employee");

  // Load employer contracts by default if the tab exists
  if (employerTab && employerContainer) {
    console.log("Loading employer contracts by default");
    loadContent("/dashboard/employer-contracts", "employer");

    employerTab.addEventListener("shown.bs.tab", () => {
      loadContent("/dashboard/employer-contracts", "employer");
    });
  }

  // Load employee contracts if that tab exists
  if (employeeTab && employeeContainer) {
    console.log("Loading employee contracts by default");
    loadContent("/dashboard/employee-contracts", "employee");

    employeeTab.addEventListener("shown.bs.tab", () => {
      loadContent("/dashboard/employee-contracts", "employee");
    });
  }
});

// Reusable loader
const showLoader = (containerId) => {
  document.getElementById(containerId).innerHTML = `
    <div class="d-flex justify-content-center align-items-center py-5">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Cargando...</span>
      </div>
    </div>`;
};

// Reusable fetcher
const loadContent = (url, containerId) => {
  showLoader(containerId);
  fetch(url)
    .then((res) => {
      if (!res.ok) throw new Error("Error al cargar contenido");
      return res.text();
    })
    .then((html) => (document.getElementById(containerId).innerHTML = html))
    .catch((err) => {
      document.getElementById(containerId).innerHTML = `
        <div class="alert alert-danger" role="alert">
          ${err.message}
        </div>`;
    });
};
