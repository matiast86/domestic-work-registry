// ===== NAVIGATION.JS =====
// Handles sidebar collapse, mobile menu, and active states

document.addEventListener("DOMContentLoaded", () => {
  const sidebar = document.getElementById("sidebar");
  const sidebarToggle = document.getElementById("sidebarToggle");
  const mobileMenuToggle = document.getElementById("mobileMenuToggle");
  const mobileOverlay = document.getElementById("mobileOverlay");

  // ==========================================
  // DESKTOP: Toggle sidebar collapsed state
  // ==========================================
  if (sidebarToggle) {
    sidebarToggle.addEventListener("click", () => {
      sidebar.classList.toggle("collapsed");

      // Save state to localStorage
      const isCollapsed = sidebar.classList.contains("collapsed");
      localStorage.setItem("sidebarCollapsed", isCollapsed);

      // Update icon
      const icon = sidebarToggle.querySelector("i");
      if (isCollapsed) {
        icon.classList.remove("bi-list");
        icon.classList.add("bi-arrow-bar-right");
      } else {
        icon.classList.remove("bi-arrow-bar-right");
        icon.classList.add("bi-list");
      }
    });
  }

  // ==========================================
  // MOBILE: Toggle sidebar visibility
  // ==========================================
  if (mobileMenuToggle) {
    mobileMenuToggle.addEventListener("click", () => {
      sidebar.classList.add("show");
      mobileOverlay.classList.add("show");
      document.body.style.overflow = "hidden";
    });
  }

  if (mobileOverlay) {
    mobileOverlay.addEventListener("click", closeMobileSidebar);
  }

  function closeMobileSidebar() {
    sidebar.classList.remove("show");
    mobileOverlay.classList.remove("show");
    document.body.style.overflow = "";
  }

  // Close mobile sidebar on navigation
  document.querySelectorAll(".sidebar .nav-link").forEach((link) => {
    link.addEventListener("click", () => {
      if (window.innerWidth < 993) {
        closeMobileSidebar();
      }
    });
  });

  // ==========================================
  // RESTORE sidebar state from localStorage
  // ==========================================
  if (localStorage.getItem("sidebarCollapsed") === "true") {
    sidebar.classList.add("collapsed");
    const icon = sidebarToggle?.querySelector("i");
    if (icon) {
      icon.classList.remove("bi-list");
      icon.classList.add("bi-arrow-bar-right");
    }
  }

  // ==========================================
  // HIGHLIGHT active nav item based on current path
  // ==========================================
  const currentPath = window.location.pathname;

  document.querySelectorAll(".sidebar .nav-link").forEach((link) => {
    const linkPath = link.getAttribute("href");

    // Remove active from all
    link.classList.remove("active");

    // Add active to matching link
    if (linkPath === currentPath) {
      link.classList.add("active");
    }

    // Special case for dashboard-related paths
    if (currentPath.startsWith("/dashboard") && linkPath === "/dashboard") {
      link.classList.add("active");
    }

    // Special case for contracts
    if (currentPath.includes("/contracts") && linkPath.includes("contracts")) {
      link.classList.add("active");
    }
  });

  // ==========================================
  // HANDLE window resize
  // ==========================================
  let resizeTimer;
  window.addEventListener("resize", () => {
    clearTimeout(resizeTimer);
    resizeTimer = setTimeout(() => {
      if (window.innerWidth >= 993) {
        closeMobileSidebar();
      }
    }, 250);
  });

  // ==========================================
  // SMOOTH SCROLL for anchor links
  // ==========================================
  document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
    anchor.addEventListener("click", function (e) {
      const target = document.querySelector(this.getAttribute("href"));
      if (target) {
        e.preventDefault();
        target.scrollIntoView({
          behavior: "smooth",
          block: "start",
        });
      }
    });
  });

  // ==========================================
  // ADD loading state to buttons on click
  // ==========================================
  document.querySelectorAll('button[type="submit"]').forEach((button) => {
    button.addEventListener("click", function (e) {
      if (this.form && this.form.checkValidity()) {
        const originalText = this.innerHTML;
        this.disabled = true;
        this.innerHTML =
          '<span class="spinner-border spinner-border-sm me-2"></span>Cargando...';

        // Restore after 10 seconds as fallback
        setTimeout(() => {
          this.disabled = false;
          this.innerHTML = originalText;
        }, 10000);
      }
    });
  });

  console.log("✅ Navigation JS loaded successfully");
});
