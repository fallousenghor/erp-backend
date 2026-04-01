# Diagrammes UML - Company ERP

Ce dossier contient les diagrammes UML **découpés par domaine métier** pour une meilleure lisibilité dans le mémoire de fin d'études.

## 📁 Structure des Fichiers

```
diagrammes/
├── usecases/              # Diagrammes de cas d'utilisation par acteur
│   ├── usecase-authentification.puml
│   ├── usecase-rh.puml
│   ├── usecase-finance.puml
│   ├── usecase-education.puml
│   ├── usecase-organisation.puml
│   └── usecase-documents.puml
├── classes/               # Diagrammes de classes par domaine métier
│   ├── class-authentification.puml
│   ├── class-rh.puml
│   ├── class-finance.puml
│   ├── class-education.puml
│   ├── class-organisation.puml
│   └── class-documents.puml
└── README.md              # Ce fichier
```

---

## 🎯 Diagrammes de Cas d'Utilisation

### 1. Authentification & Sécurité
**Fichier :** `usecases/usecase-authentification.puml`

**Acteurs :** Administrateur, Utilisateur

**Fonctionnalités :**
- Authentification (login, logout, refresh token)
- Gestion des rôles (CRUD, assignation)
- Gestion des permissions

---

### 2. Gestion des Ressources Humaines
**Fichier :** `usecases/usecase-rh.puml`

**Acteurs :** Employé, Manager RH, Administrateur

**Fonctionnalités :**
- Gestion des employés (CRUD, photo, QR code)
- Gestion des congés (demande, approbation)
- Gestion des présences (check-in/out, statistiques)
- Évaluation de performance et objectifs

---

### 3. Finance & Comptabilité
**Fichier :** `usecases/usecase-finance.puml`

**Acteurs :** Comptable, Administrateur, Employé

**Fonctionnalités :**
- Gestion des factures (création, envoi, paiement)
- Gestion des dépenses (soumission, approbation)
- Écritures comptables
- Tableau de bord financier

---

### 4. Formation & Éducation
**Fichier :** `usecases/usecase-education.puml`

**Acteurs :** Étudiant, Enseignant, Administrateur

**Fonctionnalités :**
- Gestion des étudiants
- Gestion des programmes de formation
- Inscriptions aux programmes
- Gestion des notes et moyennes
- Gestion des enseignants

---

### 5. Organisation & Inventaire
**Fichier :** `usecases/usecase-organisation.puml`

**Acteurs :** Administrateur, Manager RH, Employé

**Fonctionnalités :**
- Gestion des départements et budgets
- Gestion des positions
- Gestion des assets (assignation, retour)
- Gestion de projets

---

### 6. Gestion Documentaire & Rapports
**Fichier :** `usecases/usecase-documents.puml`

**Acteurs :** Administrateur, Employé, Comptable

**Fonctionnalités :**
- Gestion des documents (signature, versioning)
- Tableau de bord global
- Rapports et audits

---

## 🏗️ Diagrammes de Classes

### 1. Authentification
**Fichier :** `classes/class-authentification.puml`

**Classes :** User, Role, Permission, RefreshToken

**Relations :**
- User (1) ↔ (M) Role
- Role (1) ↔ (M) Permission
- User (1) ↔ (M) RefreshToken

---

### 2. Ressources Humaines
**Fichier :** `classes/class-rh.puml`

**Classes :** Employee, LeaveRequest, Attendance, PerformanceReview, Objective, Salary

**Relations :**
- Employee (1) ↔ (M) LeaveRequest
- Employee (1) ↔ (M) Attendance
- Employee (1) ↔ (M) PerformanceReview
- Employee (1) ↔ (M) Objective
- Employee (1) *-- (1) Salary

---

### 3. Finance
**Fichier :** `classes/class-finance.puml`

**Classes :** Invoice, InvoiceItem, Payment, Expense, Money

**Relations :**
- Invoice (1) *-- (M) InvoiceItem
- Invoice (1) *-- (M) Payment
- Expense *-- Money

---

### 4. Éducation
**Fichier :** `classes/class-education.puml`

**Classes :** Student, TrainingProgram, Enrollment, CourseModule, ModuleGrade, Teacher

**Relations :**
- Student (1) ↔ (M) Enrollment
- TrainingProgram (1) ↔ (M) Enrollment
- TrainingProgram (1) *-- (M) CourseModule
- Enrollment (1) *-- (M) ModuleGrade
- CourseModule (M) --> (1) Teacher

---

### 5. Organisation & Inventaire
**Fichier :** `classes/class-organisation.puml`

**Classes :** Department, Position, DepartmentHead, Asset, AssetAssignment, Project

**Relations :**
- Department (1) *-- (M) Position
- Department (1) *-- (M) DepartmentHead
- Asset (1) *-- (M) AssetAssignment

---

### 6. Documents & Comptabilité
**Fichier :** `classes/class-documents.puml`

**Classes :** Document, JournalEntry

**Relations :**
- Document --> Department (référence)
- Document --> Employee (référence)

---

## 🔧 Comment Générer les PNG

### Option 1: En Ligne (Recommandé - Sans Installation)

1. Allez sur https://www.plantuml.com/plantuml/
2. Copiez le contenu d'un fichier `.puml`
3. Collez dans l'éditeur
4. Cliquez sur "Generate PNG"

### Option 2: Extension VS Code

1. Installez l'extension "PlantUML" (jebbs.plantuml)
2. Ouvrez un fichier `.puml`
3. Appuyez sur `Alt + D` pour prévisualiser
4. Clic droit → "Export Diagram File to..." → PNG

### Option 3: PlantUML CLI

```bash
# Installer PlantUML
sudo apt-get install plantuml graphviz

# Générer tous les PNG
cd diagrammes/usecases
plantuml -tpng *.puml

cd ../classes
plantuml -tpng *.puml
```

---

## 📝 Intégration dans le Mémoire

### Structure Recommandée

```
Chapitre 3 - Conception et Modélisation

3.1 Diagrammes de Cas d'Utilisation
    3.1.1 Authentification et Sécurité (Figure 3.1)
    3.1.2 Gestion des Ressources Humaines (Figure 3.2)
    3.1.3 Finance et Comptabilité (Figure 3.3)
    3.1.4 Formation et Éducation (Figure 3.4)
    3.1.5 Organisation et Inventaire (Figure 3.5)
    3.1.6 Gestion Documentaire (Figure 3.6)

3.2 Diagrammes de Classes
    3.2.1 Module d'Authentification (Figure 3.7)
    3.2.2 Module RH (Figure 3.8)
    3.2.3 Module Finance (Figure 3.9)
    3.2.4 Module Éducation (Figure 3.10)
    3.2.5 Module Organisation (Figure 3.11)
    3.2.6 Module Documents (Figure 3.12)
```

### Exemple de Légende

```
Figure 3.2 - Diagramme de cas d'utilisation : Gestion des Ressources Humaines

Ce diagramme présente les interactions entre les acteurs du module RH :
- L'employé peut demander des congés et consulter ses présences
- Le manager RH approuve les congés et gère les évaluations
- L'administrateur gère le catalogue des employés

Les relations « include » montrent que l'approbation d'un congé
nécessite préalablement la consultation des demandes.
```

---

## 📊 Récapitulatif

| Domaine | Use Cases | Classes | Acteurs |
|---------|-----------|---------|---------|
| Authentification | 8 | 4 | 2 |
| RH | 18 | 6 | 3 |
| Finance | 17 | 5 | 3 |
| Éducation | 15 | 6 | 3 |
| Organisation | 14 | 6 | 3 |
| Documents | 10 | 2 | 3 |
| **Total** | **82** | **29** | **6** |

---

## 🎨 Conventions Utilisées

- **Notation UML :** 2.5
- **Outil :** PlantUML
- **Couleurs des acteurs :**
  - Bleu : Administrateur
  - Vert : Employé
  - Orange : Manager RH
  - Jaune : Comptable
  - Rose : Étudiant
  - Cyan : Enseignant

---

## ℹ️ Notes Techniques

- **Architecture :** Hexagonale (Ports & Adapters) + DDD
- **Base de données :** PostgreSQL 16
- **ORM :** JPA/Hibernate
- **Backend :** Spring Boot 3.2.5
- **Sécurité :** Spring Security 6 + JWT
