const titleInput = document.getElementById("titleInput");
const contentInput = document.getElementById("contentInput");
const form = document.getElementById("docForm");

if (form) {
    form.addEventListener("submit", async function (e) {
        e.preventDefault();
        const titleField = titleInput.value.trim();
        const contentField = contentInput.value.trim();

        if (titleField === "" || contentField === "") {
            alert("Fill both fields!");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/add", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    title: titleField,
                    content: contentField
                })
            });

           if (!response.ok) {
                const err = await response.json().catch(() => ({}));
                alert("Server error: " + (err.error || response.status));
                return;
            }

            alert("Document added!");
            titleInput.value = "";
            contentInput.value = "";

        } catch (error) {
            alert("Could not reach the server. Is the backend running on port 8080?");
            console.error(error);
        }
    });
}
