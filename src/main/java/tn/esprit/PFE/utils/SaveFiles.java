package tn.esprit.PFE.utils;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.payload.response.MessageResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaveFiles {
    @Value("C:/Users/amadd/Desktop/9antraProject/9antraProjetFront-master/src/assets/UploadedFiles")
    private String Location;

    public void DeleteFile(String path){
        File fileToDelete = new File(Location + "/" + path);
        fileToDelete.delete();
    }


    public ResponseEntity<?> SaveFile(MultipartFile file, String name) {
        if (!file.isEmpty()) {
            Path path = null;
            Map<String, String> response = new HashMap<>();
            String originalFileName = file.getOriginalFilename();
            String timeStamp = String.valueOf(System.currentTimeMillis());
            String newFileName = StringUtils.cleanPath(timeStamp + "_" + originalFileName);
            try {
                if (name.isEmpty() || name.equals("")) {
                    // Check if the base location exists, and create it if not
                    if (!Files.exists(Paths.get(Location))) {
                        Files.createDirectories(Paths.get(Location));
                    }
                    path = Paths.get(Location + "/" + newFileName);
                } else {
                    // Check if the base location + name exists, and create it if not
                    if (!Files.exists(Paths.get(Location + "/" + name))) {
                        Files.createDirectories(Paths.get(Location + "/" + name));
                    }
                    path = Paths.get(Location + "/" + name + "/" + newFileName);
                    response.put("path", name + "/" + newFileName);
                }

                // Check if the file already exists at the destination
                if (Files.exists(path)) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Fichier existe deja"));
                }

                // Copy the file to the destination
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
            }

            response.put("path", name+"/"+newFileName);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Pas de fichier envoyer"));
    }


}
