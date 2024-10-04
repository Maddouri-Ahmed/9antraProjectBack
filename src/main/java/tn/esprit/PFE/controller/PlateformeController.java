package tn.esprit.PFE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.entities.Plateforme;
import tn.esprit.PFE.service.Impl.PlateformeService;

@CrossOrigin(origins = {"*"})
@RequestMapping("/api")
@RestController
public class PlateformeController {
    @Autowired
    PlateformeService plateformeService;

    @PostMapping(value = "/AddPlateforme")
    public ResponseEntity<?> AddCategorie(@RequestBody Plateforme p){
        return plateformeService.AddPlateforme(p);
    }

    @PutMapping(value = "/ModifierPlateforme")
    public ResponseEntity<?> ModifierPlateforme(@RequestBody Plateforme p){
        return plateformeService.ModifierPlateforme(p);
    }

    @GetMapping(value = "/ChangerActivePlateforme/{id}")
    public void ChangerActivePlateforme(@PathVariable("id") Long id){
        plateformeService.ChangeActive(id);
    }

    @GetMapping(value = "/GetPlateforme/{id}")
    public ResponseEntity<?> GetPlateforme(@PathVariable("id") Long id){
        return plateformeService.GetPlateforme(id);
    }

    @GetMapping(value = "/GetAllPlateformeByActiveOrderByNom")
    public ResponseEntity<?> GetAllByActiveOrderByNom(){
        return plateformeService.GetAllByActiveOrderByNom();
    }

    @PostMapping(value="/EditPlateformeFile")
    public ResponseEntity<?> EditFile(@RequestParam(name = "file") MultipartFile file,
                                      @RequestParam(name = "name") String name,
                                      @RequestParam(name = "id") Long id){
        return plateformeService.EditFile(file, name, id);
    }

    @GetMapping(value = "/GetAllPlateformesFilteredBy")
    public ResponseEntity<?> GetAllPlateformesFilteredBy(
            @RequestParam(value = "nom", required = false) String nom,
            @RequestParam(value = "active", required = false) Boolean active,
            Pageable pageable
    ) {
        return plateformeService.GetAllFiltredBy(nom, active, pageable);
    }

    @GetMapping(value = "/GetTop5Plateformes")
    public ResponseEntity<?> GetTop5Plateformes() {
        return plateformeService.GetTop5Plateformes();
    }

    @GetMapping(value = "/GetNumberPlatforme")
    public Number GetNumberPlatforme() {
        return plateformeService.GetNumberPlatforme();
    }
}
