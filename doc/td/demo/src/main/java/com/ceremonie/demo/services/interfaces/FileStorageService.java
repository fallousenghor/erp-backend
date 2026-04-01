package com.ceremonie.demo.services.interfaces;

// ========================================
// 1. FILE STORAGE SERVICE - Interface
// ========================================

import org.springframework.web.multipart.MultipartFile;

import com.ceremonie.demo.dto.FileUploadResult;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {
    String storeFile(MultipartFile file, String directory) throws IOException;
    FileUploadResult storeFileAdvanced(MultipartFile file, String directory) throws IOException;
    byte[] loadFileAsBytes(String filename, String directory) throws IOException;
    Path loadFile(String filename, String directory);
    void deleteFile(String filename, String directory);
    String getFileUrl(String filename, String directory);
    void init();
}









// ========================================
// 9. CONFIGURATION MULTIPART (application.properties)
// ========================================
/*
Ajoutez ces propriétés dans application.properties :

# File Upload Configuration
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
spring.servlet.multipart.file-size-threshold=2KB

# File Storage
file.upload-dir=./uploads

# Création automatique du dossier au démarrage
*/

// ========================================
// 10. EXEMPLE D'UTILISATION (Frontend)
// ========================================
/*
// Upload avec JavaScript/React/Angular

const uploadMedia = async (file, data) => {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('data', new Blob([JSON.stringify(data)], {
    type: 'application/json'
  }));

  const response = await fetch('http://localhost:8080/api/medias', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`
    },
    body: formData
  });

  return await response.json();
};

// Utilisation
const file = document.getElementById('fileInput').files[0];
const data = {
  ceremonialYearId: 1,
  title: "Photo cérémonie 2024",
  description: "Belle photo de groupe",
  type: "PHOTO",
  tags: "ceremonie,2024,groupe"
};

uploadMedia(file, data).then(response => {
  console.log('Upload réussi:', response);
});

// Afficher une image
<img src="http://localhost:8080/api/files/photos/filename.jpg" />
*/


/*
========================================
RÉSUMÉ ÉTAPE 9
========================================

✅ Services créés:
1. FileStorageService - Gestion stockage fichiers
2. MediaService - Gestion médias (photos/vidéos/sons)
3. ImageCompressionService (bonus) - Compression images

✅ Controllers créés:
1. MediaController - Upload/Download médias
2. FileController - Servir fichiers statiques

✅ Fonctionnalités implémentées:
- Upload multipart (photos, vidéos, sons)
- Validation types de fichiers
- Stockage organisé par type (photos/, videos/, audios/)
- Génération noms uniques (UUID)
- URLs publiques pour accès fichiers
- Download/Suppression médias
- Recherche et filtrage par année/type
- Métadonnées (titre, description, tags)

✅ Endpoints:
- POST /api/medias - Upload média
- GET /api/medias/{id} - Obtenir média
- GET /api/medias/active-year - Médias année active
- GET /api/files/{directory}/{filename} - Accéder fichier
- DELETE /api/medias/{id} - Supprimer média

🎯 Prochaine étape suggérée:
ÉTAPE 10: Intégration paiements mobiles (Wave/Orange Money)
*/