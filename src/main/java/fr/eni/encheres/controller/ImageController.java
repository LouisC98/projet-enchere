package fr.eni.encheres.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ImageController {
    @GetMapping("/images/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        try {
            // Chemin vers le dossier uploads
            Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");
            Path filePath = uploadDir.resolve(filename);

            // Charger le fichier comme ressource
            Resource resource = new UrlResource(filePath.toUri());

            // Vérifier si le fichier existe
            if (resource.exists() || resource.isReadable()) {
                // Déterminer le type MIME en fonction de l'extension
                String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
                MediaType mediaType;

                switch (extension) {
                    case "png":
                        mediaType = MediaType.IMAGE_PNG;
                        break;
                    case "jpg":
                    case "jpeg":
                    default:
                        mediaType = MediaType.IMAGE_JPEG;
                }

                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}