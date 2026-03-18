# 🏢 Company ERP

> Enterprise Resource Planning System — Java 21 | Spring Boot 3.2 | DDD + Hexagonal Architecture

---

## 🧠 Architecture

Ce projet suit une **Architecture Hexagonale (Ports & Adapters)** couplée au **Domain-Driven Design (DDD)** et à un **CQRS léger**.

```
Présentation → Application → Domain ← Infrastructure
```

Chaque module est **autonome** et structuré en 4 couches strictes :

| Couche | Contenu |
|--------|---------|
| `domain/` | Aggregates, Entities, Value Objects, Domain Events, Repository interfaces |
| `application/` | Services, Commands, Queries, DTOs, Mappers |
| `infrastructure/` | JPA repositories, Specifications, Event adapters |
| `presentation/` | REST Controllers |

---

## 📦 Modules

| Module | Description |
|--------|-------------|
| `auth` | Authentification JWT, RBAC, gestion utilisateurs |
| `organization` | Départements, postes, historique |
| `hr` | Employés, contrats, congés, présences, salaires |
| `finance` | Factures, paiements, dépenses |
| `inventory` | Matériels, affectations |
| `education` | Programmes, étudiants, enseignants, inscriptions, notes |
| `dashboard` | Statistiques read-only via projections |

---

## 🚀 Démarrage rapide

### Prérequis
- Java 21+
- Maven 3.9+
- Docker & Docker Compose

### Avec Docker Compose

```bash
# Cloner le projet
git clone https://github.com/company/erp.git
cd erp

# Copier les variables d'environnement
cp .env.example .env
# Éditer .env avec vos valeurs

# Démarrer (PostgreSQL + Application)
docker-compose up -d

# Vérifier les logs
docker-compose logs -f erp-app
```

L'API sera disponible sur : http://localhost:8080/api

### En local (dev)

```bash
# Démarrer PostgreSQL uniquement (optionnel - Neon DB par défaut)
docker-compose up -d postgres

# Compiler et lancer (corrige l'erreur Maven)
./mvnw clean spring-boot:run

# Avec profil spécifique
./mvnw clean spring-boot:run -Dspring.profiles.active=dev

# ❌ Commande incorrecte qui causait l'erreur:
# mvn run spring:boot  ← PAS ÇA!
```


---

## 🔐 Sécurité

- **JWT Access Token** : 15 minutes
- **JWT Refresh Token** : 7 jours avec rotation automatique
- **BCrypt** pour le hachage des mots de passe
- **Account lock** après 5 tentatives échouées
- **RBAC** granulaire via `@PreAuthorize`

---

## 📖 Documentation API

Swagger UI disponible (en mode dev) :
```
http://localhost:8080/api/swagger-ui.html
```

---

## 🧪 Tests

```bash
# Tests unitaires
./mvnw test

# Tests d'intégration (Testcontainers)
./mvnw verify -P integration-tests

# Rapport de couverture (JaCoCo)
./mvnw verify
open target/site/jacoco/index.html
```

---

## 📁 Structure du projet

```
com.company.erp
├── config/           — Configurations Spring
├── security/         — JWT, RBAC, Filters
├── shared/           — BaseEntity, Audit, Exceptions, Responses
└── modules/
    ├── auth/
    ├── organization/
    ├── hr/
    ├── finance/
    ├── inventory/
    ├── education/
    └── dashboard/
```

---

## 🛠 Stack technique

| Technologie | Version |
|-------------|---------|
| Java | 21 |
| Spring Boot | 3.2.5 |
| Spring Security | 6.x |
| PostgreSQL | 16 |
| Flyway | 10.x |
| MapStruct | 1.5.5 |
| JJWT | 0.12.5 |
| Testcontainers | 1.19.8 |
| Springdoc OpenAPI | 2.5.0 |

---

## 🚨 Dépannage rapide

| Erreur | Solution |
|--------|----------|
| `No plugin found for prefix 'spring'` | `mvn spring-boot:run` (pas `spring:boot`) |
| `Flyway: Unable to connect` | Vérifier `DB_URL` ou `docker-compose up postgres` |
| `Port 8080 already in use` | `killall java` ou changer `SERVER_PORT` |

## 📋 Variables d'environnement

| Variable | Description | Défaut |
|----------|-------------|--------|
| `DB_URL` | URL JDBC PostgreSQL | Neon DB (application.yml) |
| `DB_USERNAME` | Utilisateur DB | `neondb_owner` |
| `DB_PASSWORD` | Mot de passe DB | — |
| `JWT_SECRET` | Clé secrète JWT | — |
| `CLOUDINARY_*` | Uploads Cloudinary | Configurés |

## 📜 Licence

Propriétaire — Company Internal Use Only

