# 🚀 TORNADOES JOB ERP - GUIDE DE TEST RAPIDE

> Comment tester l'application en 5 minutes

---

## ⚡ Démarrage Rapide

### Étape 1 : Lancer le Backend (2 min)

```bash
# Terminal 1
cd backend/company-erp

# Option A : Avec Docker (Recommandé)
docker-compose up -d

# Option B : Manuellement (si PostgreSQL déjà installé)
# 1. Créer la base de données
# 2. Modifier .env avec les credentials
mvn spring-boot:run
```

**Vérification** :
- Backend : http://localhost:8080/api/actuator/health
- Swagger : http://localhost:8080/api/swagger-ui.html

---

### Étape 2 : Lancer le Frontend (2 min)

```bash
# Terminal 2
cd frontend

# Installer les dépendances (une seule fois)
npm install

# Lancer le serveur de développement
npm run dev
```

**Vérification** :
- Application : http://localhost:5173

---

### Étape 3 : Se connecter (30 sec)

**URL** : http://localhost:5173/login

| Username | Password | Rôle |
|----------|----------|------|
| `admin` | Admin@123 | Administrateur |
| `hrmanager` | Test123! | RH |
| `finance` | Test123! | Finance |

---

## 🎯 Parcours de Test

### Test 1 : Tableau de Bord (1 min)
1. ✅ Se connecter avec `admin` / `Admin@123`
2. ✅ Vérifier les KPIs (Employés, Factures, Deals, Projets)
3. ✅ Vérifier les graphiques (Revenus, Cash Flow)
4. ✅ Vérifier l'activité récente

---

### Test 2 : Ressources Humaines (3 min)

#### 2.1 Employés
1. ✅ Aller dans **RH → Employés**
2. ✅ Voir la liste des 3 employés
3. ✅ Cliquer sur un employé pour voir les détails
4. ✅ Créer un nouvel employé (bouton "+")
   - Prénom : "Test"
   - Nom : "User"
   - Email : "test@tornadoesjob.com"
   - Département : "IT"
   - Poste : "Développeur"

#### 2.2 Départements
1. ✅ Aller dans **RH → Départements**
2. ✅ Voir les 6 départements
3. ✅ Vérifier les postes intégrés (JSON)

#### 2.3 Présences & Congés
1. ✅ Aller dans **RH → Présences & Congés**
2. ✅ Voir les présences du jour
3. ✅ Voir les demandes de congés

---

### Test 3 : Finance (3 min)

#### 3.1 Factures
1. ✅ Aller dans **Finance → Factures**
2. ✅ Voir les 3 factures clients
3. ✅ Créer une nouvelle facture
   - Client : "Nouveau Client"
   - Montant : 100000 XOF
   - Items : Description, Quantité, Prix

#### 3.2 Dépenses
1. ✅ Aller dans **Finance → Dépenses**
2. ✅ Voir les 3 dépenses
3. ✅ Créer une nouvelle dépense
   - Titre : "Test"
   - Catégorie : "TRANSPORT"
   - Montant : 5000 XOF

#### 3.3 Trésorerie
1. ✅ Aller dans **Finance → Trésorerie**
2. ✅ Voir le cash flow
3. ✅ Voir les revenus et dépenses par mois

---

### Test 4 : CRM (NOUVEAU) (3 min)

#### 4.1 Contacts
1. ✅ Aller dans **CRM → Contacts**
2. ✅ Voir les 6 contacts (3 clients, 2 fournisseurs, 1 prospect)
3. ✅ Créer un nouveau contact
   - Type : "CLIENT"
   - Entreprise : "Ma Société"
   - Email : "contact@masociete.com"
   - Ville : "Abidjan"

#### 4.2 Opportunités (Deals)
1. ✅ Aller dans **CRM → Opportunités**
2. ✅ Voir les 2 deals
3. ✅ Créer un nouveau deal
   - Titre : "Nouveau Projet"
   - Contact : "Ma Société"
   - Montant : 5000000 XOF
   - Stage : "PROSPECTION"

---

### Test 5 : Achats (NOUVEAU) (2 min)

1. ✅ Aller dans **Opérations → Achats**
2. ✅ Voir les 2 purchase orders
3. ✅ Créer une nouvelle commande
   - Fournisseur : "Dell Technologies"
   - Items : Laptop, Quantité: 1, Prix: 500000
   - Total : 590000 XOF (avec taxe)

---

### Test 6 : Inventaire (2 min)

1. ✅ Aller dans **Opérations → Inventaire**
2. ✅ Voir les 5 actifs
3. ✅ Vérifier les statuts (AVAILABLE, ASSIGNED, etc.)
4. ✅ Affecter un actif à un employé

---

### Test 7 : Projets (2 min)

1. ✅ Aller dans **Opérations → Projets**
2. ✅ Voir les 3 projets
3. ✅ Vérifier la progression (%)
4. ✅ Voir les jalons (milestones)

---

### Test 8 : Formation (3 min)

#### 8.1 Étudiants
1. ✅ Aller dans **Formation → Étudiants**
2. ✅ Voir les 4 étudiants
3. ✅ Créer un nouvel étudiant

#### 8.2 Programmes
1. ✅ Aller dans **Formation → Programmes**
2. ✅ Voir les 3 programmes
3. ✅ Vérifier les modules (JSON)

#### 8.3 Inscriptions
1. ✅ Aller dans **Formation → Inscriptions**
2. ✅ Voir les inscriptions
3. ✅ Inscrire un étudiant à un programme

---

### Test 9 : Administration (2 min)

#### 9.1 Rôles & Permissions
1. ✅ Aller dans **Administration → Rôles & Permissions**
2. ✅ Voir les 5 rôles
3. ✅ Vérifier les permissions (JSON)

#### 9.2 Audit Logs
1. ✅ Aller dans **Administration → Audit Logs**
2. ✅ Voir les actions récentes
3. ✅ Filtrer par utilisateur ou action

#### 9.3 Paramètres
1. ✅ Aller dans **Administration → Paramètres**
2. ✅ Modifier les paramètres de l'app

---

## 🧪 Tests API (Swagger)

### Tester avec Swagger UI

**URL** : http://localhost:8080/api/swagger-ui.html

### Test 1 : Auth
```
POST /api/v1/auth/login
{
  "username": "admin",
  "password": "Admin@123"
}
```

### Test 2 : Employees
```
GET /api/v1/employees
```

### Test 3 : Invoices
```
GET /api/v1/invoices
```

### Test 4 : Contacts (NOUVEAU)
```
GET /api/v1/contacts
```

### Test 5 : Deals (NOUVEAU)
```
GET /api/v1/deals
```

### Test 6 : Purchase Orders (NOUVEAU)
```
GET /api/v1/purchase-orders
```

---

## ✅ Checklist de Test Complète

### Backend
- [ ] Serveur démarré sur le port 8080
- [ ] Base de données connectée
- [ ] Migrations exécutées (10/10)
- [ ] Swagger accessible
- [ ] Données de démo présentes

### Frontend
- [ ] Serveur démarré sur le port 5173
- [ ] Login fonctionnel
- [ ] Navigation fonctionne
- [ ] Toutes les pages chargent
- [ ] Pas d'erreurs console

### Fonctionnalités
- [ ] RH : Employés, Départements, Présences, Congés
- [ ] Finance : Factures, Dépenses, Trésorerie
- [ ] CRM : Contacts, Deals
- [ ] Achats : Purchase Orders
- [ ] Inventaire : Actifs
- [ ] Projets
- [ ] Documents
- [ ] Formation : Étudiants, Programmes, Inscriptions
- [ ] Admin : Rôles, Audit, Paramètres

---

## 🐛 Problèmes Courants

### Erreur : "Connection refused" sur le port 8080
**Solution** : Vérifier que le backend est lancé
```bash
cd backend/company-erp
mvn spring-boot:run
```

### Erreur : "Cannot find module" sur le frontend
**Solution** : Réinstaller les dépendances
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
```

### Erreur : "Table doesn't exist"
**Solution** : Reset la base de données et relancer les migrations
```sql
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
```
Puis redémarrer le backend.

### Erreur : "JWT secret must be at least 256 bits"
**Solution** : Modifier .env avec un secret plus long
```env
JWT_SECRET=tornadoes_job_secret_key_change_this_in_production_123456789
```

---

## 📊 Résumé des Données de Démo

| Entité | Count |
|--------|-------|
| Utilisateurs | 5 |
| Départements | 6 |
| Employés | 3 |
| Factures | 3 |
| Dépenses | 3 |
| Contacts | 6 |
| Deals | 2 |
| Purchase Orders | 2 |
| Actifs | 5 |
| Programmes | 3 |
| Étudiants | 4 |
| Projets | 3 |
| Documents | 3 |

---

## 🎉 Test Réussi !

Si tous les tests passent, votre application **Tornadoes Job ERP** est **100% fonctionnelle** ! ✅

---

**Prochaine étape** : Personnaliser l'application avec vos propres données et besoins métier.

**Support** : support@tornadoesjob.com

---

**Tornadoes Job ERP v2.0.0**  
Développé avec ❤️ par l'équipe Tornadoes Job
