# Fiche de Révision — Spring Framework : Les Fondamentaux
### Préparation entretien | Profil C#/C++ avec bases Java solides

---

> **Objectif de cette fiche :** Comprendre la philosophie et le fonctionnement interne de Spring sans avoir à pratiquer. Savoir expliquer clairement ce qu'est Spring, comment il fonctionne, et démontrer une compréhension conceptuelle solide face à un interlocuteur technique.

---

## 1. Qu'est-ce que Spring ?

Spring est un **framework applicatif open-source pour Java**, créé par Rod Johnson en 2003. Il est aujourd'hui le standard de facto pour le développement d'applications Java d'entreprise.

Son idée fondatrice est simple mais puissante : **libérer le développeur de la gestion de l'infrastructure technique** (création des objets, gestion de leur cycle de vie, connexions aux bases de données, transactions, sécurité…) pour qu'il se concentre uniquement sur la logique métier.

### L'analogie avec C# / .NET

Pour un développeur C#, Spring joue un rôle comparable à ce que fait l'ensemble **.NET + ASP.NET Core + Entity Framework** combinés. Là où .NET fournit ces outils de manière intégrée dans la plateforme, Spring les apporte sous forme de modules indépendants et composables au-dessus de la JVM.

| Concept | Monde C# / .NET | Monde Java / Spring                          |
|---|---|----------------------------------------------|
| Conteneur IoC | `IServiceCollection` / `IServiceProvider` | `ApplicationContext` (Spring IoC Container)  |
| Injection de dépendances | `services.AddScoped<T>()` | `@RequiredArgsConstructor`                   |
| ORM | Entity Framework Core | Spring Data JPA (sur Hibernate)              |
| API REST | ASP.NET Core MVC | Spring MVC / Spring WebFlux                  |
| Configuration | `appsettings.json` + `IConfiguration` | `application.properties` / `application.yml` |
| Auto-configuration | Minimal APIs / Startup conventions | Spring Boot Auto-Configuration               |

---

## 2. Les deux piliers philosophiques de Spring

### 2.1 L'Inversion de Contrôle (IoC)

En programmation classique, **c'est votre code qui crée ses dépendances** :

```java
// Sans Spring : le développeur contrôle tout
public class RiskCalculator {
    private final MarketDataService service;

    public RiskCalculator() {
        this.service = new MarketDataService(); // couplage fort, non testable
    }
}
```

Avec l'Inversion de Contrôle, **c'est le framework qui contrôle la création et l'assemblage des objets**. Votre code ne crée plus rien — il déclare ce dont il a besoin, et Spring le lui fournit.

> **Définition :** L'IoC est un principe selon lequel le contrôle du flux d'exécution et de la création des objets est délégué au framework plutôt qu'au code applicatif.

### 2.2 L'Injection de Dépendances (DI)

La DI est la **mise en œuvre concrète de l'IoC**. Spring injecte automatiquement les dépendances dont un objet a besoin, sans que cet objet ait à les créer lui-même.

```java
// Avec Spring : Spring injecte automatiquement MarketDataService
@RequiredArgsConstructor
public class RiskCalculator {
    private final MarketDataService service;
}
```

> **Analogie C# :** C'est exactement le même principe que `IServiceCollection.AddScoped<IRiskCalculator, RiskCalculator>()` avec injection via le constructeur dans ASP.NET Core.

## 3. Le Conteneur Spring : l'`ApplicationContext`

Le cœur de Spring est le **conteneur IoC**, représenté par l'interface `ApplicationContext`. C'est lui qui :

1. **Lit la configuration** (annotations, fichiers XML ou classes Java `@Configuration`)
2. **Instancie les objets** (appelés **Beans** dans le vocabulaire Spring)
3. **Injecte les dépendances** entre ces objets
4. **Gère leur cycle de vie** (initialisation, destruction)

```
┌─────────────────────────────────────────────────────┐
│                  ApplicationContext                 │
│                                                     │
│   ┌──────────┐    ┌──────────┐    ┌──────────────┐  │
│   │  Bean A  │───▶│  Bean B  │───▶│    Bean C    │  │
│   │(Service) │    │  (Repo)  │    │ (DataSource) │  │
│   └──────────┘    └──────────┘    └──────────────┘  │
│                                                     │
│   Gestion : instanciation, injection, cycle de vie  │
└─────────────────────────────────────────────────────┘
```

### Qu'est-ce qu'un Bean ?

Un **Bean Spring** est simplement un objet Java dont le cycle de vie est géré par le conteneur Spring. Ce n'est pas une classe spéciale — c'est n'importe quelle classe Java annotée de façon à être reconnue par Spring.

### Les annotations de déclaration de Beans

| Annotation | Usage | Équivalent sémantique |
|---|---|---|
| `@Component` | Annotation générique pour tout Bean Spring | Classe gérée par Spring |
| `@Service` | Bean de la couche service (logique métier) | Identique à `@Component` + intention sémantique |
| `@Repository` | Bean de la couche d'accès aux données | `@Component` + gestion des exceptions JPA |
| `@Controller` | Bean de la couche présentation (MVC) | `@Component` + gestion des requêtes HTTP |
| `@RestController` | Combinaison de `@Controller` + `@ResponseBody` | Retourne directement du JSON/XML |
| `@Configuration` | Classe de configuration Spring | Déclare des Beans via des méthodes `@Bean` |

> **À retenir :** `@Service`, `@Repository` et `@Controller` sont des **spécialisations** de `@Component`. Elles sont fonctionnellement équivalentes mais apportent une intention sémantique claire et, pour `@Repository`, un comportement supplémentaire (traduction des exceptions de persistance).

---

## 4. Les Scopes des Beans Spring

Le **scope** d'un Bean définit **combien d'instances** Spring va créer et **quelle est leur durée de vie**. C'est l'un des concepts les plus importants à maîtriser.

### 4.1 Les scopes fondamentaux

#### Singleton (scope par défaut)

```java
@Component
// équivalent explicite :
@Scope("singleton")
public class RiskEngineService {
    // ...
}
```

Spring ne crée **qu'une seule instance** de ce Bean pour l'ensemble du contexte applicatif. Toutes les classes qui demandent ce Bean reçoivent **la même instance**.

- **Durée de vie :** Toute la durée de vie de l'application
- **Cas d'usage :** Services stateless (sans état), repositories, utilitaires, calculateurs de risque
- **Attention :** Un Bean singleton ne doit **pas contenir d'état mutable** partagé entre threads (risque de race condition)

> **Analogie C# :** Équivalent de `services.AddSingleton<T>()` dans ASP.NET Core.

#### Prototype

```java
@Component
@Scope("prototype")
public class TradeContext {
    private String tradeId;
    // ...
}
```

Spring crée une **nouvelle instance à chaque fois** que ce Bean est demandé au conteneur.

- **Durée de vie :** Jusqu'à ce que l'instance ne soit plus référencée (GC)
- **Cas d'usage :** Objets avec état, contextes de calcul spécifiques à une opération
- **Attention :** Spring ne gère **pas la destruction** des Beans prototype (contrairement au singleton)

> **Analogie C# :** Équivalent de `services.AddTransient<T>()` dans ASP.NET Core.

### 4.2 Les scopes Web (contexte Spring MVC / Web)

Ces scopes ne sont disponibles que dans une application web Spring.

| Scope | Annotation | Durée de vie | Cas d'usage |
|---|---|---|---|
| **Request** | `@Scope("request")` | Une requête HTTP | Données spécifiques à une requête (ex: contexte utilisateur courant) |
| **Session** | `@Scope("session")` | Une session HTTP | Panier, préférences utilisateur |
| **Application** | `@Scope("application")` | Durée de vie du `ServletContext` | Données partagées entre toutes les sessions |
| **WebSocket** | `@Scope("websocket")` | Durée d'une connexion WebSocket | Contexte de connexion temps réel |

### 4.3 Tableau récapitulatif des scopes

| Scope | Instances créées | Durée de vie | Thread-safe ? |
|---|---|---|---|
| `singleton` | 1 par contexte | Durée de l'application | ⚠️ À gérer manuellement |
| `prototype` | 1 par demande | Jusqu'au GC | ✅ Oui (chaque thread a sa propre instance) |
| `request` | 1 par requête HTTP | Durée de la requête | ✅ Oui |
| `session` | 1 par session HTTP | Durée de la session | ⚠️ À gérer si accès concurrent |
| `application` | 1 par `ServletContext` | Durée de l'application | ⚠️ Comme singleton |

---

## 5. Spring Boot : la simplification de Spring

### Le problème que Spring Boot résout

Spring Framework est très puissant mais historiquement **verbeux à configurer**. Avant Spring Boot, démarrer un projet Spring nécessitait des dizaines de lignes de configuration XML ou Java pour déclarer le contexte, configurer la datasource, le gestionnaire de transactions, etc.

**Spring Boot** (lancé en 2014) résout ce problème avec trois mécanismes clés :

### 5.1 L'Auto-Configuration

Spring Boot **détecte automatiquement** les bibliothèques présentes dans le classpath et configure automatiquement les Beans correspondants.

> **Exemple concret :** Si Spring Boot détecte `spring-data-jpa` et un driver de base de données dans le classpath, il configure automatiquement un `DataSource`, un `EntityManagerFactory` et un gestionnaire de transactions — sans que vous écriviez une seule ligne de configuration.

Ce mécanisme repose sur l'annotation `@EnableAutoConfiguration` (incluse dans `@SpringBootApplication`) et des fichiers de configuration `spring.factories` / `AutoConfiguration.imports` qui listent les classes d'auto-configuration à activer conditionnellement.

### 5.2 Les Starters

Les **starters** sont des dépendances Maven/Gradle pré-packagées qui regroupent toutes les bibliothèques nécessaires pour une fonctionnalité donnée.

| Starter | Ce qu'il apporte |
|---|---|
| `spring-boot-starter-web` | Spring MVC, Tomcat embarqué, Jackson (JSON) |
| `spring-boot-starter-data-jpa` | Spring Data JPA, Hibernate, gestion des transactions |
| `spring-boot-starter-security` | Spring Security (authentification, autorisation) |
| `spring-boot-starter-test` | JUnit 5, Mockito, Spring Test |
| `spring-boot-starter-actuator` | Endpoints de monitoring (health, metrics, info) |

### 5.3 Le Serveur Embarqué

Spring Boot embarque directement un serveur web (Tomcat par défaut, ou Jetty/Undertow) dans le JAR de l'application. L'application se lance donc comme un simple programme Java (`java -jar app.jar`) sans nécessiter de déploiement sur un serveur d'application externe.

### 5.4 L'annotation centrale : `@SpringBootApplication`

```java
@SpringBootApplication
public class RiskApplication {
    public static void main(String[] args) {
        SpringApplication.run(RiskApplication.class, args);
    }
}
```

`@SpringBootApplication` est une méta-annotation qui combine trois annotations :

| Annotation incluse | Rôle |
|---|---|
| `@SpringBootConfiguration` | Déclare cette classe comme source de configuration Spring (variante de `@Configuration`) |
| `@EnableAutoConfiguration` | Active le mécanisme d'auto-configuration de Spring Boot |
| `@ComponentScan` | Demande à Spring de scanner le package courant et ses sous-packages pour trouver les Beans (`@Component`, `@Service`, etc.) |

---

## 6. Le Contexte Spring : `ApplicationContext` en détail

### 6.1 Les types de contextes

Spring propose plusieurs implémentations de `ApplicationContext` selon le contexte d'exécution :

| Implémentation | Usage |
|---|---|
| `AnnotationConfigApplicationContext` | Application standalone basée sur des annotations Java |
| `ClassPathXmlApplicationContext` | Application configurée via XML (approche historique) |
| `AnnotationConfigServletWebServerApplicationContext` | Application web Spring Boot (créé automatiquement) |
| `AnnotationConfigReactiveWebServerApplicationContext` | Application web réactive (Spring WebFlux) |

> **En pratique avec Spring Boot :** Le bon type de contexte est créé et démarré automatiquement par `SpringApplication.run()`. Le développeur n'a généralement pas à s'en préoccuper.

### 6.2 Le cycle de vie du contexte

```
1. Démarrage de SpringApplication.run()
       │
       ▼
2. Création de l'ApplicationContext
       │
       ▼
3. Scan des composants (@ComponentScan)
   Lecture des @Configuration
       │
       ▼
4. Instanciation des Beans (dans l'ordre des dépendances)
       │
       ▼
5. Injection des dépendances (@Autowired)
       │
       ▼
6. Appel des méthodes @PostConstruct
   (initialisation personnalisée)
       │
       ▼
7. Application prête à recevoir des requêtes
       │
       ▼
8. À l'arrêt : appel des méthodes @PreDestroy
   (nettoyage des ressources)
```

### 6.3 Le cycle de vie d'un Bean

| Étape | Mécanisme | Équivalent C# |
|---|---|---|
| Instanciation | Spring appelle le constructeur | `new` géré par le conteneur DI |
| Injection des dépendances | `@Autowired` résolu | Injection via constructeur dans `IServiceProvider` |
| Initialisation | Méthode annotée `@PostConstruct` | `IHostedService.StartAsync()` |
| Utilisation | Bean disponible dans le contexte | — |
| Destruction | Méthode annotée `@PreDestroy` | `IDisposable.Dispose()` |

```java
@Service
public class MarketDataCacheService {

    @PostConstruct
    public void init() {
        // Appelé après l'injection des dépendances
        // Idéal pour charger un cache initial, ouvrir des connexions
        System.out.println("Cache initialisé");
    }

    @PreDestroy
    public void cleanup() {
        // Appelé avant la destruction du Bean (arrêt de l'application)
        // Idéal pour fermer des connexions, vider des ressources
        System.out.println("Cache nettoyé");
    }
}
```

---

## 7. La Configuration Spring : trois approches

### 7.1 Configuration par annotations (approche moderne — standard actuel)

```java
@Configuration
public class AppConfig {

    @Bean
    public RiskCalculator riskCalculator(MarketDataService marketDataService) {
        return new RiskCalculator(marketDataService);
    }
}
```

### 7.2 Configuration par fichier `application.properties` / `application.yml`

Spring Boot lit automatiquement le fichier `src/main/resources/application.properties` (ou `.yml`). Ces propriétés permettent de configurer tous les aspects de l'application sans modifier le code.

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/risk_db
    username: risk_user
    password: secret
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false

server:
  port: 8080
```

Ces propriétés peuvent être injectées dans les Beans via `@Value` ou `@ConfigurationProperties` :

```java
@Component
public class RiskEngineConfig {

    @Value("${risk.engine.max-threads:10}")
    private int maxThreads; // Valeur par défaut : 10

    @Value("${spring.datasource.url}")
    private String dbUrl;
}
```

### 7.3 Les Profils Spring

Les **profils** permettent d'avoir des configurations différentes selon l'environnement (développement, test, production) :

```java
@Configuration
@Profile("production")
public class ProductionDataSourceConfig {
    @Bean
    public DataSource dataSource() {
        // Configuration pour la production
    }
}

@Configuration
@Profile("dev")
public class DevDataSourceConfig {
    @Bean
    public DataSource dataSource() {
        // Base de données H2 en mémoire pour le développement
    }
}
```

Activation d'un profil :
```bash
java -jar app.jar --spring.profiles.active=production
```

---

## 8. Spring dans le contexte du Calcul de Risque de Contrepartie

Dans un projet de **calcul de risque de contrepartie** (CVA, DVA, XVA, PFE…), Spring joue typiquement les rôles suivants :

| Composant | Rôle de Spring |
|---|---|
| **Moteur de calcul** | `@Service` singleton orchestrant les calculs (CVA Engine, Exposure Calculator) |
| **Accès aux données de marché** | `@Repository` avec Spring Data JPA pour récupérer les courbes de taux, les prix de marché |
| **Pipeline de calcul** | `@Component` représentant chaque étape du pipeline (pricing, agrégation, netting) |
| **API d'exposition des résultats** | `@RestController` pour exposer les résultats de risque via des endpoints REST |
| **Configuration des paramètres** | `@ConfigurationProperties` pour les paramètres du modèle (dates de simulation, grilles de scénarios) |
| **Gestion des transactions** | `@Transactional` pour garantir l'atomicité des écritures en base (résultats de calcul) |

### Architecture typique d'un service de risque avec Spring

```
┌─────────────────────────────────────────────────────────────┐
│                    Spring ApplicationContext                │
│                                                             │
│  ┌──────────────────┐    ┌──────────────────────────────┐   │
│  │ @RestController  │    │       @Service               │   │
│  │ RiskApiController│───▶│  CounterpartyRiskService     │   │
│  └──────────────────┘    └──────────────────────────────┘   │
│                                        │                    │
│                           ┌────────────┼────────────┐       │
│                           ▼            ▼            ▼       │
│                    ┌─────────┐  ┌─────────┐  ┌──────────┐   │
│                    │@Service │  │@Service │  │@Repo     │   │
│                    │CVAEngine│  │PFECalc  │  │MarketData│   │
│                    └─────────┘  └─────────┘  └──────────┘   │
│                                                    │        │
│                                             ┌──────────┐    │
│                                             │ Database │    │
│                                             └──────────┘    │
└─────────────────────────────────────────────────────────────┘
```

---

## 9. Questions d'entretien fréquentes — Spring Fondamentaux

### Q1 : Quelle est la différence entre Spring et Spring Boot ?

**Réponse attendue :** Spring est le framework de base (IoC, DI, AOP, Data, MVC…). Spring Boot est une surcouche qui simplifie la configuration de Spring via l'auto-configuration, les starters et le serveur embarqué. Spring Boot ne remplace pas Spring — il l'utilise et le configure automatiquement.

### Q2 : Qu'est-ce qu'un Bean Spring ?

**Réponse attendue :** Un Bean est un objet Java dont le cycle de vie (instanciation, injection des dépendances, initialisation, destruction) est géré par le conteneur IoC de Spring (`ApplicationContext`). Il est déclaré via `@Component` (ou ses spécialisations) ou via une méthode `@Bean` dans une classe `@Configuration`.

### Q3 : Quelle est la différence entre `@Component`, `@Service` et `@Repository` ?

**Réponse attendue :** Fonctionnellement, ces trois annotations sont des spécialisations de `@Component` et ont le même effet sur la détection par Spring. La différence est sémantique : `@Service` indique une classe de logique métier, `@Repository` indique une classe d'accès aux données (et active en plus la traduction des exceptions de persistance en `DataAccessException`). Utiliser la bonne annotation améliore la lisibilité et l'intention du code.

### Q4 : Quel est le scope par défaut d'un Bean Spring ? Pourquoi est-ce important ?

**Réponse attendue :** Le scope par défaut est `singleton`. Cela signifie qu'une seule instance est partagée dans tout le contexte applicatif. C'est important car un Bean singleton ne doit pas contenir d'état mutable partagé entre threads, sous peine de race conditions. Dans un contexte de calcul de risque multi-threadé (comme avec Spark), il faut être particulièrement vigilant à ce que les services Spring soient stateless.

### Q5 : Qu'est-ce que l'auto-configuration de Spring Boot ?

**Réponse attendue :** L'auto-configuration est un mécanisme par lequel Spring Boot détecte les bibliothèques présentes dans le classpath et configure automatiquement les Beans correspondants. Par exemple, si `spring-data-jpa` est détecté, Spring Boot configure automatiquement la datasource, l'EntityManager et le gestionnaire de transactions. Ce mécanisme repose sur des classes de configuration conditionnelles (`@ConditionalOnClass`, `@ConditionalOnMissingBean`) listées dans `AutoConfiguration.imports`.

### Q6 : Quelle est la différence entre `@Autowired` par constructeur et par champ ?

**Réponse attendue :** L'injection par constructeur est recommandée car elle garantit l'immuabilité des dépendances (champs `final`), facilite les tests unitaires (on peut instancier la classe directement sans Spring), et rend les dépendances explicites. L'injection par champ (`@Autowired` directement sur l'attribut) est déconseillée car elle masque les dépendances, empêche l'utilisation de `final`, et rend les tests plus difficiles.

---

## 10. Résumé — Ce qu'il faut absolument retenir

| Concept | Ce qu'il faut savoir dire |
|---|---|
| **Spring** | Framework Java d'entreprise basé sur l'IoC et la DI. Gère le cycle de vie des objets (Beans) via un conteneur (`ApplicationContext`). |
| **Spring Boot** | Surcouche de Spring qui auto-configure l'application selon les dépendances détectées. Simplifie le démarrage via les starters et le serveur embarqué. |
| **Bean** | Tout objet Java géré par le conteneur Spring. Déclaré via `@Component`, `@Service`, `@Repository`, `@Controller` ou `@Bean`. |
| **IoC / DI** | Le développeur déclare ses dépendances, Spring les crée et les injecte. Couplage faible, testabilité accrue. |
| **Scope singleton** | Une seule instance par contexte (défaut). Doit être stateless en environnement multi-threadé. |
| **Scope prototype** | Nouvelle instance à chaque demande. Pour les objets avec état. |
| **`@PostConstruct`** | Méthode d'initialisation appelée après l'injection des dépendances. |
| **`@PreDestroy`** | Méthode de nettoyage appelée avant la destruction du Bean. |
| **`@SpringBootApplication`** | Point d'entrée d'une application Spring Boot. Combine `@Configuration` + `@EnableAutoConfiguration` + `@ComponentScan`. |
| **Profils** | Permettent d'avoir des configurations différentes par environnement (`dev`, `prod`, `test`). |

---

*Fiche rédigée pour une préparation rapide à un entretien technique Java/Spring — Calcul de Risque de Contrepartie*
