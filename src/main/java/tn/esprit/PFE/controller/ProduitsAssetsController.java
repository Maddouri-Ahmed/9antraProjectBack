package tn.esprit.PFE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.PFE.service.Impl.ProduitsAssetsService;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RequestMapping("/api")
@RestController
public class ProduitsAssetsController {
    @Autowired
    ProduitsAssetsService produitsAssetsService;

    @PostMapping(value = "/DeleteAllAssetsById")
    public void DeleteAllAssetsById(@RequestParam("ids") List<Long> id) {
     produitsAssetsService.Delete(id);
    }
}
