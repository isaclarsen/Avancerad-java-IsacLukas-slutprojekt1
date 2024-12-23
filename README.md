
# Todo-List Management

## Projektetes Mål och funktionalitet
Syftet med detta projekt är att skapa en enkel applikation för att hantera uppgifter (ToDo-List) som gör det möjligt för användare att:

-  Visa en lista över alla uppgifter. 
- Lägga till nya uppgifter.
- Uppdatera befintliga uppgifter.
- Ta bort specifika uppgifter. 

## Intruktioner för Start

### Krav

- Java 23 eller senare
- Maven
- JavaFX SDK

### Hur man kör programmet:

#### Starta Servern

1. Klona detta repository: https://github.com/isaclarsen/Avancerad-java-IsacLukas-slutprojekt1.git
2. Navigera till serverprojekts rotmapp.
3. Kör följande kommando: "mvn spring-boot: run"

#### Starta Frontend-applikationen
1. Navigera till frontendprojektets rotmapp.
2. Kör huvudklassen för javaFX-applikationen.
3. Redo att anvädas.









## API Reference

#### Get all tasks

```http
  GET /api/tasks
```

- Alla tasks visas redan upp i tabellen.

#### POST task

```http
  POST /api/items
```
- Fyll i titel, Description och id-fälten och tryck sedan på ADD knappen. 

#### PUT task


```http
 PUT /api/items/{id}
```
- Dubbelklicka på en textrad i tabellen för att redigera vad som står, tryck sedan på Enter knappen på tangentbordet, tryck sedan på UPDATE knappen i programmet. 


#### DELETE task


```http 
 DELETE /api/items/{id}
```


- Klicka på en rad i tabellen och klicka sedan på DELETE knappen för att radera en task.



