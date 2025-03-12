# **Esercizio Gestionale con Spring Boot, JPA, Hibernate, MySQL, REST API e Thymeleaf**

## **Obiettivo**
L'obiettivo dell'esercizio è realizzare una **web application gestionale** utilizzando **Spring Boot**, **JPA con Hibernate**, **MySQL** come database, **REST API**, **Thymeleaf** per la parte visuale e **Spring Dependency Injection** per permettere l'estendibilità del codice senza modificarlo.  

L'applicazione deve gestire **due entità principali** e fornire un'**interfaccia REST e un'interfaccia web separata**, con l'obiettivo di organizzare al meglio la logica di business nei servizi e utilizzare `@Qualifier` per gestire **implementazioni multiple** di un'interfaccia.

---

## **📌 Requisiti Tecnici**
- **Spring Boot**
- **Spring Data JPA con Hibernate**
- **MySQL come database relazionale**
- **Spring Web (Controller REST e Thymeleaf)**
- **Service Layer per la logica di business**
- **Repository Layer con Spring Data JPA**
- **`@Qualifier` per selezionare implementazioni di un'interfaccia**
- **Dependency Injection per modularità**
- **Configurazione nel file `application.properties`**
- **Gestione degli errori con `@ExceptionHandler`**
- **Validazione degli input con `@Valid` e `@NotBlank`**

---

## **📌 Entità del Gestionale**
L'applicazione dovrà gestire due entità:  
- **Cliente** (Clienti registrati nel sistema)
- **Ordine** (Ordini effettuati da un cliente)

Ogni **Cliente** può avere più **Ordini**, quindi esiste una relazione **uno-a-molti** tra Cliente e Ordine.

### **Struttura delle Entità**
#### **Cliente**
- `id` (PK, Long, auto-generato)
- `nome` (String, obbligatorio, minimo 2 caratteri)
- `email` (String, obbligatorio, formato email)
- `telefono` (String, opzionale)

#### **Ordine**
- `id` (PK, Long, auto-generato)
- `cliente` (FK, relazione ManyToOne con Cliente)
- `dataOrdine` (LocalDate, default oggi)
- `importo` (BigDecimal, obbligatorio)
- `stato` (Enum: **IN_ELABORAZIONE, SPEDITO, CONSEGNATO**)

---

## **📌 Struttura del Progetto**
L'architettura deve seguire il pattern **MVC** e separare i livelli:

📂 `com.example.gestionale`
- 📂 `controller`
  - `ClienteController` (Gestisce API REST per i clienti)
  - `OrdineController` (Gestisce API REST per gli ordini)
- 📂 `service`
  - `ClienteService` (Interfaccia)
  - `ClienteServiceImpl` (Implementazione predefinita)
  - `OrdineService` (Interfaccia)
  - `OrdineServiceImpl` (Implementazione predefinita)
  - `OrdineServicePriorityImpl` (**Seconda implementazione con `@Qualifier`**)
- 📂 `repository`
  - `ClienteRepository` (Estende `JpaRepository`)
  - `OrdineRepository` (Estende `JpaRepository`)
- 📂 `model`
  - `Cliente` (Entità JPA)
  - `Ordine` (Entità JPA)
  - `StatoOrdine` (Enum per stato dell'ordine)
- 📂 `config`
  - `DatabaseConfig` (Configurazione connessione a MySQL)
- 📂 `templates`
  - `clienti.html` (Lista clienti con Thymeleaf)
  - `ordini.html` (Lista ordini con Thymeleaf)

---

## **📌 Implementazione dei Controller**
1. **ClienteController** (Gestisce API REST per Cliente)
   - `GET /api/clienti` → Restituisce tutti i clienti in formato JSON
   - `GET /api/clienti/{id}` → Restituisce un cliente per ID
   - `POST /api/clienti` → Crea un nuovo cliente (Validazione `@Valid`)
   - `PUT /api/clienti/{id}` → Aggiorna un cliente esistente
   - `DELETE /api/clienti/{id}` → Elimina un cliente se non ha ordini

2. **OrdineController** (Gestisce API REST per Ordine)
   - `GET /api/ordini` → Restituisce tutti gli ordini in formato JSON
   - `GET /api/ordini/{id}` → Restituisce un ordine per ID
   - `POST /api/ordini` → Crea un nuovo ordine (Validazione `@Valid`)
   - `PUT /api/ordini/{id}/stato` → Modifica lo stato di un ordine
   - `DELETE /api/ordini/{id}` → Elimina un ordine

---

## **📌 Service Layer con `@Qualifier` per Gestire più Implementazioni**
L'**OrdineService** avrà due implementazioni, e useremo `@Qualifier` per scegliere quale usare.

1. **OrdineServiceImpl** → Implementazione base
2. **OrdineServicePriorityImpl** → Implementazione alternativa con priorità sugli ordini urgenti

Nel **controller** e nel **service**, si dovrà iniettare l'implementazione corretta usando:
#java
@Autowired
@Qualifier("ordineServicePriority")
private OrdineService ordineService;
#/java

In questo modo, possiamo **cambiare il comportamento dell'applicazione senza modificare il codice**, semplicemente cambiando `@Qualifier`.

---

## **📌 Dependency Injection per Aggiornare una Feature Senza Modificare il Codice**
### **Obiettivo**
L'applicazione deve poter **cambiare il comportamento del calcolo del prezzo degli ordini** **senza modificare il codice**.

- Creare un'interfaccia `PrezzoStrategy`
- Implementare due classi:
  - `PrezzoStandardStrategy` (Prezzo normale)
  - `PrezzoScontatoStrategy` (Prezzo con sconto del 10% per clienti fedeli)

- Iniettare la strategia nel `Service`:
#java
@Autowired
private PrezzoStrategy prezzoStrategy;
#/java

- Usare `@Primary` per scegliere quale implementazione è attiva **di default**, e usare `@Qualifier` per cambiarla se necessario.

---

## **📌 Thymeleaf: Interfaccia Web**
Creare due pagine:
1. `clienti.html` → Mostra i clienti in tabella con pulsanti "Modifica" e "Elimina"
2. `ordini.html` → Mostra gli ordini, con selezione per modificare lo stato

**Bonus:**
- Aggiungere un **form HTML per creare nuovi ordini**.
- Aggiungere un filtro per mostrare solo gli ordini "SPEDITI".

---

## **📌 Configurazione Database in `application.properties`**
Il database deve essere configurato in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestionale_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
message.txt
6 KB