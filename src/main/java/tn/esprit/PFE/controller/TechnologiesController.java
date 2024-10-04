package tn.esprit.PFE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.entities.Categories;
import tn.esprit.PFE.entities.Technologies;
import tn.esprit.PFE.service.Impl.CategoriesService;
import tn.esprit.PFE.service.Impl.TechnologiesService;


@CrossOrigin(origins = {"*"})
@RequestMapping("/api")
@RestController
public class TechnologiesController {
    @Autowired
    TechnologiesService technologiesService;

    @PostMapping(value = "/AddTechnologie")
    public ResponseEntity<?> AddTechnologie(@RequestBody Technologies t){;
        return technologiesService.AddTechnologie(t);
    }

    @PutMapping(value = "/ModifiyTechnologie")
    public ResponseEntity<?> ModifiyTechnologie(@RequestBody Technologies t){;
        return technologiesService.ModifierTechnologie(t);
    }

    @GetMapping(value = "/ChangerActiveTechnologie/{id}")
    public void ChangerActiveTechnologie(@PathVariable("id") Long id){
        technologiesService.ChangeActive(id);
    }
    
    @GetMapping(value = "/GetTechnologie/{id}")
    public ResponseEntity<?> GetTechnologie(@PathVariable("id") Long id){
        return technologiesService.GetTechnologie(id);
    }

    @GetMapping(value = "/GetAllTechnologieByActiveOrderByNom")
    public ResponseEntity<?> GetAllByActiveOrderByNom(){
        return technologiesService.GetAllByActiveOrderByNom();
    }

    @PostMapping(value="/EditTechnologieFile")
    public ResponseEntity<?> EditFile(@RequestParam(name = "file") MultipartFile file,
                                      @RequestParam(name = "name") String name,
                                      @RequestParam(name = "id") Long id){
        return technologiesService.EditFile(file,name,id);
    }


    @GetMapping(value = "/GetAllTechnologiesFilteredBy")
    public ResponseEntity<?> GetAllTechnologiesFilteredBy( @RequestParam(value = "nom",required = false) String nom,
                                                          @RequestParam(value = "active",required = false) Boolean active,
                                                          Pageable pageable
    ) {
        return technologiesService.GetAllFiltredBy(nom,active, pageable);
    }

    @GetMapping(value = "/GetTopTechnologies")
    public ResponseEntity<?> GetTopTechnologies() {
        return technologiesService.GetTopTechnologies();
    }

    @GetMapping(value = "/GetNumberTechnologies")
    public Number GetNumberTechnologies() {
        return technologiesService.GetNumberTechnologies();
    }
}
