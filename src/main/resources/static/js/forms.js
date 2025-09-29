const countrySelect = document.getElementById("country");
const citySelect = document.getElementById("city");

const getData = async (url) => {
  // Await the fetch call to get the Response object
  const res = await fetch(url);
  // Await the .json() call to get the parsed data
  const data = await res.json();

  // Now access the property on the fully resolved data object
  return data;
};

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
    console.error(error.message);
  }
};

const processCityData = async (countryName) => {
  try {
    const result = await getData(
      "https://countriesnow.space/api/v0.1/countries/states"
    );
    const data = result.data;

    // Find selected country
    const country = data.find((c) => c.name === countryName);

    // Clear previous cities
    citySelect.innerHTML = "<option value=''>--Seleccione ciudad--</option>";

    if (country && country.states) {
      const cities = country.states.map((s) => s.name);
      cities.forEach((city) => {
        const option = document.createElement("option");
        option.textContent = city;
        option.value = city;
        citySelect.appendChild(option);
      });
    } else {
      console.warn(`No states found for ${countryName}`);
    }
  } catch (error) {
    console.error(error.message);
  }
};

processCountriesData();

countrySelect.addEventListener("change", () => {
  const selectedCountry = countrySelect.value;
  if (selectedCountry) {
    processCityData(selectedCountry);
  }
});
