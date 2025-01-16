# **Nom du Projet**

Une application web développée avec **Spring Boot** et **Thymeleaf**, connectée à une base de données **PostgreSQL** et administrée via **pgAdmin**. Ce projet vise à offrir une gestion intuitive de diverses entités avec une interface claire et des opérations robustes.

---

## **Table des Matières**

1. [Fonctionnalités](#fonctionnalités)
2. [Technologies Utilisées](#technologies-utilisées)
3. [Pré-requis](#pré-requis)
4. [Installation](#installation)
5. [Structure du Projet](#structure-du-projet)
6. [Configuration](#configuration)
7. [Tests](#tests)
8. [Contribution](#contribution)
9. [Auteur](#auteur)

---

## **Fonctionnalités**

- Gestion des entités principales :
    - **Clients** : Ajout, modification, suppression, et consultation des clients.
    - **Mouvements** : Suivi des mouvements associés.
    - **Règles** : Gestion des différentes règles et leurs relations.
    - **Problèmes** : Rapport des problèmes liés aux opérations.
- Templates **Thymeleaf** personnalisés pour une navigation fluide.
- Tests unitaires et d’intégration complets pour garantir la fiabilité.

---

## **Technologies Utilisées**

### **Backend**
- **Framework** : Spring Boot 
- **ORM** : Hibernate JPA
- **Tests** : JUnit 5, Mockito

### **Frontend**
- **Template Engine** : Thymeleaf


### **Base de Données**
- **PostgreSQL** : Base de données relationnelle
- **pgAdmin** : Outil de gestion et d’administration

---

## **Pré-requis**

- **Java** : Version 17 ou supérieure
- **Maven** : Version 3.x
- **PostgreSQL** : Version 16
- **pgAdmin** : Installé et configuré (optionnel)

---

## **Installation**

### 1. **Cloner le dépôt**
```bash
git clone https://github.com/Margauxdo/buncker_TS.git
cd buncker_TS
```

### 2. **Configurer la base de données**
- Créez une base de données PostgreSQL nommée `projet_db`.
- Configurez les informations de connexion dans le fichier `application.properties` :
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/projet_db
spring.datasource.username=VOTRE_UTILISATEUR
spring.datasource.password=VOTRE_MOT_DE_PASSE
```

### 3. **Lancer l’application**
```bash
mvn spring-boot:run
```
Accédez à l’application via [http://localhost:8080](http://localhost:8080).

---

## **Structure du Projet**

```plaintext
src/
├── main/
│   ├── java/
│   │   ├── controllers/      # Contrôleurs REST et MVC
│   │   ├── entities/         # Entités JPA
│   │   ├── services/         # Services métiers
│   │   └── repositories/     # Interfaces des répertoires JPA
│   └── resources/
│       ├── templates/        # Templates Thymeleaf
│       └── application.properties # Configuration de l’application
└── test/
    ├── java/
    │   ├── unit/             # Tests unitaires
    │   └── integration/      # Tests d’intégration
    └── resources/
```

---

## **Configuration**

Assurez-vous de personnaliser les paramètres suivants dans `application.properties` :

### **Profil de développement**
```properties
spring.profiles.active=dev
```

### **Connexion à la base de données**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/projet_db
spring.datasource.username=VOTRE_UTILISATEUR
spring.datasource.password=VOTRE_MOT_DE_PASSE
```

---

## **Tests**

### **Lancer les tests**

```bash
mvn test
```

### **Types de tests inclus**

1. **Tests Unitaires** : Validations des services et des méthodes.
    - Exemple : `ClientServiceTest`

2. **Tests d’Intégration** : Validation des opérations complètes entre les couches.
    - Exemple : `ClientControllerIntegrationTest`

3. **Base de données en mémoire** : Utilisation de H2 pour les tests.
    - Configuration spécifique dans `application-test.properties`.

---

## **Contribution**

1. Forkez le dépôt
2. Créez une branche :
   ```bash
   git checkout -b feature/nom_fonctionnalite
   ```
3. Proposez vos modifications
4. Soumettez une pull request

---

## **Auteur**

**Margaux Doisy**  
[LinkedIn](https://www.linkedin.com/in/margaux-doisy-089a90b8/) | [Email](mailto:margauxdoisy@gmail.com)

---


