package tn.esprit.PFE.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.entities.Categories;
import tn.esprit.PFE.service.Impl.CategoriesService;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"*"})
@RequestMapping("/api")
@RestController
public class CategoriesController {
    @Autowired
    CategoriesService categoriesService;

    @PostMapping(value = "/AddCategorie")
    public ResponseEntity<?> AddCategorie(@RequestBody Categories c){
        return categoriesService.AddCategories(c);
    }

    @PutMapping(value = "/ModifiyCategorie")
    public ResponseEntity<?> ModifiyCategorie(@RequestBody Categories c){
        return categoriesService.ModifierCategories(c);
    }

    @GetMapping(value = "/ChangeActiveCategorie/{id}")
    public void ChangeActiveCategorie(@PathVariable("id") Long id
                                ){
        categoriesService.ChangeActive(id);
    }



    @GetMapping(value = "/GetCategorie/{id}")
    public ResponseEntity<?> GetCategorie(@PathVariable("id") Long id){
        return categoriesService.GetCategorie(id);
    }


    @PostMapping(value="/SaveFile")
    public ResponseEntity<?> SaveFile(@RequestParam(name = "file") MultipartFile file,
                                       @RequestParam(name = "name") String name){
        return categoriesService.SaveFile(file,name);
    }

    @PostMapping(value="/EditCategorieFile")
    public ResponseEntity<?> EditFile(@RequestParam(name = "file") MultipartFile file,
                                      @RequestParam(name = "name") String name,
                                      @RequestParam(name = "id") Long id){
        return categoriesService.EditFile(file,name,id);
    }

    @GetMapping(value = "/GetAllCategoriesOrderByNom")
    public ResponseEntity<?> GetAllCategoriesOrderByNom(@RequestParam(value = "page",required = false) int page,
                                                        @RequestParam(value = "size",required = false) int size){
        Pageable pageable= PageRequest.of(page, size);
        return categoriesService.GetAllByActiveOrderByNom(pageable);
    }


    @GetMapping(value = "/GetAllCategoriesFilteredBy")
    public ResponseEntity<?> GetAllFiltredBy( @RequestParam(value = "nom",required = false) String nom,
                                              @RequestParam(value = "active",required = false) Boolean active,
                                              Pageable pageable
    ) {
        return categoriesService.GetAllFiltredBy(nom,active, pageable);
    }
    @GetMapping(value = "/GetTop5Categoreis")
    public ResponseEntity<?> GetTop5Categoreis() {
        return categoriesService.GetTop5Categoreis();
    }

    @GetMapping(value = "/GetCountCategoreis")
    public Number GetCountCategoreis() {
        return categoriesService.GetCountCategoreis();
    }

    @PutMapping("/{id}/meilleur")
    public ResponseEntity<Void> setMeilleur(@PathVariable Long id, @RequestParam boolean isMeilleur) {
        categoriesService.setMeilleur(id, isMeilleur);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Categories> getCategory(@PathVariable Long id) {
        Categories category = categoriesService.getCategory(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/meilleur")
    public ResponseEntity<List<Categories>> getCategoriesMeilleur() {
        List<Categories> categories = categoriesService.getCategoriesMeilleur();
        return ResponseEntity.ok(categories);
    }


}

