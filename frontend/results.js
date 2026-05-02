const resultsBox = document.getElementById("resultsBox");
const queryText = document.getElementById("queryText");

const params = new URLSearchParams(window.location.search);
const query = params.get("q");

queryText.innerText = "Results for: " + query;

async function loadResults() {
    const response = await fetch("http://localhost:8080/search?q=" + encodeURIComponent(query));
    const data = await response.json();

    resultsBox.innerHTML = "";

    if (data.length === 0) {
        resultsBox.innerHTML = "<p>No results found</p>";
        return;
    }

    data.forEach(doc => {
    const div = document.createElement("div");
    div.className = "result-item";

    // make short preview (2 lines approx)
    let preview = doc.content;
    if (preview.length > 120) {
        preview = preview.substring(0, 120) + "...";
    }

    div.innerHTML = `
        <h3 class="result-title" data-id="${doc.id}">
            ${doc.title}
        </h3>
        <p class="result-snippet">${preview}</p>
    `;

    // click on title → open full document
    div.querySelector(".result-title").addEventListener("click", () => {
        window.location.href = "document.html?id=" + doc.id;
    });

    resultsBox.appendChild(div);
});
}

loadResults();