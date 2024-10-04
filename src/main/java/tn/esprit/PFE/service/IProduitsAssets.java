package tn.esprit.PFE.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.entities.ProduitsAssets;

import java.util.List;

public interface IProduitsAssets {
    ProduitsAssets AddAsset(ProduitsAssets p);
    ProduitsAssets ModifierAsset(ProduitsAssets p);

    void Delete(List<Long> id);

    ResponseEntity<?> EditAsset(MultipartFile file, String name, Long id);
}
