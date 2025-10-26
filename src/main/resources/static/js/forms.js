const countrySelect = document.getElementById("country");
const stateSelect = document.getElementById("state");

// Fetch helper
const getData = async (url) => {
  const res = await fetch(url);
  if (!res.ok) throw new Error("Error al cargar datos");
  return await res.json();
};

// Load countries into <select>
const processCountriesData = async () => {
  try {
    const countries = await getData("/data/countries.json");
    countries.forEach((country) => {
      const option = document.createElement("option");
      option.textContent = country;
      option.value = country;
      countrySelect.appendChild(option);
    });
  } catch (error) {
    console.error("Error cargando países:", error.message);
  }
};

// Load states when a country is chosen
const processStateData = async (countryName) => {
  try {
    const data = await getData("/data/provinces.json");

    // Clear old states
    stateSelect.innerHTML =
      "<option value=''>-- Seleccione una provincia --</option>";

    const states = data[countryName]; // directly use the JSON object key
    if (states && Array.isArray(states)) {
      states.forEach((state) => {
        const option = document.createElement("option");
        option.textContent = state;
        option.value = state;
        stateSelect.appendChild(option);
      });
    } else {
      console.warn(`No se encontraron provincias para ${countryName}`);
    }
  } catch (error) {
    console.error("Error cargando provincias:", error.message);
  }
};

// Init
processCountriesData();

// Listener: when selecting a country, load states
countrySelect.addEventListener("change", () => {
  const selectedCountry = countrySelect.value;
  if (selectedCountry) {
    processStateData(selectedCountry);
  } else {
    stateSelect.innerHTML =
      "<option value=''>-- Seleccione una provincia --</option>";
  }
});

// contract -form
reindexRows = () => {
  const rows = document.querySelectorAll("#scheduleTable tbody tr");
  rows.forEach((row, index) => {
    row.querySelectorAll("select, input").forEach((input) => {
      const name = input.getAttribute("name");
      if (name) {
        input.setAttribute("name", name.replace(/\[.*?\]/, "[" + index + "]"));
      }
      const id = input.getAttribute("id");
      if (id) {
        input.setAttribute("id", id.replace(/\[.*?\]/, "[" + index + "]"));
      }
    });
  });
};

addRow = () => {
  const table = document
    .getElementById("scheduleTable")
    .getElementsByTagName("tbody")[0];
  const newRow = table.rows[0].cloneNode(true);

  //reset values
  newRow
    .querySelectorAll("input, select")
    .forEach((input) => (input.value = ""));
  table.appendChild(newRow);

  reindexRows();
};

removeRow = (btn) => {
  const row = btn.closest("tr");
  const tbody = row.parentNode;
  if (tbody.rows.length > 1) {
    row.remove();
    reindexRows();
  }
};
