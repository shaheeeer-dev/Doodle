# Doodle – Mini Search Engine

![DOODLE](https://img.shields.io/badge/DOODLE-SEARCH%20ENGINE-000000?style=flat-square)<br>
![Java](https://img.shields.io/badge/Java-Backend-red)
![Frontend](https://img.shields.io/badge/Frontend-HTML%2FCSS%2FJS-blue)

---

## Overview

Doodle is a mini search engine built using Java (HttpServer) and a basic HTML, CSS, and JavaScript frontend. The main objective of this project is to apply core **Data Structures and Algorithms (DSA)** concepts in a practical system design scenario.

It implements an **inverted index-based information retrieval system**, where efficient searching is achieved using **HashMaps for constant-time lookups, tokenization for text processing, and frequency-based sorting for ranking results**. The project simulates how real-world search engines process and retrieve relevant documents from large text datasets.
The system allows users to:
- Add documents (title + content)
- Search documents using keywords
- Retrieve ranked results instantly

---

## Features

- Keyword-based search engine
- Fast retrieval using inverted index (HashMap-based)
- Document storage with title and content
- Ranked search results based on frequency
- REST-style API using Java HttpServer
- Frontend deployed on Netlify
- Backend deployed on Render

---

## Core Concepts Used

- Inverted Index (word → document IDs mapping)
- HashMap for fast lookup (O(1) average)
- ArrayList for dynamic storage
- HashSet for stopword filtering
- Sorting for ranking results
- String tokenization using regex

---

## Project Structure
```
Doodle/
│
├── backend/
│   ├── src/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── doodle/
│   │   │           ├── Document.java
│   │   │           ├── InvertedIndex.java
│   │   │           ├── SearchEngine.java
│   │   │           └── Main.java
│   │   └── resources/
│   │       ├── stopwords.txt
│   │       └── config.properties
│   └── test/...
│  
├── frontend/
│   ├── index.html
│   ├── addDoc.html
│   ├── results.html
│   ├── view.html
│   ├── index.js
│   ├── addDoc.js
│   ├── results.js
│   ├── view.js
│   └── style.css
│
├── data/
│   └── data.ser
│
└── README.md
```

---

## How It Works

1. User adds a document with title and content
2. Backend tokenizes the text into words
3. Words are stored in an inverted index
4. On search:
    - Query is tokenized
    - Matching documents are retrieved
    - Results are ranked based on frequency
5. Frontend displays results dynamically

---

## Deployment

### Backend

* Hosted on Render
* Uses Docker + Java runtime

### Frontend

* Hosted on Netlify

---

## Future Improvements

* TF-IDF based ranking system
* Autocomplete search
* Typo correction system
* Database integration (MongoDB or PostgreSQL)
* Analytics dashboard

---

### Author

Muhammad Shaheer<br>
Software Engineering Undergrad<br>
Project focused on **DSA, backend systems** and **search engine fundamentals**