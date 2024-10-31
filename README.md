[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/QlRjSf1d)
# Workout app
### Kecskemeti Eduard-Alexandru

## Descriere
Workout App

Aplicația urmărește să ajute utilizatorul în urmărirea progresului de-a lungul antrenamentelor la sală, aplicația va include: 

1. Interfață pentru Monitorizarea Progresului
Funcționalități: Creează o pagină principală unde utilizatorul își poate înregistra și vizualiza progresul pentru fiecare exercițiu.
Design: Oferă un tabel simplu cu câmpuri pentru numărul de seturi, repetări și greutate, și un grafic de progres care să arate îmbunătățirile de-a lungul timpului.
Stocare: Utilizează o bază de date  în care să salvezi toate datele antrenamentului, pentru a permite utilizatorului să le acceseze ulterior.
2. Tracker pentru Macronutrienți și Calorii
Calcule și Algoritmi: Aplică o formulă  pentru a calcula necesarul caloric de întreținere al utilizatorului, ajustat pentru obiectivul său de creștere/scădere în greutate.
Bază de Date pentru Alimente: Integrează o bază de date de alimente pentru a ușura introducerea de mâncăruri .
Funcționalități: Permite utilizatorului să introducă alimentele și cantitățile consumate, iar aplicația va calcula automat cantitatea de proteine, carbohidrați și grăsimi.
3. Sugestii de Programe de Antrenament
Antrenamente Standard și Personalizate: Oferă utilizatorului mai multe opțiuni, cum ar fi antrenamente full-body, push-pull-legs, sau upper-lower split. Pentru fiecare tip, poți predefini exercițiile și structura.
Interfață Personalizabilă: Adaugă o opțiune prin care utilizatorul poate să își aleagă un program sau să creeze unul de la zero.
4. Monitorizarea Grupei Musculare Antrenate Săptămânal
Analiză Săptămânală: Creează o funcționalitate de sumar săptămânal, care să arate numărul total de seturi pentru fiecare grupă musculară, bazat pe datele introduse.
Vizualizare: Folosește grafice  pentru a arăta volumul de lucru pe fiecare grupă musculară și permite utilizatorului să urmărească variația volumului de la săptămână la săptămână.
5. Informații pentru Execuția Corectă a Exercițiilor
Text și Video: Pentru fiecare exercițiu, oferă explicații scrise despre tehnica corectă. În plus, poți include o secțiune video 
Sfaturi și Avertismente:  adăuga mici sfaturi și indicații despre ce greșeli să evite utilizatorul (ex.: „Nu bloca coatele la finalul mișcării” pentru împinsul cu bara).
6. Sistem de Achievements
Tipuri de Realizări: Creează un sistem prin care utilizatorul poate debloca diverse „insigne” sau „medalii” pentru atingerea unor milestone-uri, cum ar fi:
14 zile consecutive de antrenament
un record personal la greutate ridicată (de ex., genuflexiuni cu 100kg)
un număr total de seturi executate (ex.: 1000 de seturi).
Notificări și Motivație: Fiecare achievement deblocat poate genera o notificare motivațională care să încurajeze utilizatorul să continue progresul.

## Obiective
Aplicația urmărește să rezolve problema ..

* ob1
* ob2
* ob3
    - sob31
    - sob32
    - ...
* ....

## Arhitectura
Lorem ipsum ...

![Alt text](documentatie-ghid-utlizare-raport/diagrama-clase.png)

Lorem ipsum ...

## Functionalitati/Exemple utilizare
Ecranul de Login/Înregistrare

Prima utilizare: Utilizatorul își creează un cont printr-un ecran de înregistrare, unde introducere detalii personale de bază (nume, vârstă, greutate, înălțime, nivel de activitate).
Autentificare: După înregistrare, utilizatorul poate să se autentifice cu contul creat pentru a accesa toate funcționalitățile aplicației.
Scop: Acest pas permite personalizarea planurilor și a recomandărilor pe baza caracteristicilor individuale ale fiecărui utilizator.

Dashboard-ul Principal

Overview: După autentificare, utilizatorul este întâmpinat de un Dashboard unde vede un rezumat al progresului, inclusiv antrenamentele recente, un sumar săptămânal al seturilor și grupelor musculare lucrate și o vizualizare a progresului caloric și al macronutrienților.
Navigație rapidă: Dashboard-ul include butoane către funcționalitățile principale: „Progres Antrenament”, „Planuri de Antrenament”, „Macronutrienți” și „Achievements”.

Secțiunea de Progres Antrenament

Adăugare Antrenament: Utilizatorul poate crea o sesiune de antrenament nouă. Poate alege exercițiile dorite, introduce numărul de seturi, repetări și greutatea folosită pentru fiecare exercițiu.
Vizualizare Progres: Aici, utilizatorul poate vedea un grafic al progresului în timp pentru fiecare exercițiu individual. Graficele pot fi de tip linie (pentru creșterea greutății), bară (pentru numărul de seturi), și pot include o comparație pe ultimele săptămâni.
Analiză Detaliată: În această secțiune, utilizatorul poate filtra exercițiile în funcție de grupă musculară (piept, spate, picioare etc.) și să vadă detalii ale progresului pentru fiecare dintre acestea.

Tracker pentru Macronutrienți și Calorii

Setarea Caloriilor Zilnice: Utilizatorul poate vedea aici recomandarea de calorii și macronutrienți, bazată pe scopul de creștere/scădere în greutate și activitatea sa.
Înregistrare Alimente: Utilizatorul poate adăuga alimentele consumate și cantitățile acestora, iar aplicația calculează automat caloriile și nivelurile de macronutrienți.
Progress Bar Zilnic: Există un progres vizual sub forma unei bare de progres care indică câte proteine, carbohidrați și grăsimi a consumat utilizatorul în acea zi față de target-ul său.

Sugestii de Programe de Antrenament

Explorare și Alegere Plan: Utilizatorul poate explora programele de antrenament disponibile (ex.: push-pull-legs, full-body) și să selecteze un program preferat. Fiecare program include o listă de exerciții recomandate pentru fiecare zi de antrenament.
Personalizare Plan: Utilizatorul poate adapta programul predefinit, adăugând sau eliminând exerciții în funcție de preferințele sale.

Monitorizarea Grupelor Musculare Antrenate Săptămânal

Sumar Săptămânal: La finalul fiecărei săptămâni, utilizatorul poate vedea un sumar care arată volumul total de lucru pentru fiecare grupă musculară.
Detalii Volum și Recomandări: Utilizatorul primește o evaluare generală a volumului de lucru per grupă musculară, alături de recomandări pentru a echilibra antrenamentele în caz de sub sau suprasolicitare a unei grupe.

Execuția Corectă a Exercițiilor

Selecția Exercițiului: Când un exercițiu este ales în timpul unui antrenament, utilizatorul poate vedea informații detaliate despre execuția corectă a acestuia.
Informații vizuale și scrise: Această secțiune poate include scurte clipuri video și indicații scrise despre cum să execute mișcarea corect, greșelile comune și beneficiile exercițiului respectiv.


Sistem de Achievements

Secțiunea de Realizări: Utilizatorul poate vedea o listă de milestones și achievements posibile 
Notificări la Finalizarea Achievements: Când un achievement este atins , utilizatorul primește o notificare și o insignă vizibilă pe profil.


### Resurse
Markdown Guide, [Online] Available: https://www.markdownguide.org/basic-syntax/ [accesed: Mar 14, 1706]
