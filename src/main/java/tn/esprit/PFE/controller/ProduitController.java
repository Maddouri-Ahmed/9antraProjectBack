package tn.esprit.PFE.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.entities.*;
import tn.esprit.PFE.repository.ProduitRepository;
import tn.esprit.PFE.service.FileServiceImpl;
import tn.esprit.PFE.service.ProduitService;
import javax.servlet.ServletContext;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api")
public class ProduitController {

    /****************************************************/
    @Autowired
    ProduitService us ;
    @Autowired
    private ProduitRepository produitRepository;
    @Autowired
    ServletContext context;

    String subPath = "products";

    @Autowired
    FileServiceImpl serviceFile;

   // ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);



  
     /*@GetMapping("/oldproduit")
    public ResponseEntity<List<oldproduit>> getProduits(){
        return ResponseEntity.ok().body(us.getProduits());
    }

  @PostMapping(value="/Addproduit", consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<oldproduit> addEvent(@RequestPart("oldproduit") String pro, @RequestPart("file") MultipartFile file, User user) throws IOException {

        boolean isExit = new File(context.getRealPath("/Images/")).exists();
        if (!isExit)
        {
            new File (context.getRealPath("/Images/")).mkdir();
            System.out.println("mk dir.............");
        }
        String filename = file.getOriginalFilename();
        String newFileName = FilenameUtils.getBaseName(filename)+"."+FilenameUtils.getExtension(filename);
        File serverFile = new File (context.getRealPath("/Images/"+File.separator+newFileName));
        try
        {
            System.out.println("Image");
            FileUtils.writeByteArrayToFile(serverFile,file.getBytes());

        }catch(Exception e) {
            e.printStackTrace();
        }

        oldproduit nprod = objectMapper.readValue(pro, oldproduit.class);
        nprod.setFileName(file.getOriginalFilename());


        us.saveProduit(nprod,user);
        return new ResponseEntity<>(nprod, HttpStatus.CREATED);
    }*/


    @PostMapping(value = "/addProduct/{id}")
    public ResponseEntity addProduct(@RequestParam(name = "fileName") MultipartFile fileName,
                                     @RequestParam(name = "fileName1") MultipartFile fileName1,
                                    // @RequestParam(name = "fileName2") MultipartFile fileName2,
                                    // @RequestParam(name = "fileName3") MultipartFile fileName3,
                                     @RequestParam(name = "name") String name,
                                     @PathVariable(value = "id",required = false) Long id,
                                     @RequestParam(name = "outil",required = false) Outil outil,
                                     @RequestParam(name = "outil1",required = false) Outil outil1,
                                     @RequestParam(name = "catégories",required = false) catégories catégories,
                                     @RequestParam(name = "description") String description,
                                     @RequestParam(name = "prix") float prix
    ) {
        String imageName = "";
        String imageName1 = "";
       // String imageName2 = "";
       // String imageName3 = "";

        if (fileName != null) {
            imageName = serviceFile.saveFile(fileName, this.subPath);
         }
        if (fileName1 != null) {
            imageName1 = serviceFile.saveFile(fileName1, this.subPath);
        }
         /*if (fileName2 != null) {
            imageName2 = serviceFile.saveFile(fileName2, this.subPath);
        }
        if (fileName3 != null) {
            imageName3 = serviceFile.saveFile(fileName3, this.subPath);
        }*/
        oldproduit product = new oldproduit();
        product.setName(name);
        product.setFileName(imageName);
        product.setFileName1(imageName1);
      //  product.setFileName2(imageName2);
       // product.setFileName3(imageName3);
        product.setIduser(id);
        product.setOutil(outil);
        product.setOutil1(outil1);
        product.setCatégories(catégories);
        product.setTypev("NULL");
        product.setDescription(description);
        product.setPrix(prix);
        return ResponseEntity.ok(us.saveProduit(product,id));
    }

    @PostMapping(value = "/addProductMobil/{id}")
    public ResponseEntity addProductMobil(@RequestParam(name = "fileName") MultipartFile fileName,
                                     @RequestParam(name = "fileName1") MultipartFile fileName1,
                                     //@RequestParam(name = "fileName2") MultipartFile fileName2,
                                    // @RequestParam(name = "fileName3") MultipartFile fileName3,
                                     @RequestParam(name = "name") String name,
                                     @PathVariable(value = "id",required = false) Long id,
                                     @RequestParam(name = "outil2") Outil outil2,
                                     @RequestParam(name = "description") String description,
                                          @RequestParam(name = "Mobileapps",required = false) Mobileapps Mobileapps,
                                     @RequestParam(name = "prix") float prix

    ) {
        String imageName = "";
        String imageName1 = "";
        if (fileName != null) {
            imageName = serviceFile.saveFile(fileName, this.subPath);
        }
        if (fileName1 != null) {
            imageName1 = serviceFile.saveFile(fileName1, this.subPath);
        }

        oldproduit product = new oldproduit();
        product.setName(name);
        product.setFileName(imageName);
        product.setFileName1(imageName1);
        product.setIduser(id);
        product.setOutil2(outil2);
        product.setDescription(description);
        product.setMobileapps(Mobileapps);
        product.setPrix(prix);
        product.setTypev("NULL");
        return ResponseEntity.ok(us.saveProduitMobile(product,id));
    }


    @PostMapping(value = "/addProductDesigne/{id}")
    public ResponseEntity addProductDesigne(@RequestParam(name = "fileName") MultipartFile fileName,

                                          @RequestParam(name = "name") String name,
                                          @PathVariable(value = "id",required = false) Long id,
                                          @RequestParam(name = "outil3") Outil outil3,
                                          @RequestParam(name = "description") String description,
                                            @RequestParam(name = "catégoriesDG",required = false) catégoriesDG catégoriesDG,
                                          @RequestParam(name = "prix") float prix
    ) {
        String imageName = "";

        if (fileName != null) {
            imageName = serviceFile.saveFile(fileName, this.subPath);
        }

        oldproduit product = new oldproduit();
        product.setName(name);
        product.setFileName(imageName);
        product.setIduser(id);
        product.setOutil3(outil3);
        product.setDescription(description);
        product.setPrix(prix);
        product.setCatégoriesDG(catégoriesDG);
        product.setTypev("NULL");
        return ResponseEntity.ok(us.saveProduitDesigne(product,id));
    }

    @PostMapping(value = "/addProductVideo/{id}")
    public ResponseEntity addProductVideo(@RequestParam(name = "fileVideo") MultipartFile fileVideo,
                                            @RequestParam(name = "name") String name,
                                            @PathVariable(value = "id",required = false) Long id,
                                            @RequestParam(name = "outil4") Outil outil4,
                                            @RequestParam(name = "description") String description,
                                            @RequestParam(name = "prix") float prix
    ) {
        String imageName = "";
        if (fileVideo != null) {
            imageName = serviceFile.saveFile(fileVideo, this.subPath);
        }
        oldproduit product = new oldproduit();
        product.setName(name);
        product.setFileVideo(imageName);
        product.setIduser(id);
        product.setOutil4(outil4);
        product.setDescription(description);
        product.setTypev("videofile");
        product.setPrix(prix);
        return ResponseEntity.ok(us.saveProduitvideo(product,id));
    }



    @GetMapping(path="/ImagePros/{id}")
    public byte[] getPhotos(@PathVariable("id") Long id) throws Exception{
        oldproduit e = us.findById(id);
        return Files.readAllBytes(Paths.get(context.getRealPath("/Images/")+e.getFileName()));
    }
    @GetMapping(path="/ImagePros1/{id}")
    public byte[] getPhotos1(@PathVariable("id") Long id) throws Exception{
        oldproduit e = us.findById(id);
        return Files.readAllBytes(Paths.get(context.getRealPath("/Images/")+e.getFileName1()));
    }
    @GetMapping(path="/ImagePros2/{id}")
    public byte[] getPhotos2(@PathVariable("id") Long id) throws Exception{
        oldproduit e = us.findById(id);
        return Files.readAllBytes(Paths.get(context.getRealPath("/Images/")+e.getFileName2()));
    }
    @GetMapping(path="/ImagePros3/{id}")
    public byte[] getPhotos3(@PathVariable("id") Long id) throws Exception{
        oldproduit e = us.findById(id);
        return Files.readAllBytes(Paths.get(context.getRealPath("/Images/")+e.getFileName3()));
    }
    @GetMapping(path="/ImageProsv/{id}")
    public byte[] getPhotosv(@PathVariable("id") Long id) throws Exception{
        oldproduit e = us.findById(id);
        return Files.readAllBytes(Paths.get(context.getRealPath("/Images/")+e.getFileVideo()));
    }



    /**********************************************************************************/
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable(value = "id") Long id) {

        return produitRepository.findById(id).map(pro -> {
            produitRepository.delete(pro);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new IllegalArgumentException(" News not found with id " + id));
    }
    /**********************************************************************************/
    @GetMapping("/retrieve/{id}")
    @ResponseBody
    public oldproduit retrieveproduitById(@PathVariable("id") Long id) {
        return us.findById(id);
    }

    @PutMapping("/update/{id}")
    public oldproduit updateNews(@PathVariable(value = "id") Long id, @RequestBody oldproduit newsRequest) {
        return produitRepository.findById(id).map(pro -> {
            pro.setName (newsRequest.getName());
            pro.setDescription (newsRequest.getDescription());
            pro.setPrix (newsRequest.getPrix());
            pro.setCategore (newsRequest.getCategore());
            pro.setOutil (newsRequest.getOutil());
            pro.setOutil1 (newsRequest.getOutil1());
            pro.setOutil2 (newsRequest.getOutil2());
            pro.setOutil3 (newsRequest.getOutil3());
            pro.setOutil4 (newsRequest.getOutil4());
            return produitRepository.save(pro);
        }).orElseThrow(() -> new IllegalArgumentException("id "  + "not found"));
    }


    @GetMapping("/retrieve-Produit-par-user/{id}")
    @ResponseBody
    public List<oldproduit> findByUser(@PathVariable("id") Long id ) {return us.findByUser(id);
    }

    @GetMapping("/retrieve-Produitv-par-user/{id}")
    @ResponseBody
    public List<oldproduit> findByUserv(@PathVariable("id") Long id ) {return us.findByUserv(id);
    }


    /*******************************************************************************/
    @GetMapping("/retrieve-produit-by-categore/{categore}")
    @ResponseBody
    public List<oldproduit> retrieveProduitByCategore(@PathVariable("categore") Categore categore) {
        return us.retrieveProduitByCategore(categore);
    }

    @GetMapping("/retrieve-produit-by-catégories/{catégories}")
    @ResponseBody
    public List<oldproduit> retrieveProduitByCatégories(@PathVariable("catégories") catégories catégories) {
        return us.retrieveProduitByCatégories(catégories);
    }

    @GetMapping("/retrieve-produit-by-outil/{outil}")
    @ResponseBody
    public List<oldproduit> retrieveProduitByOutil(@PathVariable("outil") Outil outil) {
        return us.retrieveProduitByOutil(outil);
    }
    @GetMapping("/retrieve-produit-by-outil1/{outil1}")
    @ResponseBody
    public List<oldproduit> retrieveProduitByOutil1(@PathVariable("outil1") Outil outil1) {
        return us.retrieveProduitByOutil1(outil1);
    }

    @GetMapping("/retrieve-produit-by-outil2/{outil2}")
    @ResponseBody
    public List<oldproduit> retrieveProduitByOutil2(@PathVariable("outil2") Outil outil2) {
        return us.retrieveProduitByOutil2(outil2);
    }

    @GetMapping("/retrieve-produit-by-outil3/{outil3}")
    @ResponseBody
    public List<oldproduit> retrieveProduitByOutil3(@PathVariable("outil3") Outil outil3) {
        return us.retrieveProduitByOutil3(outil3);
    }

    @GetMapping("/retrieve-produit-by-outil4/{outil4}")
    @ResponseBody
    public List<oldproduit> retrieveProduitByOutil4(@PathVariable("outil4") Outil outil4) {
        return us.retrieveProduitByOutil4(outil4);
    }

    /*******************************************************************************/


    /*******************************************************************************/

    @GetMapping("/findByCategoreAndOutil/{outil}/{categore}")
    public List<oldproduit> findByCategoreAndOutil(@PathVariable("outil") Outil outil, @PathVariable("categore") Categore categore){
        return produitRepository.findByCategoreAndOutil(outil,categore);
    }

    @GetMapping("/findByCategoreAndOutil1/{outil1}/{categore}")
    public List<oldproduit> findByCategoreAndOutil1(@PathVariable("outil1") Outil outil1, @PathVariable("categore") Categore categore){
        return produitRepository.findByCategoreAndOutil1(outil1,categore);
    }


    @GetMapping("/findByCategoreAndOutil2/{outil2}/{categore}")
    public List<oldproduit> findByCategoreAndOutil2(@PathVariable("outil2") Outil outil2, @PathVariable("categore") Categore categore){
        return produitRepository.findByCategoreAndOutil2(outil2,categore);
    }


    @GetMapping("/findByCategoreAndOutil3/{outil3}/{categore}")
    public List<oldproduit> findByCategoreAndOutil3(@PathVariable("outil3") Outil outil3, @PathVariable("categore") Categore categore){
        return produitRepository.findByCategoreAndOutil3(outil3,categore);
    }


    @GetMapping("/findByCategoreAndOutil4/{outil4}/{categore}")
    public List<oldproduit> findByCategoreAndOutil4(@PathVariable("outil4") Outil outil4, @PathVariable("categore") Categore categore){
        return produitRepository.findByCategoreAndOutil4(outil4,categore);
    }
    /*******************************************************************************/

    @GetMapping("/number-webproject")
    public float nomberWebproject() throws Exception {
        return produitRepository.getNumberWebproject();
    }
    @GetMapping("/number-mobilewebprojects")
    public float getNumbermobilewebprojects() throws Exception {
        return produitRepository.getNumbermobilewebprojects();
    }
    @GetMapping("/number-designgraphique")
    public float getNumberdesigngraphique() throws Exception {
        return produitRepository.getNumberdesigngraphique();
    }
    @GetMapping("/number-React")
    public float nomberReact() throws Exception {
        return produitRepository.getNumberReact();
    }
    @GetMapping("/number-Angular")
    public float nomberAngular() throws Exception {
        return produitRepository.getNumberAngular();
    }
    @GetMapping("/number-Vue")
    public float nomberVue() throws Exception {
        return produitRepository.getNumberVue();
    }
    @GetMapping("/number-Svelte")
    public float nomberSvelte() throws Exception {
        return produitRepository.getNumberSvelte();
    }
    @GetMapping("/number-jQuery")
    public float nomberjQuery() throws Exception {
        return produitRepository.getNumberjQuery();
    }
    @GetMapping("/number-Ember")
    public float nomberEmber() throws Exception {
        return produitRepository.getNumberEmber();
    }
    @GetMapping("/number-Backbone")
    public float nomberBackbone() throws Exception {
        return produitRepository.getNumberBackbone();
    }
    @GetMapping("/number-SemanticUI")
    public float nomberSemanticUI() throws Exception {
        return produitRepository.getNumberSemanticUI();
    }
    @GetMapping("/number-Foundation")
    public float nomberFoundation() throws Exception {
        return produitRepository.getNumberFoundation();
    }
    @GetMapping("/number-Preact")
    public float nomberPreact() throws Exception {
        return produitRepository.getNumberPreact();
    }

    @GetMapping("/number-Django")
    public float nomberDjango() throws Exception {
        return produitRepository.getNumberDjango();
    }
    @GetMapping("/number-ExpressJS")
    public float nomberExpressJS() throws Exception {
        return produitRepository.getNumberExpressJS();
    }
    @GetMapping("/number-Laravel")
    public float nomberLaravel() throws Exception {
        return produitRepository.getNumberLaravel();
    }
    @GetMapping("/number-Ruby")
    public float nomberRuby() throws Exception {
        return produitRepository.getNumberRuby();
    }
    @GetMapping("/number-CakePHP")
    public float nomberCakePHP() throws Exception {
        return produitRepository.getNumberCakePHP();
    }
    @GetMapping("/number-Flask")
    public float nomberFlask() throws Exception {
        return produitRepository.getNumberFlask();
    }
    @GetMapping("/number-ASPDOTNETCore")
    public float nomberASPDOTNETCore() throws Exception {
        return produitRepository.getNumberASPDOTNETCore();
    }
    @GetMapping("/number-SpringBoot")
    public float nomberSpringBoot() throws Exception {
        return produitRepository.getNumberSpringBoot();
    }
    @GetMapping("/number-Koa")
    public float nomberKoa() throws Exception {
        return produitRepository.getNumberKoa();
    }
    @GetMapping("/number-Phoenix")
    public float nomberPhoenix() throws Exception {
        return produitRepository.getNumberPhoenix();
    }


    @GetMapping("/p-pending")
    public List<oldproduit> findProduitpending() throws Exception {
        return produitRepository.find();
    }

    @GetMapping("/p-VITRINE")
    public List<oldproduit> findProduitVITRINE() throws Exception {
        return produitRepository.findVITRINE();
    }
    @GetMapping("/p-CATALOGUE")
    public List<oldproduit> findSITE_CATALOGUE() throws Exception {
        return produitRepository.findSITE_CATALOGUE();
    }
    @GetMapping("/p-INFORMATIF")
    public List<oldproduit> findSITE_INFORMATIF() throws Exception {
        return produitRepository.findSITE_INFORMATIF();
    }
    @GetMapping("/p-ECOMMERCE")
    public List<oldproduit> findSITE_MARCHAND_OU_E_COMMERCE() throws Exception {
        return produitRepository.findSITE_MARCHAND_OU_E_COMMERCE();
    }
    @GetMapping("/p-NSTITUTIONNEL")
    public List<oldproduit> findSITE_INSTITUTIONNEL() throws Exception {
        return produitRepository.findSITE_INSTITUTIONNEL();
    }
    @GetMapping("/p-LINTRANET")
    public List<oldproduit> findLINTRANET() throws Exception {
        return produitRepository.findLINTRANET();
    }
    @GetMapping("/p-MINISITE")
    public List<oldproduit> findMINI_SITE() throws Exception {
        return produitRepository.findMINI_SITE();
    }

    @GetMapping("/p-VITRINEMOB")
    public List<oldproduit> Social_Media_Mobile_Apps() throws Exception {
        return produitRepository.Social_Media_Mobile_Apps();
    }
    @GetMapping("/p-CATALOGUEMOB")
    public List<oldproduit> Utility_Mobile_Apps() throws Exception {
        return produitRepository.Utility_Mobile_Apps();
    }
    @GetMapping("/p-INFORMATIFMOB")
    public List<oldproduit> Games_Entertainment_Mobile_Apps() throws Exception {
        return produitRepository.Games_Entertainment_Mobile_Apps();
    }
    @GetMapping("/p-ECOMMERCEMOB")
    public List<oldproduit> Productivity_Mobile_Apps() throws Exception {
        return produitRepository.Productivity_Mobile_Apps();
    }
    @GetMapping("/p-NSTITUTIONNELMOB")
    public List<oldproduit> News_Information_Outlets_Mobile_Apps() throws Exception {
        return produitRepository.News_Information_Outlets_Mobile_Apps();
    }



    @GetMapping("/pp")
    public List<oldproduit> findProduit() throws Exception {
        return produitRepository.findp();
    }

    @GetMapping("/ppx")
    public List<oldproduit> findProduitx() throws Exception {
        return produitRepository.findpx();
    }


    @GetMapping("/pvideo")
    public List<oldproduit> findVideo() throws Exception {
        return produitRepository.findvideo();
    }

    @GetMapping("/pWMD")
    public List<oldproduit> findPWID() throws Exception {
        return produitRepository.findPWID();
    }

    @GetMapping("/produit/{id}/{iduser}")
    public void  retr(@PathVariable("id") long id,@PathVariable("iduser") long iduser) {

        us.AcceptProduit(id,iduser);
    }







    /*******************************************************************************/

    @PutMapping(value = "/updateProductDesigne/{id}")
    public oldproduit updateProductDesigne(// @RequestParam(name = "fileName",required = false) MultipartFile fileName,
                                           @RequestParam(name = "description",required = false) String description,
                                           @RequestParam(name = "prix",required = false) float prix,
                                           @RequestParam(name = "name",required = false) String name,
                                           //  @RequestParam(name = "outil3",required = false) Outil outil3,
                                           @PathVariable("id") Long id)
    {
        Optional<oldproduit> product = Optional.ofNullable(us.findById(id));
        if (product.isEmpty()) {
            ResponseEntity.badRequest().build();
        }


        String imageName = product.get().getFileName();



       // if (fileName!= null) {
           // if (!imageName.equals("")) {
           //     serviceFile.deleteFile(this.subPath + "/" + imageName);
           // }
           // imageName = serviceFile.saveFile(fileName, this.subPath);
      //  }
        oldproduit newProduct = new oldproduit();
        newProduct.setName(name);
        newProduct.setDescription(description);
        newProduct.setPrix(prix);
      //  newProduct.setOutil3(outil3);

       // newProduct.setFileName(imageName);

        return ResponseEntity.ok(us.updateProductDesigne(newProduct, id)).getBody();

    }

    @PutMapping(value = "/updateProductVideo/{id}")
    public oldproduit updateProductVideo(@RequestParam(name = "fileVideo",required = false) MultipartFile fileVideo,
                                         @RequestParam(name = "description",required = false) String description,
                                         @RequestParam(name = "prix",required = false) float prix,
                                         @RequestParam(name = "name",required = false) String name,
                                         @RequestParam(name = "outil4",required = false) Outil outil4,
                                         @PathVariable("id") Long id)
    {
        Optional<oldproduit> product = Optional.ofNullable(us.findById(id));
        if (product.isEmpty()) {
            ResponseEntity.badRequest().build();
        }


        String imageName = product.get().getFileVideo();



        if (fileVideo!= null) {
            if (!imageName.equals("")) {
                serviceFile.deleteFile(this.subPath + "/" + imageName);
            }
            imageName = serviceFile.saveFile(fileVideo, this.subPath);
        }
        oldproduit newProduct = new oldproduit();
        newProduct.setName(name);
        newProduct.setDescription(description);
        newProduct.setPrix(prix);
        newProduct.setOutil4(outil4);

        newProduct.setFileVideo(imageName);

        return ResponseEntity.ok(us.updateProductVideo(newProduct, id)).getBody();

    }


    @PutMapping(value = "/updateProductMobile/{id}")
    public oldproduit updateProductMobile(@RequestParam(name = "fileName") MultipartFile fileName,
                                          @RequestParam(name = "fileName1") MultipartFile fileName1,
                                          @RequestParam(name = "fileName2") MultipartFile fileName2,
                                          @RequestParam(name = "fileName3") MultipartFile fileName3,
                                          @RequestParam(name = "description",required = false) String description,
                                          @RequestParam(name = "prix",required = false) float prix,
                                          @RequestParam(name = "name",required = false) String name,
                                          @RequestParam(name = "outil2",required = false) Outil outil2,
                                          @PathVariable("id") Long id)
    {
        Optional<oldproduit> product = Optional.ofNullable(us.findById(id));
        if (product.isEmpty()) {
            ResponseEntity.badRequest().build();
        }


        String imageName = "";
        String imageName1 = "";
        String imageName2 = "";
        String imageName3 = "";


        if (fileName!= null) {
            if (!imageName.equals("")) {
                serviceFile.deleteFile(this.subPath + "/" + imageName);
            }
            imageName = serviceFile.saveFile(fileName, this.subPath);
        }
        if (fileName1!= null) {
            if (!imageName1.equals("")) {
                serviceFile.deleteFile(this.subPath + "/" + imageName1);
            }
            imageName1 = serviceFile.saveFile(fileName1, this.subPath);
        }
        if (fileName2!= null) {
            if (!imageName2.equals("")) {
                serviceFile.deleteFile(this.subPath + "/" + imageName2);
            }
            imageName2 = serviceFile.saveFile(fileName2, this.subPath);
        }
        if (fileName3!= null) {
            if (!imageName3.equals("")) {
                serviceFile.deleteFile(this.subPath + "/" + imageName3);
            }
            imageName3 = serviceFile.saveFile(fileName3, this.subPath);
        }
        oldproduit newProduct = new oldproduit();
        newProduct.setName(name);
        newProduct.setDescription(description);
        newProduct.setPrix(prix);
        newProduct.setOutil2(outil2);

        newProduct.setFileName(imageName);
        newProduct.setFileName1(imageName1);
        newProduct.setFileName2(imageName2);
        newProduct.setFileName3(imageName3);

        return ResponseEntity.ok(us.updateProductMobile(newProduct, id)).getBody();

    }


    @PutMapping(value = "/updateProductWeb/{id}")
    public oldproduit updateProductWeb(@RequestParam(name = "fileName") MultipartFile fileName,
                                       @RequestParam(name = "fileName1") MultipartFile fileName1,
                                       @RequestParam(name = "fileName2") MultipartFile fileName2,
                                       @RequestParam(name = "fileName3") MultipartFile fileName3,
                                       @RequestParam(name = "description",required = false) String description,
                                       @RequestParam(name = "prix",required = false) float prix,
                                       @RequestParam(name = "name",required = false) String name,
                                       @RequestParam(name = "outil",required = false) Outil outil,
                                       @RequestParam(name = "outil1",required = false) Outil outil1,
                                       @PathVariable("id") Long id)
    {
        Optional<oldproduit> product = Optional.ofNullable(us.findById(id));
        if (product.isEmpty()) {
            ResponseEntity.badRequest().build();
        }


        String imageName = "";
        String imageName1 = "";
        String imageName2 = "";
        String imageName3 = "";


        if (fileName!= null) {
            if (!imageName.equals("")) {
                serviceFile.deleteFile(this.subPath + "/" + imageName);
            }
            imageName = serviceFile.saveFile(fileName, this.subPath);
        }
        if (fileName1!= null) {
            if (!imageName1.equals("")) {
                serviceFile.deleteFile(this.subPath + "/" + imageName1);
            }
            imageName1 = serviceFile.saveFile(fileName1, this.subPath);
        }
        if (fileName2!= null) {
            if (!imageName2.equals("")) {
                serviceFile.deleteFile(this.subPath + "/" + imageName2);
            }
            imageName2 = serviceFile.saveFile(fileName2, this.subPath);
        }
        if (fileName3!= null) {
            if (!imageName3.equals("")) {
                serviceFile.deleteFile(this.subPath + "/" + imageName3);
            }
            imageName3 = serviceFile.saveFile(fileName3, this.subPath);
        }
        oldproduit newProduct = new oldproduit();
        newProduct.setName(name);
        newProduct.setDescription(description);
        newProduct.setPrix(prix);
        newProduct.setOutil(outil);
        newProduct.setOutil1(outil1);

        newProduct.setFileName(imageName);
        newProduct.setFileName1(imageName1);
        newProduct.setFileName2(imageName2);
        newProduct.setFileName3(imageName3);

        return ResponseEntity.ok(us.updateProductWeb(newProduct, id)).getBody();

    }


    @GetMapping("/findByCode/{code}")
    public oldproduit findByCode(@PathVariable("code") String code){
        return produitRepository.findCode(code);
    }



}

