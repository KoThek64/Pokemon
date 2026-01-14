# 📋 Guide de gestion des Issues et Workflow Git

Guide simple pour organiser le développement du projet avec GitHub Issues et les branches Git.

---

## 🎯 Comment créer une bonne issue

Une issue = une tâche à faire. Gardez ça simple !

### Template basique

```markdown
## 📝 Description
Expliquez en 2-3 lignes ce qu'il faut faire.

Exemple : Ajouter un système d'évolution pour que les Pokémon puissent évoluer après un combat.

## 🔧 À faire
- Créer la classe Evolution
- Modifier Pokemon.kt
- Ajouter les données dans pokedex.json
- Écrire des tests

## ✅ C'est terminé quand
Les Pokémon évoluent correctement après combat et les tests passent.
```

### Conseils
- **Titre clair** : "Ajouter système d'évolution" ✅ plutôt que "Évolution" ❌
- **Description courte** : Vous comprendrez dans 2 semaines
- **Ajouter des labels** : Pour trier facilement
- **Rester simple** : Vous êtes seul, pas besoin de complexité

---

## 🏷️ Labels à créer sur GitHub

Créez ces labels dans votre dépôt pour organiser vos issues.

| Label | Couleur | Quand l'utiliser |
|-------|---------|------------------|
| `fonctionnalité` | `#a2eeef` | Nouvelle feature à ajouter |
| `bug` | `#d73a4a` | Quelque chose ne fonctionne pas |
| `urgent` | `#b60205` | À faire en priorité |
| `important` | `#fbca04` | Important mais pas urgent |
| `plus tard` | `#0e8a16` | Peut attendre |
| `gameplay` | `#5319e7` | Mécanique de jeu |
| `interface` | `#d876e3` | Affichage / menus |
| `tests` | `#c5def5` | Tests uniquement |
| `documentation` | `#0075ca` | README, commentaires |
| `refactoring` | `#fbca04` | Améliorer le code existant |

### Comment créer les labels

1. Aller sur `github.com/KoThek64/Pokemon/labels`
2. Supprimer les labels par défaut si vous voulez
3. Cliquer sur "New label"
4. Remplir : nom, couleur (code hexa)
5. Sauvegarder

---

## 🌿 Workflow Git (dev → master)

Le principe : **toujours travailler sur `dev`, merger dans `master` quand c'est stable**.

### 1. Créer la branche dev (une seule fois)

```bash
git checkout -b dev
git push -u origin dev
```

### 2. Pour chaque nouvelle fonctionnalité

```bash
# 1. Se placer sur dev
git checkout dev
git pull origin dev

# 2. Créer une branche pour votre tâche
git checkout -b feature/evolution-system
# OU pour un bug
git checkout -b fix/pp-validation

# 3. Coder, tester, commiter
git add .
git commit -m "feat: ajout système évolution"

# 4. Pousser la branche
git push origin feature/evolution-system

# 5. Sur GitHub : créer une Pull Request
#    Source: feature/evolution-system
#    Destination: dev
#    Écrire "Closes #3" pour fermer l'issue automatiquement

# 6. Merger la PR sur GitHub

# 7. Revenir sur dev localement
git checkout dev
git pull origin dev

# 8. Supprimer la branche de feature (optionnel)
git branch -d feature/evolution-system
```

### 3. Quand dev est stable : merger dans master

```bash
# Après plusieurs features testées
git checkout master
git pull origin master
git merge dev

# Créer un tag de version (recommandé)
git tag -a v0.2.0 -m "Version 0.2 - Évolutions et sauvegardes"

# Pousser tout
git push origin master --tags
```

---

## 🎨 Nommage des branches

Utilisez ces préfixes :

- `feature/nom` - Nouvelle fonctionnalité
  - Exemple : `feature/menu-principal`
- `fix/nom` - Correction de bug
  - Exemple : `fix/validation-pp`
- `refactor/nom` - Amélioration du code
  - Exemple : `refactor/combat-simplification`
- `docs/nom` - Documentation uniquement
  - Exemple : `docs/readme-update`

---

## 💬 Messages de commit

Utilisez des préfixes clairs :

```bash
feat: ajout de nouvelles fonctionnalités
fix: correction de bug
docs: mise à jour de documentation
test: ajout ou modification de tests
refactor: simplification de code sans changer le comportement
chore: mise à jour de tâches annexes (CI, config, etc.)
```

**Pourquoi ?** Vous comprendrez l'historique en un coup d'œil !

---

## 🔗 Lier commits et issues

### Dans un commit

```bash
# Référencer une issue
git commit -m "feat: ajout menu principal (#2)"

# Fermer automatiquement une issue au push
git commit -m "fix: validation des PP (closes #4)"
```

### Dans une Pull Request

Écrivez dans la description de la PR :

```markdown
Closes #3
```

L'issue #3 se fermera automatiquement quand vous mergerez la PR ! 🎉

---

## 📅 Workflow quotidien recommandé

1. **Matin** : Regarder les issues, en choisir une
2. **Créer une branche** depuis `dev`
3. **Coder + tester**
4. **Commiter** régulièrement (au moins 1x/jour)
5. **Pousser** et créer une PR quand terminé
6. **Merger** dans `dev`
7. **Répéter** !

Quand vous avez 3-4 features terminées → merger `dev` dans `master` 🚀

---

## 🚀 Pour commencer maintenant

```bash
# 1. Créer dev si pas encore fait
git checkout -b dev
git push -u origin dev

# 2. Retour sur master
git checkout master

# 3. Créer vos premières issues sur GitHub
# Exemples :
# - "Ajouter validation des PP" (bug, urgent)
# - "Créer menu principal" (fonctionnalité, important)
# - "Implémenter système évolution" (fonctionnalité, important)

# 4. Commencer par une issue facile
git checkout dev
git checkout -b fix/pp-validation

# 5. Coder et s'amuser ! 🎮
```

---

## 📚 Résumé rapide

| Action | Commande |
|--------|----------|
| Nouvelle feature | `git checkout dev` → `git checkout -b feature/nom` |
| Commit | `git commit -m "feat: description"` |
| Pousser | `git push origin feature/nom` |
| PR vers dev | Sur GitHub, merger quand OK |
| Release | `git checkout master` → `git merge dev` |

---

**Gardez ça simple et amusez-vous ! 🚀**

