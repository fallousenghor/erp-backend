# 🌪️ Tornadoes Job ERP

> **Enterprise Resource Planning** — Système de Gestion d'Entreprise Complet  
> **Java 17+** | **Spring Boot 3.2** | **PostgreSQL** | **Architecture Hexagonale + DDD**

---

## 📋 Table des Matières

- [Vue d'ensemble](#vue-densemble)
- [Architecture](#architecture)
- [Modules](#modules)
- [Base de données](#base-de-données)
- [Démarrage rapide](#démarrage-rapide)
- [Utilisateurs de test](#utilisateurs-de-test)
- [API Documentation](#api-documentation)
- [Développement](#développement)
- [Déploiement](#déploiement)

---

## 🎯 Vue d'ensemble

**Tornadoes Job ERP** est une application de gestion d'entreprise complète et simplifiée, conçue pour être :

- ✅ **Complète** : Tous les modules essentiels pour gérer une entreprise
- ✅ **Simple** : Interface intuitive, logique métier claire
- ✅ **Moderne** : Stack technique à jour, bonnes pratiques
- ✅ **Évolutive** : Architecture modulaire, facile à étendre

### Fonctionnalités Principales

| Domaine | Fonctionnalités |
|---------|----------------|
| 👥 **Ressources Humaines** | Employés, Départements, Présences, Congés, Performance |
| 💰 **Finance** | Factures Clients/Fournisseurs, Paiements, Dépenses, Trésorerie |
| 🤝 **CRM & Ventes** | Contacts (Clients/Fournisseurs), Opportunités, Pipeline |
| 📦 **Achats** | Commandes fournisseurs, Suivi des livraisons |
| 📊 **Inventaire** | Actifs, Affectations, Suivi du matériel |
| 🎓 **Formation** | Étudiants, Programmes, Inscriptions, Notes |
| 🗂️ **Projets** | Suivi de projets, Jalons, Équipes |
| 📄 **Documents** | Gestion documentaire, Versions, Approbations |
| ⚙️ **Administration** | Utilisateurs, Rôles, Permissions, Audit Logs |

---

## 🏗️ Architecture

### Architecture Hexagonale (Ports & Adapters)

```
┌─────────────────────────────────────────────────────────┐
│                   PRÉSENTATION                           │
│                    (REST API)                            │
└─────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────┐
│                   APPLICATION                            │
│              (Services, DTOs, Rules)                     │
└─────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────┐
│                     DOMAIN                               │
│         (Entities, Value Objects, Repository)            │
└─────────────────────────────────────────────────────────┘
                            ↑
┌─────────────────────────────────────────────────────────┐
│                  INFRASTRUCTURE                          │
│        (JPA, External Services, Events)                  │
└─────────────────────────────────────────────────────────┘
```

### Structure d'un Module

```
modules/<module-name>/
├── domain/
│   ├── model/          # Entities & Value Objects
│   ├── repository/     # Repository interfaces
│   └── event/          # Domain Events
├── application/
│   ├── service/        # Application Services
│   ├── dto/            # Data Transfer Objects
│   └── mapper/         # MapStruct Mappers
├── infrastructure/
│   ├── persistence/    # JPA Repositories & Entities
│   └── adapter/        # External adapters
└── presentation/
    └── controller/     # REST Controllers
```

---

## 📦 Modules

### 1. Auth & Utilisateurs
| Table | Description |
|-------|-------------|
| `users` | Utilisateurs avec authentification JWT |
| `roles` | Rôles avec permissions (JSON) |
| `user_roles` | Association utilisateurs-rôles |
| `refresh_tokens` | Tokens de rafraîchissement |

### 2. Organization
| Table | Description |
|-------|-------------|
| `departments` | Départements avec postes intégrés |

### 3. HR (Ressources Humaines)
| Table | Description |
|-------|-------------|
| `employees` | Employés, contrats, salaires |
| `leave_requests` | Demandes de congés |
| `attendances` | Présences et pointages |

### 4. Finance
| Table | Description |
|-------|-------------|
| `invoices` | Factures clients/fournisseurs (items en JSON) |
| `payments` | Paiements reçus/émis |
| `expenses` | Dépenses et notes de frais |

### 5. CRM
| Table | Description |
|-------|-------------|
| `contacts` | Clients, Fournisseurs, Partenaires, Prospects |
| `deals` | Opportunités commerciales (pipeline) |

### 6. Achats
| Table | Description |
|-------|-------------|
| `purchase_orders` | Commandes fournisseurs (items en JSON) |

### 7. Inventory
| Table | Description |
|-------|-------------|
| `assets` | Actifs/équipements de l'entreprise |
| `asset_assignments` | Historique des affectations |

### 8. Éducation
| Table | Description |
|-------|-------------|
| `students` | Étudiants/apprenants |
| `training_programs` | Programmes de formation (modules en JSON) |
| `enrollments` | Inscriptions avec notes (JSON) |

### 9. Projets & Documents
| Table | Description |
|-------|-------------|
| `projects` | Projets, jalons, équipes |
| `documents` | Documents, versions, approbations |
| `audit_logs` | Journal d'audit système |

---

## 💾 Base de données

### Schéma simplifié (18 tables)

```
┌──────────────────────────────────────────────┐
│  AUTH (4)        │  users, roles,            │
│                  │  user_roles, refresh_tokens│
├──────────────────────────────────────────────┤
│  ORG (1)         │  departments              │
├──────────────────────────────────────────────┤
│  HR (3)          │  employees, leave_requests,│
│                  │  attendances              │
├──────────────────────────────────────────────┤
│  FINANCE (3)     │  invoices, payments,       │
│                  │  expenses                 │
├──────────────────────────────────────────────┤
│  CRM (2)         │  contacts, deals          │
├──────────────────────────────────────────────┤
│  PURCHASES (1)   │  purchase_orders          │
├──────────────────────────────────────────────┤
│  INVENTORY (2)   │  assets, asset_assignments│
├──────────────────────────────────────────────┤
│  EDUCATION (3)   │  students, training_programs,│
│                  │  enrollments              │
├──────────────────────────────────────────────┤
│  OTHER (2)       │  projects, documents,      │
│                  │  audit_logs               │
└──────────────────────────────────────────────┘
```

### Migrations

Les migrations sont gérées avec **Flyway** et situées dans :
`src/main/resources/db/migration/`

| Migration | Description |
|-----------|-------------|
| `V1__auth_and_users.sql` | Auth, utilisateurs, rôles |
| `V2__organization.sql` | Départements |
| `V3__hr.sql` | Employés, congés, présences |
| `V4__finance.sql` | Factures, paiements, dépenses |
| `V5__crm.sql` | Contacts, deals |
| `V6__purchases.sql` | Commandes fournisseurs |
| `V7__inventory.sql` | Actifs |
| `V8__education.sql` | Étudiants, programmes |
| `V9__projects_documents.sql` | Projets, documents, audit |
| `V10__dashboard_views.sql` | Vues dashboard, seed data |

---

## 🚀 Démarrage rapide

### Prérequis

- **Java 17+**
- **Maven 3.9+**
- **PostgreSQL 16+** (ou Docker)
- **Node.js 18+** (pour le frontend)

### 1. Configuration de la base de données

#### Option A : Docker (Recommandé)

```bash
cd backend/company-erp
docker-compose up -d postgres
```

#### Option B : PostgreSQL local

```sql
CREATE DATABASE tornadoes_job_erp;
CREATE USER tornadoes_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE tornadoes_job_erp TO tornadoes_user;
```

### 2. Variables d'environnement

Créez un fichier `.env` à la racine du backend :

```env
# Database
DB_URL=jdbc:postgresql://localhost:5432/tornadoes_job_erp
DB_USERNAME=tornadoes_user
DB_PASSWORD=your_password

# JWT
JWT_SECRET=votre_secret_jwt_tres_long_et_secuise_123456789

# Cloudinary (optionnel, pour les uploads)
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret

# Server
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=dev
```

### 3. Lancer le backend

```bash
cd backend/company-erp
mvn clean install
mvn spring-boot:run
```

L'application sera disponible sur : **http://localhost:8080**

### 4. Lancer le frontend

```bash
cd frontend
npm install
npm run dev
```

L'application sera disponible sur : **http://localhost:5173**

---

## 👥 Utilisateurs de test

| Username | Password | Rôle | Permissions |
|----------|----------|------|-------------|
| `admin` | Admin@123 | ROLE_ADMIN | Accès complet |
| `hrmanager` | Test123! | ROLE_HR_MANAGER | RH, Employés, Congés |
| `finance` | Test123! | ROLE_FINANCE | Factures, Paiements, Dépenses |
| `manager` | Test123! | ROLE_MANAGER | Projets, Équipes |
| `user` | Test123! | ROLE_USER | Lecture seule |

---

## 📚 API Documentation

### Swagger / OpenAPI

Une fois l'application lancée, accédez à la documentation interactive :

- **Swagger UI** : http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON** : http://localhost:8080/api/v3/api-docs

### Endpoints principaux

```
POST   /api/v1/auth/login          # Connexion
POST   /api/v1/auth/refresh        # Rafraîchir token
POST   /api/v1/auth/logout         # Déconnexion

GET    /api/v1/employees           # Liste employés
POST   /api/v1/employees           # Créer employé
PUT    /api/v1/employees/{id}      # Modifier employé

GET    /api/v1/invoices            # Liste factures
POST   /api/v1/invoices           # Créer facture

GET    /api/v1/contacts            # Liste contacts
POST   /api/v1/deals               # Créer opportunité

GET    /api/v1/dashboard/stats     # Statistiques dashboard
```

---

## 🛠️ Développement

### Structure du projet

```
backend/company-erp/
├── src/main/java/com/company/erp/
│   ├── ErpApplication.java      # Point d'entrée
│   ├── config/                   # Configuration
│   ├── security/                 # Sécurité (JWT, RBAC)
│   ├── shared/                   # Utilitaires communs
│   └── modules/                  # Modules métier
│       ├── auth/
│       ├── organization/
│       ├── hr/
│       ├── finance/
│       ├── crm/
│       ├── purchases/
│       ├── inventory/
│       ├── education/
│       ├── projects/
│       └── documents/
├── src/main/resources/
│   ├── application.yml           # Configuration principale
│   ├── db/migration/             # Migrations Flyway
│   └── templates/                # Templates email
└── src/test/                     # Tests unitaires & intégration
```

### Tests

```bash
# Tests unitaires
mvn test

# Tests avec couverture
mvn clean verify

# Lancer avec Docker
docker-compose up --build
```

---

## 📊 Données de démo

L'application inclut des données de démo complètes :

- **6 départements** prédéfinis
- **3 employés** de démo
- **3 factures** clients
- **3 dépenses**
- **6 contacts** (clients/fournisseurs)
- **2 opportunités** (deals)
- **2 commandes** fournisseurs
- **5 actifs**
- **3 programmes** de formation
- **4 étudiants**
- **3 projets**
- **3 documents**

---

## 🔒 Sécurité

### JWT Configuration

| Paramètre | Valeur |
|-----------|--------|
| Access Token | 15 minutes |
| Refresh Token | 7 jours |
| Algorithme | HS256 |
| Password Hash | BCrypt (strength 12) |

### RBAC (Role-Based Access Control)

Les permissions sont stockées en JSON dans la table `roles` :

```json
["employee:*", "department:read", "leave:*", "dashboard:read"]
```

### Rate Limiting

- **Login** : 100 requêtes/minute
- **API** : 1000 requêtes/minute

---

## 🚢 Déploiement

### Docker

```bash
# Build
docker build -t tornadoes-job-erp .

# Run
docker run -p 8080:8080 tornadoes-job-erp
```

### Docker Compose (Production)

```bash
docker-compose -f docker-compose.prod.yml up -d
```

### Variables d'environnement (Production)

```env
SPRING_PROFILES_ACTIVE=prod
DB_URL=jdbc:postgresql://host:5432/tornadoes_job_erp
DB_USERNAME=prod_user
DB_PASSWORD=<strong_password>
JWT_SECRET=<very_long_secret>
```

---

## 📝 License

Copyright © 2024 Tornadoes Job. Tous droits réservés.

---

## 📞 Support

Pour toute question ou problème :
- 📧 Email: support@tornadoesjob.com
- 📚 Documentation: https://docs.tornadoesjob.com

---

**Développé avec ❤️ par l'équipe Tornadoes Job**
