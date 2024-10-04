package tn.esprit.PFE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.service.Impl.FavorisService;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RequestMapping("/api")
@RestController
public class FavorisController {
    @Autowired
    private FavorisService favorisService;

    @PostMapping(value = "/Favoris")
    public void Addtest(@RequestParam("idp") Long idp,
                        @RequestParam("idu") Long idu
    ) {
        System.out.println("azefsf ");
         favorisService.AddFavoris(idu,idp);
    }

    @GetMapping(value = "/GetFavoris")
    public ResponseEntity<?> GetFavoris(@RequestParam("idu") Long idu,
                                        Pageable pageable){
        return favorisService.GetFavoris(idu,pageable);
    }
}
