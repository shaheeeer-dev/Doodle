const input = document.getElementById("searchInput");
const searchBtn = document.getElementById("searchBtn");
const addBtn = document.getElementById("AddDocBtn");


function handleSearch() {
    const inputField = input.value.trim();
    if (inputField === "") return;
    window.location.href = "results.html?q=" + encodeURIComponent(inputField);
}

searchBtn.addEventListener("click", handleSearch);

input.addEventListener("keydown", function (e) {
    if (e.key === "Enter") handleSearch();
});

function handleDoc() {
    window.location.href = "addDoc.html";
}

addBtn.addEventListener("click", handleDoc);