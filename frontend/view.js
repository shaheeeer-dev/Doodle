const params = new URLSearchParams(window.location.search);

const title = params.get("title");
const content = params.get("content");

const docTitle = document.getElementById("docTitle");
const docContent = document.getElementById("docContent");

docTitle.innerText = title || "No Title";
docContent.innerText = content || "No Content";