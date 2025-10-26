# 📘 Frontend Architecture & UX/UI Guide

## 🧠 Overview

El **frontend** de *Domestic Work Registry* está construido sobre **Spring Boot + Thymeleaf + Bootstrap 5**, con una capa de estilos modularizada en CSS y una serie de scripts JavaScript organizados por funcionalidad.  
Su objetivo es ofrecer una **experiencia moderna, fluida y responsiva**, sin depender de frameworks SPA.

---

## 🏗️ Folder Structure

```
src/
 └── main/
     └── resources/
         ├── static/
         │    ├── css/
         │    │    ├── attendance.css
         │    │    ├── contracts.css
         │    │    ├── forms.css
         │    │    ├── jobs.css
         │    │    ├── auth.css
         │    │    └── payslips.css
         │    │
         │    ├── js/
         │    │    ├── navigation.js
         │    │    ├── dashboard.js
         │    │    ├── attendance.js
         │    │    ├── jobs.js
         │    │    ├── countries.js
         │    │    └── status-badges.js
         │    │
         │    └── data/
         │         ├── countries.json
         │         └── provinces.json
         │
         └── templates/
              ├── fragments/
              ├── dashboard/
              ├── contracts/
              ├── jobs/
              ├── attendance/
              ├── payslips/
              └── auth/
```

---

## 🎨 CSS Architecture

Cada archivo `.css` representa un **módulo visual** cohesivo, diseñado con variables globales (`--color-primary`, `--radius-xl`, `--shadow-lg`) y principios de consistencia tipográfica, espaciado y color.

| File | Purpose |
|------|----------|
| **`login-register.css`** | Estilos de autenticación: formularios de login, registro y recuperación de contraseña. Usa gradientes, íconos y tarjetas limpias con foco UX. |
| **`contracts.css`** | Estructura visual de los detalles de contrato: tabs, cabeceras, cards de información y tablas de horarios. |
| **`attendance.css`** | Vista de calendario interactivo para asistencia. Contiene grillas responsivas, estados visuales (`PRESENT`, `ABSENT`, `LATE`, etc.) y leyendas. |
| **`jobs.css`** | Consolidado para listas, reportes mensuales y formularios de tareas. Incluye filtros, tablas con hover y previsualización de cálculos en tiempo real. |
| **`forms.css`** | Estilos compartidos para formularios CRUD (empleadores, empleados, contratos). Define headers de color contextual (`success`, `warning`, `info`) y secciones segmentadas. |
| **`payslips.css`** | Diseño de recibos: selección de período, previsualización, resumen y grillas de recibos históricos con badges de estado. |

### 🧩 Design Principles

- **Consistencia cromática:** colores primarios (`--color-primary`), secundarios e informativos mantienen coherencia a lo largo de todas las vistas.  
- **Feedback visual inmediato:** hover, sombras y transiciones suaves (`var(--transition-base)`).  
- **Escalabilidad tipográfica:** encabezados fuertes, subtítulos suaves y badges legibles.  
- **Accesibilidad:** contraste adecuado y semántica visual (íconos + texto).  
- **Responsividad total:** uso extensivo de *grid layouts* y media queries hasta `max-width: 480px`.

---

## ⚙️ JavaScript Modules

Cada script encapsula una responsabilidad específica y trabaja en sinergia con Thymeleaf para cargar contenido dinámico, validar formularios o mejorar la UX.

| File | Responsibility |
|------|----------------|
| **`navigation.js`** | Controla el sidebar (colapsado, móvil, overlay, almacenamiento del estado en `localStorage`) y resalta la ruta activa. |
| **`dashboard.js`** | Carga dinámica de contratos (empleador/empleado) en pestañas mediante `fetch()` y loaders reutilizables. |
| **`attendance.js`** | Generación del calendario mensual, control de dropdowns para modificar estados y manejo de clics fuera del componente. |
| **`jobs.js`** | Filtrado, búsqueda, confirmación de eliminación, cálculo de horas y validación de horarios. Implementa previsualización del total en tiempo real. |
| **`countries.js`** | Carga dinámica de países y provincias desde archivos JSON y lógica para agregar/eliminar filas en la tabla de horarios (`scheduleTable`). |
| **`status-badges.js`** | Aplica estilos de color a badges según estado (`BORRADOR`, `FINALIZADO`, `CANCELADO`), compatible con labels y enums. |

---

## 🧠 UX / UI Behavior Summary

| Interaction | Behavior |
|-------------|-----------|
| **Sidebar toggle** | Guarda el estado colapsado en `localStorage` y lo restaura en sesiones posteriores. |
| **Tabs dinámicas (Dashboard)** | Carga contenido vía `fetch` sin recargar la página. Usa un spinner de Bootstrap mientras carga. |
| **Calendario (Attendance)** | Dibuja automáticamente el mes actual y permite desplegar dropdowns para editar estado diario. |
| **Job form** | Calcula automáticamente horas, subtotal y total según inputs. Previsualiza el resultado con estilo contable. |
| **Payslip generation** | Guía al usuario paso a paso con mensajes informativos y accesos rápidos hacia tareas o asistencias. |
| **Responsividad total** | Todos los componentes colapsan correctamente hasta móvil (menús, formularios, tablas y calendarios). |

---

## 💅 Design Tokens (Variables Base)

Declaradas en `:root` dentro del layout principal (`layout.html` o `styles.css`):

```css
:root {
  --color-primary: #0ea5e9;
  --color-primary-light: #38bdf8;
  --color-success: #22c55e;
  --color-warning: #fbbf24;
  --color-danger: #ef4444;
  --color-info: #8b5cf6;
  --radius-md: 0.5rem;
  --radius-xl: 1rem;
  --shadow-sm: 0 1px 3px rgba(0,0,0,0.1);
  --shadow-lg: 0 8px 24px rgba(0,0,0,0.15);
  --transition-base: all 0.3s ease;
}
```

Estas variables aseguran coherencia entre todas las vistas y facilitan mantener una identidad visual unificada.

---

## 🔐 Integration with Thymeleaf

Cada vista HTML hereda del layout principal con:
```html
<html xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="~{fragments/layout}">
```

Y define su bloque principal:
```html
<main layout:fragment="content">
  <!-- contenido específico -->
</main>
```

Los scripts se inyectan al final del body para optimizar rendimiento:
```html
<th:block layout:fragment="scripts">
  <script th:src="@{/js/navigation.js}"></script>
  <script th:src="@{/js/dashboard.js}"></script>
</th:block>
```

---

## 🧩 Reusability & Extensibility

- Todos los módulos `.js` son **idempotentes** (pueden cargarse más de una vez sin romper el DOM).  
- El CSS mantiene un esquema de **prefijos por dominio** (`attendance-`, `contract-`, `payslip-`, etc.) para evitar conflictos.  
- Se puede extender el diseño con un `theme-light.css` o `theme-dark.css` simplemente sobreescribiendo las variables raíz.

---

## 🚀 Future Improvements

| Area | Idea |
|------|------|
| **Performance** | Minificar CSS/JS con Webpack o Maven plugin. |
| **Accessibility** | Agregar etiquetas `aria-` y roles en elementos interactivos. |
| **Dark Mode** | Implementar tema oscuro con `prefers-color-scheme: dark`. |
| **Localization** | Extraer textos fijos de los scripts y usarlos desde `messages.properties`. |
