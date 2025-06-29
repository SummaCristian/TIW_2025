# Tecnologie Informatiche per il Web

## 🇮🇹 Italiano

### 📃 Specifiche
Realizzare un'applicazione web che consente la gestione di aste online.
Il sito è composto da due pagine, una per monitorare lo stato delle proprie aste vendute e per crearne di nuove, l'altra per cercare aste di altre utenti a cui poter far offerte.

Implementare il progetto in due versioni:
1. Versione HTML pura
2. Versione RIA

Per la versione RIA è richiesto che tutto, escluso il login, avvenga all'interno di una singola pagina web.

### 🛠️ Tecnologie impiegate

[![HTML](https://img.shields.io/badge/HTML-%23E34F26.svg?logo=html5&logoColor=white)](#)
[![CSS](https://img.shields.io/badge/CSS-639?logo=css&logoColor=fff)](#)
[![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?logo=javascript&logoColor=000)](#)
[![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?logo=openjdk&logoColor=white)](#)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.0-005F0F?logo=thymeleaf&logoColor=white)

La prima versione del sito è implementata unicamente tramite HTML, CSS e Java per le Servlet. Tutte le richiesti di aggiornamento producono il rendering di una nuova pagina a lato Server e l'invio al client mediante redirect o forward.
Per il rendering delle pagine si fa uso di Thymeleaf.

Per la seconda versione si è fatto uso di HTML, CSS, Java per le Servlet e JavaScript per il controllo a lato client.
JavaScript viene utilizzato per:
- Navigazione tra le varie sezioni dell'interfaccia
- Controllo e validazione a lato client dei dati prima di inviare le richieste, con eventual gestione locale degli errori
- Fetch dei dati dal Server con richieste asincrone e aggiornamento dell'interfaccia senza il ricaricamento dell'intera pagina.

Entrambe le versioni basano il proprio Backend su un Database MySQL.

[Qui](https://github.com/SummaCristian/TIW_2025/blob/main/Documents/Database%20design.pdf) è possibile visionare il progetto del Database.

[Qui](https://github.com/SummaCristian/TIW_2025/blob/main/Project/Database%20scripts/Create_Database.sql) è possibile scaricare lo script di creazione del Database.

[Qui](https://github.com/SummaCristian/TIW_2025/blob/main/Project/Database%20scripts/Fill_Database.sql) invece è presente uno script che riempie il Database con dei dati fittizi. A questi dati vanno abbinate delle immagini, presenti nel [seguente zip](https://github.com/SummaCristian/TIW_2025/blob/main/Project/item_images.zip). 

La cartella in cui le immagini sono posizionate va specificata all'interno del file .env di ognuna delle due versioni del progetto.

### 📑 Documentazione
La documentazione completa del progetto è consultabile [qui](https://github.com/SummaCristian/TIW_2025/blob/main/Documents/Documentazione.pdf)

### 🔬 Testing
Il progetto è stato testato sui seguenti browser

[![Google Chrome](https://img.shields.io/badge/Google%20Chrome-4285F4?logo=GoogleChrome&logoColor=white)](#)
[![Microsoft Edge](https://custom-icon-badges.demolab.com/badge/Microsoft%20Edge-2771D8?logo=edge-white&logoColor=white)](#)
[![Arc](https://img.shields.io/badge/Arc-FCBFBD?logo=arc&logoColor=000)](#)
[![Safari](https://img.shields.io/badge/Safari-006CFF?logo=safari&logoColor=fff)](#)
[![Zen Browser](https://img.shields.io/badge/Zen%20Browser-F76F53?logo=zenbrowser&logoColor=fff)](#)

## 🇬🇧 English

### 📃 Specifications
Build a web application that allows users to handle online auctions.
The website consists of two pages, one to monitor the auctions sold by the user and to create new ones, the other to search for auctions from other users to send offers to.

Implement the website in two versions:
1. Pure HTML version
2. RIA version

For the RIA version it's required that everything, login excluded, happens inside a single web page.

### 🛠️ Technologies used

[![HTML](https://img.shields.io/badge/HTML-%23E34F26.svg?logo=html5&logoColor=white)](#)
[![CSS](https://img.shields.io/badge/CSS-639?logo=css&logoColor=fff)](#)
[![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?logo=javascript&logoColor=000)](#)
[![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?logo=openjdk&logoColor=white)](#)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.0-005F0F?logo=thymeleaf&logoColor=white)

The first version of the website is implemented with just HTML, CSS and Java for Servlets. All update requests result in the server-side rendering of a new page that is then sent to the user via either redirect or forward.
Page rendering is done using Thymeleaf.

The second version made use of HTML, CSS, Java for Servlets and JavaScript for client-side control. 
JavaScript is used for:
- Navigation between the various sections of the UI
- Client-side check and validation of data before sending the requests, with possible local handling of errors
- Fetching data from the Server with async requests and UI refresh without reloading the whole page.

Both versions base their Backend on a MySQL database.

[Here](https://github.com/SummaCristian/TIW_2025/blob/main/Documents/Database%20design.pdf) it's possible to see the Database's design.

[Here](https://github.com/SummaCristian/TIW_2025/blob/main/Project/Database%20scripts/Create_Database.sql) it's possible to download the Database creation script.

[Here](https://github.com/SummaCristian/TIW_2025/blob/main/Project/Database%20scripts/Fill_Database.sql) there is a script that fills the Database with some fictious data. 
These must be bundled with some images, which can be found in the [following zip](https://github.com/SummaCristian/TIW_2025/blob/main/Project/item_images.zip). 

The folder in which the images are located must be specified inside the .env file for each version of the project.

### 📑 Documentation
The complete documentation for the project can be found [here](https://github.com/SummaCristian/TIW_2025/blob/main/Documents/Documentation.pdf)

### 🔬 Testing
The project has been tested on the following browsers

[![Google Chrome](https://img.shields.io/badge/Google%20Chrome-4285F4?logo=GoogleChrome&logoColor=white)](#)
[![Microsoft Edge](https://custom-icon-badges.demolab.com/badge/Microsoft%20Edge-2771D8?logo=edge-white&logoColor=white)](#)
[![Arc](https://img.shields.io/badge/Arc-FCBFBD?logo=arc&logoColor=000)](#)
[![Safari](https://img.shields.io/badge/Safari-006CFF?logo=safari&logoColor=fff)](#)
[![Zen Browser](https://img.shields.io/badge/Zen%20Browser-F76F53?logo=zenbrowser&logoColor=fff)](#)

## 🖼️ Screenshots

### Pure HTML
<div align="center">
  <img alt="Login page" src="https://github.com/SummaCristian/TIW_2025/blob/main/Screenshots/Pure%20HTML/LoginPage.png" width="350" />
  <img alt="Sell page" src="https://github.com/SummaCristian/TIW_2025/blob/main/Screenshots/Pure%20HTML/SellPage.png" width="350" />
  <img alt="Sell page 2" src="https://github.com/SummaCristian/TIW_2025/blob/main/Screenshots/Pure%20HTML/SellPage2.png" width="350" />
  <img alt="Buy page" src="https://github.com/SummaCristian/TIW_2025/blob/main/Screenshots/Pure%20HTML/BuyPage.png" width="350" />
  <img alt="Auction Details page" src="https://github.com/SummaCristian/TIW_2025/blob/main/Screenshots/Pure%20HTML/AuctionDetailsPage.png" width="400" />
</div>

### RIA
<div align="center">
  <img alt="Login page" src="https://github.com/SummaCristian/TIW_2025/blob/main/Screenshots/RIA/LoginPage.png" width="350" />
  <img alt="Sell page" src="https://github.com/SummaCristian/TIW_2025/blob/main/Screenshots/RIA/SellPage.png" width="350" />
  <img alt="Sell page 2" src="https://github.com/SummaCristian/TIW_2025/blob/main/Screenshots/RIA/SellPage2.png" width="350" />
  <img alt="Buy page" src="https://github.com/SummaCristian/TIW_2025/blob/main/Screenshots/RIA/BuyPage.png" width="350" />
  <img alt="Auction Details page" src="https://github.com/SummaCristian/TIW_2025/blob/main/Screenshots/RIA/AuctionDetailsPage.png" width="400" />
</div>
