package tn.esprit.PFE.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.entities.ProduitsAssets;
import tn.esprit.PFE.repository.ProduitsAssetsRepository;
import tn.esprit.PFE.service.IProduitsAssets;
import tn.esprit.PFE.utils.SaveFiles;

import java.util.List;

@Service
public class ProduitsAssetsService implements IProduitsAssets {
    @Autowired
    ProduitsAssetsRepository produitsAssetsRepository;
    @Autowired
    CategoriesService categoriesService;
    @Autowired
    private SaveFiles saveFiles;

    @Override
    public ProduitsAssets AddAsset(ProduitsAssets p) {
        return produitsAssetsRepository.save(p);
    }

    @Override
    public ProduitsAssets ModifierAsset(ProduitsAssets p) {
        return null;
    }

    @Override
    public void Delete(List<Long> id) {
        List<ProduitsAssets> list = produitsAssetsRepository.findAllById(id);
        for (ProduitsAssets p : list) {
            if (p != null) {
                if (p.getAsset_url() != null) {
                    saveFiles.DeleteFile(p.getAsset_url());
                }
            }
        }
        produitsAssetsRepository.deleteAll(list);
    }

    @Override
    public ResponseEntity<?> EditAsset(MultipartFile file, String name, Long id) {
        return null;
    }
}
