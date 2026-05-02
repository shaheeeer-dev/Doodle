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

        let preview = doc.content;
        if (preview.length > 120) {
            preview = preview.substring(0, 120) + "...";
        }

        const titleElement = document.createElement("h3");
        titleElement.className = "result-title";
        titleElement.innerText = doc.title;

        titleElement.addEventListener("click", () => {
            window.location.href =
                "view.html?title=" + encodeURIComponent(doc.title) +
                "&content=" + encodeURIComponent(doc.content);
        });

        const snippet = document.createElement("p");
        snippet.className = "result-snippet";
        snippet.innerText = preview;

        div.appendChild(titleElement);
        div.appendChild(snippet);

        resultsBox.appendChild(div);
    });
}

loadResults();