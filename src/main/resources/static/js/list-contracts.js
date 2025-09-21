const changeEnumType = (str) => {
  switch (str) {
    case "NANNY":
      return "CUIDADO DE PERSONAS";
    case "HOUSE KEEPER":
      return "TAREAS GENERALES";
    case "HOURLY":
      return "POR HORA";
    case "MONTHLY":
      return "MENSUAL";
    default:
      return str;
  }
};

let jobType = document.getElementById("jobType");

jobType.textContent = changeEnumType(jobType.textContent.trim());
console.log("Puesto :" + jobType.textContent.trim());

let employmentType = document.getElementById("employmentType");

employmentType.textContent = changeEnumType(employmentType.textContent.trim());
console.log("Tipo de empleo: " + employmentType.textContent.trim());
