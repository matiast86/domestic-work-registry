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
  const row = btn.colsest("tr");
  const tbody = row.parentNode;
  if (tbody.rows.length > 1) {
    row.remove();
    reindexRows();
  }
};
