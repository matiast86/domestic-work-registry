document.addEventListener("DOMContentLoaded", () => {
  const employerTab = document.querySelector("#employer-tab");
  const employeeTab = document.querySelector("#employee-tab");

  // Reusable loader
  const showLoader = (containerId) => {
    document.getElementById(containerId).innerHTML = `
      <div class="d-flex justify-content-center align-items-center py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Cargando...</span>
        </div>
      </div>
    `;
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
          </div>
        `;
      });
  };

  if (employerTab) {
    employerTab.addEventListener("shown.bs.tab", () => {
      loadContent("/dashboard/employer-contracts", "employer");
    });
  }

  if (employeeTab) {
    employeeTab.addEventListener("shown.bs.tab", () => {
      loadContent("/dashboard/employee-contracts", "employee");
    });
  }
});
