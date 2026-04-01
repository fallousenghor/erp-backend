# 🌪️ TORNADOES JOB ERP

> **Enterprise Resource Planning** — Système de Gestion d'Entreprise Complet et Simplifié

[![Version](https://img.shields.io/badge/version-2.0.0-blue.svg)](https://github.com/tornadoesjob/erp)
[![Java](https://img.shields.io/badge/java-17+-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/spring%20boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/react-18.2-blue.svg)](https://reactjs.org/)
[![TypeScript](https://img.shields.io/badge/typescript-5.4.2-blue.svg)](https://www.typescriptlang.org/)

---

## 📋 Table des Matières

- [Vue d'ensemble](#vue-densemble)
- [Fonctionnalités](#fonctionnalités)
- [Architecture](#architecture)
- [Démarrage Rapide](#démarrage-rapide)
- [Documentation](#documentation)
- [Structure du Projet](#structure-du-projet)
- [Technologies](#technologies)
- [License](#license)

---

## 🎯 Vue d'ensemble

**Tornadoes Job ERP** est une application de gestion d'entreprise **moderne**, **complète** et **simplifiée**, conçue pour aider les PME/PMI à gérer tous leurs aspects opérationnels.

### Pourquoi Tornadoes Job ERP ?

- ✅ **Complet** : Tous les modules essentiels en une seule application
- ✅ **Simple** : Interface intuitive, logique métier claire
- ✅ **Moderne** : Stack technique à jour, meilleures pratiques
- ✅ **Évolutif** : Architecture modulaire, facile à étendre
- ✅ **Open Source** : Code accessible, personnalisable

---

## 🚀 Fonctionnalités

### 👥 Ressources Humaines
- Gestion des employés (CRUD, photos, QR codes)
- Départements et postes
- Présences et pointages
- Congés et absences
- Évaluations de performance

### 💰 Finance
- Factures clients et fournisseurs
- Suivi des paiements
- Dépenses et notes de frais
- Trésorerie et cash flow
- Rapports financiers

### 🤝 CRM & Ventes (NOUVEAU)
- Gestion des contacts (Clients, Fournisseurs, Partenaires)
- Pipeline des opportunités
- Suivi des deals
- Statistiques commerciales

### 📦 Achats (NOUVEAU)
- Commandes fournisseurs
- Suivi des livraisons
- Réceptions
- Historique des achats

### 📊 Inventaire
- Actifs et équipements
- Affectations
- Suivi du matériel
- Alertes de maintenance

### 🎓 Formation
- Gestion des étudiants
- Programmes et cours
- Inscriptions
- Notes et évaluations
- Planning

### 🗂️ Projets
- Suivi de projets
- Jalons et milestones
- Équipes
- Progress tracking

### 📄 Documents
- Gestion documentaire
- Versions et historique
- Approbations et signatures
- Catégories et tags

### ⚙️ Administration
- Utilisateurs et rôles
- Permissions (RBAC)
- Audit logs
- Paramètres système

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    FRONTEND                              │
│  React 18 + TypeScript + Vite + Zustand + Recharts      │
│                                                          │
│  ┌──────────┬──────────┬──────────┬─────────────────┐  │
│  │ Features │ Services │  Store   │    Components   │  │
│  │  (12)    │  (26)    │ (Zustand)│   (Réutilisables)│  │
│  └──────────┴──────────┴──────────┴─────────────────┘  │
└─────────────────────────────────────────────────────────┘
                           │
                           │ HTTP/REST + JWT
                           ▼
┌─────────────────────────────────────────────────────────┐
│                     BACKEND                              │
│  Spring Boot 3.2 + Java 17 + PostgreSQL + Flyway        │
│                                                          │
│  ┌──────────┬──────────┬──────────┬─────────────────┐  │
│  │ Modules  │ Security │  Config  │     Shared      │  │
│  │  (10)    │ (JWT+RBAC)│          │ (Exceptions)    │  │
│  └──────────┴──────────┴──────────┴─────────────────┘  │
└─────────────────────────────────────────────────────────┘
                           │
                           ▼
                 ┌─────────────────┐
                 │   PostgreSQL    │
                 │   (18 tables)   │
                 │   10 migrations │
                 └─────────────────┘
```

---

## ⚡ Démarrage Rapide

### Prérequis

- **Java 17+**
- **Node.js 18+**
- **PostgreSQL 16+** (ou Docker)
- **Maven 3.9+**
- **npm** ou **yarn**

### 1. Cloner le projet

```bash
git clone https://github.com/tornadoesjob/erp.git
cd erp
```

### 2. Backend (5 min)

```bash
cd backend/company-erp

# Copier le fichier d'environnement
cp .env.example .env

# Modifier .env avec vos credentials
# DB_URL, JWT_SECRET, etc.

# Lancer avec Docker
docker-compose up -d

# Ou lancer manuellement
mvn spring-boot:run
```

**Swagger** : http://localhost:8080/api/swagger-ui.html

### 3. Frontend (3 min)

```bash
cd frontend

# Installer les dépendances
npm install

# Copier le fichier d'environnement
cp .env.example .env

# Lancer le serveur de développement
npm run dev
```

**Application** : http://localhost:5173

### 4. Se connecter

| Username | Password | Rôle |
|----------|----------|------|
| `admin` | Admin@123 | Administrateur |
| `hrmanager` | Test123! | RH |
| `finance` | Test123! | Finance |

---

## 📚 Documentation

### Guides Principaux

- 📘 [README Backend](backend/company-erp/README.md) — Documentation complète du backend
- 📗 [README Frontend](frontend/README.md) — Documentation complète du frontend
- 📙 [Refactor Summary](REFACTOR_SUMMARY.md) — Résumé de la refonte v2.0
- 📕 [Quick Test](QUICK_TEST.md) — Guide de test rapide

### Architecture

- **Backend** : Architecture Hexagonale + DDD
- **Frontend** : React + TypeScript + Zustand
- **Database** : PostgreSQL avec Flyway migrations

### API

- **Swagger UI** : http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON** : http://localhost:8080/api/v3/api-docs

---

## 📁 Structure du Projet

```
tournadoes-job-erp/
├── backend/
│   └── company-erp/
│       ├── src/main/java/com/company/erp/
│       │   ├── ErpApplication.java
│       │   ├── config/
│       │   ├── security/
│       │   ├── shared/
│       │   └── modules/
│       │       ├── auth/
│       │       ├── organization/
│       │       ├── hr/
│       │       ├── finance/
│       │       ├── crm/          ← NOUVEAU
│       │       ├── purchases/    ← NOUVEAU
│       │       ├── inventory/
│       │       ├── education/
│       │       ├── projects/
│       │       └── documents/
│       ├── src/main/resources/
│       │   ├── application.yml
│       │   └── db/migration/     ← 10 migrations
│       ├── docker-compose.yml
│       ├── pom.xml
│       └── README.md
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── features/
│   │   │   ├── auth/
│   │   │   ├── dashboard/
│   │   │   ├── hr/               ← Renommé (ex: rh)
│   │   │   ├── finance/
│   │   │   ├── crm/              ← NOUVEAU
│   │   │   ├── purchases/        ← NOUVEAU
│   │   │   ├── inventory/        ← Renommé (ex: stock)
│   │   │   ├── projects/
│   │   │   ├── documents/
│   │   │   ├── formation/
│   │   │   └── settings/
│   │   ├── services/
│   │   ├── store/
│   │   ├── types/
│   │   └── routes/
│   ├── package.json
│   ├── vite.config.ts
│   └── README.md
├── REFACTOR_SUMMARY.md           ← Résumé de la refonte
├── QUICK_TEST.md                 ← Guide de test
└── README.md                     ← Ce fichier
```

---

## 🛠️ Technologies

### Backend

| Technologie | Version | Usage |
|-------------|---------|-------|
| Java | 17+ | Langage |
| Spring Boot | 3.2.5 | Framework |
| Spring Security | 6.x | Authentification & Autorisation |
| Spring Data JPA | - | ORM |
| PostgreSQL | 16 | Base de données |
| Flyway | 11.7.0 | Migrations |
| MapStruct | 1.5.5 | Mapping |
| Lombok | 1.18.32 | Réduction de boilerplate |
| JJWT | 0.12.5 | JWT tokens |
| Cloudinary | 1.34.0 | Upload de fichiers |
| Resilience4j | 2.2.0 | Rate limiting |

### Frontend

| Technologie | Version | Usage |
|-------------|---------|-------|
| React | 18.2.0 | Framework UI |
| TypeScript | 5.4.2 | Typage |
| Vite | 5.2.0 | Build tool |
| Zustand | 4.5.0 | State management |
| Recharts | 2.12.0 | Graphiques |
| Axios | 1.13.6 | HTTP client |
| React Router | 6.22.0 | Routing |
| Lucide React | 0.344.0 | Icônes |

---

## 📊 Statistiques

### Code

| Métrique | Valeur |
|----------|--------|
| Tables BDD | 18 |
| Migrations | 10 |
| Modules Backend | 10 |
| Features Frontend | 12 |
| Services API | 26 |
| Types TypeScript | 80+ |

### Fonctionnalités

| Domaine | Features |
|---------|----------|
| RH | 5 |
| Finance | 3 |
| CRM | 2 (NOUVEAU) |
| Achats | 1 (NOUVEAU) |
| Inventaire | 2 |
| Formation | 3 |
| Opérations | 3 |
| Admin | 3 |

**Total : 22 fonctionnalités principales**

---

## 👥 Contributeurs

Développé par l'équipe **Tornadoes Job**.

---

## 📝 License

Copyright © 2024 Tornadoes Job. Tous droits réservés.

---

## 📞 Contact

- 📧 Email : support@tornadoesjob.com
- 🌐 Site : https://tornadoesjob.com
- 📚 Docs : https://docs.tornadoesjob.com

---

**Tornadoes Job ERP v2.0.0**  
Développé avec ❤️ par l'équipe Tornadoes Job
