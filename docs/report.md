# Gestionale - Report Tecnico della Codebase

## Panoramica del Progetto

Il progetto "Gestionale" è un'applicazione web sviluppata con Spring Boot che implementa un sistema di gestione aziendale. L'applicazione permette la gestione di clienti e ordini attraverso un'interfaccia web (Thymeleaf) e un'API REST. Il sistema è strutturato per essere facilmente estensibile e configurabile grazie all'uso di pattern di design e tecniche di dependency injection.

## Scelte di Design

### Architettura Multi-livello

L'applicazione segue un'architettura multi-livello che separa chiaramente le responsabilità:

1. **Livello di Presentazione**: Implementato attraverso controller REST e controller Thymeleaf
2. **Livello di Business Logic**: Implementato nei servizi con interfacce ben definite
3. **Livello di Accesso ai Dati**: Implementato tramite repository Spring Data JPA

### Pattern di Design Implementati

1. **Strategy Pattern**: Utilizzato per la gestione dei calcoli di prezzo con l'interfaccia `PrezzoStrategy`
2. **Dependency Injection**: Utilizzo estensivo di DI per accoppiamento debole tra componenti
3. **Repository Pattern**: Implementato mediante Spring Data JPA
4. **MVC Pattern**: Separazione tra Model (entità), View (Thymeleaf templates), e Controller

### Caratteristiche di Scalabilità

- **Qualifiers**: Uso di `@Qualifier` per gestire implementazioni multiple dello stesso servizio
- **Separazione Interfaccia/Implementazione**: Permette modifiche dell'implementazione senza impattare i client

## Analisi Dettagliata dei File

### Configurazione dell'Applicazione

#### GestionaleApplication.java

**Funzionalità**: Classe principale che avvia l'applicazione Spring Boot.

**Implementazione**:

```java
@SpringBootApplication
public class GestionaleApplication {
    public static void main(String[] args) {
        SpringApplication.run(GestionaleApplication.class, args);
    }
}
```

**Utilizzo**: Punto di ingresso che configura il contesto Spring e avvia il server incorporato.

### Componenti di Accesso ai Dati

#### OrdineRepository.java

**Funzionalità**: Interfaccia per l'accesso ai dati degli ordini nel database.

**Implementazione**:

```java
public interface OrdineRepository extends JpaRepository<Ordine, Long> {
    int countByClienteId(Long clienteid);
}
```

**Utilizzo**: Fornisce metodi CRUD predefiniti e il metodo personalizzato per contare gli ordini di un cliente.

#### ClienteRepository.java (inferito)

**Funzionalità**: Interfaccia per l'accesso ai dati dei clienti.

**Implementazione**: Estende JpaRepository per operazioni CRUD sui clienti.

**Utilizzo**: Utilizzato dal ClienteService per operazioni sui clienti.

### Servizi di Business Logic

#### ClienteServiceImpl.java

**Funzionalità**: Implementa la logica di business per la gestione dei clienti.

**Implementazione**:

```java
@Service
public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepository clienteRepository;
    private final OrdineRepository ordineRepository;
    
    // Costruttore per DI
    
    // Metodi implementati:
    // - findAll(): recupera tutti i clienti
    // - findById(Long id): trova un cliente specifico
    // - addCliente(Cliente cliente): aggiunge un nuovo cliente
    // - updateCliente(Long id, Cliente clienteUp): aggiorna un cliente
    // - deleteCliente(Long id): elimina un cliente
}
```

**Utilizzo**: Fornisce funzionalità di business per i controller che gestiscono i clienti.

#### OrdineServiceImpl.java

**Funzionalità**: Implementa la logica di business per la gestione degli ordini.

**Implementazione**:

```java
@Service
@Qualifier("ordineServiceBase")
public class OrdineServiceImpl implements OrdineService {
    private final OrdineRepository ordineRepository;
    private final PrezzoStrategy prezzoStrategy;
    
    // Costruttore per DI con qualificatore per la strategia di prezzo
    
    // Metodi implementati:
    // - getAll(): recupera tutti gli ordini
    // - getOne(Long id): trova un ordine specifico
    // - addOrdine(Ordine ordine): aggiunge un nuovo ordine
    // - updateStatoOrdine(Long id, StatoOrdine newStatus): aggiorna lo stato
    // - delete(Long id): elimina un ordine
    // - countByClienteId(Long id): conta gli ordini di un cliente
}
```

**Utilizzo**:

- Implementa l'interfaccia OrdineService
- Utilizza una strategia di prezzo per calcolare l'importo degli ordini
- Viene qualificato come "ordineServiceBase" per permettere implementazioni alternative

### API Controllers

#### ClienteControllerAPI.java

**Funzionalità**: Espone endpoint REST per la gestione dei clienti.

**Implementazione**:

```java
@RestController
@RequestMapping("/api/clienti")
public class ClienteControllerAPI {
    private final ClienteService clienteService;
    
    // Costruttore per DI
    
    // Endpoint REST:
    // - GET /api/clienti: recupera tutti i clienti
    // - GET /api/clienti/{id}: recupera un cliente specifico
    // - POST /api/clienti: crea un nuovo cliente
    // - PUT /api/clienti/{id}: aggiorna un cliente
    // - DELETE /api/clienti/{id}: elimina un cliente
}
```

**Utilizzo**: Fornisce un'API RESTful per applicazioni client che necessitano di accesso programmatico ai dati dei clienti.

#### OrdineControllerAPI.java

**Funzionalità**: Espone endpoint REST per la gestione degli ordini.

**Implementazione**:

```java
@RestController
@RequestMapping("/api/ordini")
public class OrdineControllerAPI {
    private final OrdineService ordineServiceBase;
    private final OrdineService ordineServiceScontato;
    private final ClienteService clienteService;
    
    // Costruttore per DI con qualificatori
    
    // Metodo privato per selezione del servizio appropriato in base al numero ordini
    private OrdineService getOrdineService(Long clienteId) {
        int numeroOrdini = ordineServiceBase.countByClienteId(clienteId);
        return numeroOrdini > 5 ? ordineServiceScontato : ordineServiceBase;
    }
    
    // Endpoint REST:
    // - GET /api/ordini: recupera tutti gli ordini
    // - GET /api/ordini/{id}: recupera un ordine specifico
    // - POST /api/ordini: crea un nuovo ordine
    // - PUT /api/ordini/{id}/stato: aggiorna lo stato di un ordine
    // - DELETE /api/ordini/{id}: elimina un ordine
}
```

**Utilizzo**:

- Fornisce un'API RESTful per la gestione degli ordini
- Seleziona automaticamente la strategia di prezzo appropriata (base o scontata) in base al numero di ordini del cliente

### Web Controllers

#### ClienteControllerThymeLeaf.java

**Funzionalità**: Controller per la UI web Thymeleaf per la gestione dei clienti.

**Implementazione**:

```java
@Controller
@RequestMapping("/clienti")
public class ClienteControllerThymeLeaf {
    private final ClienteService clienteService;
    
    // Costruttore per DI
    
    // Metodi handler:
    // - GET /clienti: mostra tutti i clienti
    // - GET /clienti/modifica/{id}: mostra form di modifica
    // - POST /clienti/modifica/{id}: processa la modifica
    // - GET /clienti/elimina/{id}: elimina un cliente
    // - POST /clienti/nuovo: aggiunge un nuovo cliente
}
```

**Utilizzo**: Gestisce le richieste web per la gestione dei clienti tramite un'interfaccia HTML.

#### OrdineControllerThymeLeaf.java

**Funzionalità**: Controller per la UI web Thymeleaf per la gestione degli ordini.

**Implementazione**:

```java
@Controller
@RequestMapping("/ordini")
public class OrdineControllerThymeLeaf {
    private final OrdineService ordineServiceBase;
    private final OrdineService ordineServiceScontato;
    private final ClienteService clienteService;
    
    // Costruttore per DI con qualificatori
    
    // Metodo per scegliere il servizio appropriato
    private OrdineService getOrdineService(Long clienteId) {
        int numeroOrdini = ordineServiceBase.countByClienteId(clienteId);
        return numeroOrdini > 5 ? ordineServiceScontato : ordineServiceBase;
    }
    
    // Metodi handler:
    // - GET /ordini: mostra tutti gli ordini
    // - GET /ordini/{id}: mostra dettagli ordine
    // - GET /ordini/new: mostra form nuovo ordine
    // - POST /ordini: processa nuovo ordine
}
```

**Utilizzo**:

- Gestisce le richieste web per la UI degli ordini
- Seleziona il servizio appropriato in base al numero di ordini del cliente

### Pattern Strategy per il Calcolo dei Prezzi

#### PrezzoStrategy.java (inferito)

**Funzionalità**: Interfaccia per strategie di calcolo del prezzo.

**Implementazione**:

```java
public interface PrezzoStrategy {
    BigDecimal calcolaPrezzo(Ordine ordine);
}
```

**Utilizzo**: Definisce un contratto per diverse strategie di calcolo del prezzo.

#### PrezzoStandardStrategy.java (inferito)

**Funzionalità**: Implementazione standard della strategia di prezzo.

**Implementazione**: Calcola il prezzo senza sconti.

**Utilizzo**: Utilizzato per clienti regolari.

#### PrezzoScontatoStrategy.java (inferito)

**Funzionalità**: Implementazione con sconto della strategia di prezzo.

**Implementazione**: Calcola il prezzo con sconto per clienti fedeli.

**Utilizzo**: Utilizzato per clienti che hanno effettuato più di 5 ordini.

## Modelli di Dominio

### Cliente.java (inferito)

**Funzionalità**: Rappresenta l'entità cliente nel sistema.

**Implementazione**:

```java
@Entity
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Il nome è obbligatorio")
    @Size(min = 2, message = "Il nome deve avere almeno 2 caratteri")
    private String nome;
    
    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Formato email non valido")
    private String email;
    
    private String telefono;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Ordine> ordini;
    
    // Getters e setters
}
```

**Utilizzo**: Rappresenta i clienti nel sistema con validazione dei campi.

### Ordine.java (inferito)

**Funzionalità**: Rappresenta l'entità ordine nel sistema.

**Implementazione**:

```java
@Entity
public class Ordine {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    private LocalDate dataOrdine = LocalDate.now();
    
    @NotNull(message = "L'importo è obbligatorio")
    private BigDecimal importo;
    
    @Enumerated(EnumType.STRING)
    private StatoOrdine stato = StatoOrdine.IN_ELABORAZIONE;
    
    // Getters e setters
}
```

**Utilizzo**: Rappresenta gli ordini effettuati dai clienti.

### StatoOrdine.java (inferito)

**Funzionalità**: Enum che rappresenta lo stato di un ordine.

**Implementazione**:

```java
public enum StatoOrdine {
    IN_ELABORAZIONE, SPEDITO, CONSEGNATO
}
```

**Utilizzo**: Utilizzato per tracciare lo stato di avanzamento degli ordini.

## Configurazione del Database

### application.properties

**Funzionalità**: Configura la connessione al database e le proprietà di Hibernate.

**Implementazione**:

```properties
spring.application.name=gestionale

# Configuration MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/gestionale
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Usa Hibernate per gestire le tabelle automaticamente
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

**Utilizzo**: Configura il sistema per utilizzare un database MySQL locale.

## Interfaccia Web

### clienti.html

**Funzionalità**: Template Thymeleaf per la visualizzazione e gestione dei clienti.

**Implementazione**:

- Visualizza la lista dei clienti in tabella
- Fornisce pulsanti per modifica ed eliminazione
- Include un form per l'aggiunta di nuovi clienti

**Utilizzo**: Interfaccia utente per la gestione dei clienti.

### ordini.html

**Funzionalità**: Template Thymeleaf per la visualizzazione e gestione degli ordini.

**Implementazione**:

- Visualizza la lista degli ordini in tabella
- Usa colori diversi per i vari stati dell'ordine
- Permette di modificare lo stato dell'ordine

**Utilizzo**: Interfaccia utente per la gestione degli ordini.

## Conclusione

Il progetto Gestionale presenta una struttura ben organizzata che segue i principi di buona progettazione del software. L'architettura multi-livello, l'uso di pattern di design come Strategy e il pattern Repository facilitano l'estensibilità e la manutenibilità del codice. La separazione tra API REST e interfaccia web consente un utilizzo versatile del sistema, sia come backend per applicazioni mobile/web sia come applicazione standalone.

L'implementazione di strategy pattern attraverso l'interfaccia PrezzoStrategy e l'uso di qualificatori per selezionare implementazioni diverse dei servizi dimostra un'ottima comprensione dei principi di progettazione SOLID, in particolare del principio di Open/Closed e del principio di inversione delle dipendenze.
