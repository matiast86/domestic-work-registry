// ===== NAVIGATION LOGIC - navigation.js =====

document.addEventListener("DOMContentLoaded", () => {
  const sidebar = document.getElementById("sidebar");
  const sidebarToggle = document.getElementById("sidebarToggle");
  const mobileMenuToggle = document.getElementById("mobileMenuToggle");
  const mobileOverlay = document.getElementById("mobileOverlay");

  // ===== Desktop: Toggle Collapsed State =====
  if (sidebarToggle) {
    sidebarToggle.addEventListener("click", () => {
      sidebar.classList.toggle("collapsed");
      // Save state to localStorage
      localStorage.setItem(
        "sidebarCollapsed",
        sidebar.classList.contains("collapsed")
      );
    });
  }

  // ===== Mobile: Toggle Sidebar Visibility =====
  if (mobileMenuToggle) {
    mobileMenuToggle.addEventListener("click", () => {
      sidebar.classList.add("show");
      mobileOverlay.classList.add("show");
      document.body.style.overflow = "hidden"; // Prevent body scroll
    });
  }

  // Close sidebar when clicking overlay
  if (mobileOverlay) {
    mobileOverlay.addEventListener("click", () => {
      closeMobileSidebar();
    });
  }

  // Close sidebar on mobile when clicking a nav link
  const navLinks = document.querySelectorAll(".sidebar .nav-link");
  navLinks.forEach((link) => {
    link.addEventListener("click", () => {
      if (window.innerWidth <= 992) {
        closeMobileSidebar();
      }
    });
  });

  const closeMobileSidebar = () => {
    sidebar.classList.remove("show");
    mobileOverlay.classList.remove("show");
    document.body.style.overflow = ""; // Restore body scroll
  };

  // ===== Restore Sidebar State from LocalStorage =====
  const savedState = localStorage.getItem("sidebarCollapsed");
  if (savedState === "true" && window.innerWidth > 992) {
    sidebar.classList.add("collapsed");
  }

  // ===== Highlight Active Nav Item Based on Current Path =====
  const currentPath = window.location.pathname;
  navLinks.forEach((link) => {
    const linkPath = link.getAttribute("href");

    // Remove active class from all links first
    link.classList.remove("active");

    // Add active class to matching link
    if (linkPath === currentPath) {
      link.classList.add("active");
    } else if (currentPath.startsWith(linkPath) && linkPath !== "/dashboard") {
      // Handle sub-routes (but not root dashboard)
      link.classList.add("active");
    }
  });

  // ===== Handle Window Resize =====
  let resizeTimer;
  window.addEventListener("resize", () => {
    clearTimeout(resizeTimer);
    resizeTimer = setTimeout(() => {
      if (window.innerWidth > 992) {
        // Desktop: restore collapsed state, hide mobile overlay
        closeMobileSidebar();
        const savedState = localStorage.getItem("sidebarCollapsed");
        if (savedState === "true") {
          sidebar.classList.add("collapsed");
        } else {
          sidebar.classList.remove("collapsed");
        }
      } else {
        // Mobile: remove collapsed state
        sidebar.classList.remove("collapsed");
      }
    }, 250);
  });

  // ===== Smooth Scroll to Top on Navigation =====
  navLinks.forEach((link) => {
    link.addEventListener("click", (e) => {
      // Only prevent default if it's an anchor link
      if (link.getAttribute("href").startsWith("#")) {
        e.preventDefault();
        window.scrollTo({ top: 0, behavior: "smooth" });
      }
    });
  });
});
