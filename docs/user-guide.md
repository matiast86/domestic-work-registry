# 🧭 Guía de Usuario - Domestic Work Registry

## 👋 Bienvenida

**Domestic Work Registry** es una aplicación diseñada para simplificar la **gestión del trabajo doméstico registrado**.  
Permite a empleadores y empleados llevar un control claro de **contratos, tareas, asistencia y recibos de sueldo**.

La aplicación está disponible tanto en computadora como en dispositivos móviles, con una interfaz limpia y adaptada a cada rol.

---

## 👑 Roles en la plataforma

| Rol | Descripción |
|------|--------------|
| **Empleador** | Registra empleados, gestiona contratos, carga tareas, controla asistencias y genera recibos. |
| **Empleado** | Accede a sus contratos activos, visualiza tareas registradas, consulta asistencias y descarga recibos. |

---

## 🔐 Registro y acceso

### ✳️ Crear una cuenta

1. Ingresá a la página principal y seleccioná **“Registrarse”**.  
2. Completá tu nombre, correo electrónico y contraseña.  
3. Recibirás un correo de confirmación con un enlace para activar tu cuenta.  
4. Una vez activada, podrás **iniciar sesión** desde `/login`.

### 🔁 Recuperar contraseña

1. En la pantalla de inicio de sesión, seleccioná **“¿Olvidaste tu contraseña?”**.  
2. Ingresá tu correo electrónico registrado.  
3. Recibirás un enlace temporal para restablecer tu contraseña.  

---

## 🧑‍💼 Guía para Empleadores

### 1️⃣ Crear un nuevo contrato

1. Desde el menú lateral, accedé a **Contratos → Nuevo Contrato**.  
2. Completá los datos del empleado, tipo de trabajo, salario, horario y duración.  
3. Guardá el contrato. Verás su estado como **“Activo”** en tu panel.  

💡 **Tip:** Podés agregar más de un horario semanal usando el botón “➕ Agregar fila”.  

---

### 2️⃣ Registrar tareas o trabajos por hora

1. Ingresá al contrato y seleccioná **“Ver Tareas”**.  
2. Completá la fecha, hora de inicio, hora de fin, tarifa por hora y transporte.  
3. El sistema calculará automáticamente las **horas trabajadas y el total a pagar**.  
4. Guardá la tarea y verás un resumen mensual actualizado.

📅 **Consejo:** Usá los filtros por mes y año para revisar tareas anteriores.

---

### 3️⃣ Controlar asistencia

1. Accedé a **Asistencia** dentro del contrato.  
2. Verás un **calendario mensual interactivo** con cada día trabajado.  
3. Podés cambiar el estado de asistencia haciendo clic en un día y eligiendo entre:  
   - ✅ Presente  
   - ⚠️ Ausencia justificada  
   - ❌ Ausencia injustificada  
   - 🏖️ Feriado nacional  
   - 🕓 Llegada tarde  
4. Los cambios se guardan automáticamente.

---

### 4️⃣ Generar recibos de sueldo

1. Desde el menú, entrá a **Recibos → Generar Recibo**.  
2. Seleccioná el contrato y el período (mes y año).  
3. El sistema reunirá automáticamente las tareas y asistencias correspondientes.  
4. Revisá los montos y confirmá para generar el recibo.  

📄 El recibo se guarda como **“Borrador”** hasta que lo marques como **“Finalizado”**.

---

## 👩‍🦰 Guía para Empleados

### 1️⃣ Activar tu cuenta

- Cuando tu empleador te registre, recibirás un correo con un enlace de activación.  
- Definí tu contraseña para acceder a tu panel personal.

---

### 2️⃣ Ver tus contratos

1. En el panel principal (**Dashboard**), accedé a la pestaña **“Como Empleado”**.  
2. Verás tus contratos activos con detalles como: tipo de trabajo, días, salario y empleador.

📋 Podés consultar la información completa o descargar una copia del contrato.

---

### 3️⃣ Consultar tus tareas

1. Entrá en **Tareas** desde tu contrato.  
2. Podés filtrar por mes o buscar por palabra clave.  
3. Cada fila muestra la fecha, horas trabajadas y monto correspondiente.

💡 Si algo no coincide, contactá al empleador para solicitar una revisión.

---

### 4️⃣ Descargar tus recibos

1. En el menú lateral, accedé a **Recibos**.  
2. Verás una lista con tus recibos de sueldo.  
3. Hacé clic en **“Ver Detalle”** o en el ícono de descarga 📄 para obtener el PDF.  

Los recibos están organizados por estado:
- 🟡 *Borrador*  
- 🟢 *Finalizado*  
- 🔴 *Cancelado*  

---

## 🧭 Panel principal (Dashboard)

El dashboard centraliza toda la información y se divide en dos pestañas:

- **Como Empleador:** muestra tus contratos registrados, tareas pendientes y accesos a recibos.  
- **Como Empleado:** permite ver contratos activos, asistencias y recibos generados.

Ambas pestañas se cargan dinámicamente mediante `fetch`, sin recargar la página completa.

---

## 💬 Soporte y contacto

Si necesitás ayuda técnica o detectás un error:
1. Hacé clic en el ícono de **Ayuda (?)** en el menú lateral.  
2. Elegí **“Contactar Soporte”** o escribí al correo del administrador.  

---

## 🪄 Consejos generales

- 🌙 Podés reducir el menú lateral con el botón *☰* (se guarda en memoria).  
- 📱 La aplicación está totalmente optimizada para dispositivos móviles.  
- 💾 Los cambios en formularios se guardan con validación inmediata.  
- 🔒 Todos los datos personales están protegidos mediante encriptación y autenticación segura.

---

## 🧩 Glosario rápido

| Término | Significado |
|----------|--------------|
| **Contrato** | Relación laboral entre empleador y empleado. |
| **Tarea / Job** | Registro individual de horas trabajadas o trabajo realizado. |
| **Asistencia** | Control diario del cumplimiento del horario. |
| **Recibo / Payslip** | Documento con el detalle del salario mensual calculado. |

---

**Versión:** 1.0  
**Actualizado:** Octubre 2025  
© Domestic Work Registry – Todos los derechos reservados.
