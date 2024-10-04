package tn.esprit.PFE.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.PFE.entities.*;

import java.util.List;


@Repository
public interface ProduitRepository extends JpaRepository<oldproduit,Long> {

    @Query(value=" SELECT u from oldproduit  u where u.iduser= :id and u.typev= 'NULL'")
    List<oldproduit> findByUser (@Param("id") Long id);

    @Query(value=" SELECT u from oldproduit  u where u.iduser= :id and u.typev= 'videofile'  ")
    List<oldproduit> findByUserv (@Param("id") Long id);



    /*******************************************************************************/
    List<oldproduit> findByCategore(Categore categore);
    List<oldproduit> findBycatégories(catégories catégories);
    List<oldproduit> findByOutil(Outil outil);
    List<oldproduit> findByOutil1(Outil outil1);
    List<oldproduit> findByOutil2(Outil outil2);
    List<oldproduit> findByOutil3(Outil outil3);
    List<oldproduit> findByOutil4(Outil outil4);
    /*******************************************************************************/


    @Query(value=" SELECT u from oldproduit  u where u.pending= TRUE and  u.catégories= 'SITE_VITRINE' and u.categore ='webproject'")
    List<oldproduit> findVITRINE ();
    @Query(value=" SELECT u from oldproduit  u where u.pending= TRUE and  u.catégories= 'SITE_CATALOGUE' and u.categore ='webproject'")
    List<oldproduit> findSITE_CATALOGUE ();
    @Query(value=" SELECT u from oldproduit  u where u.pending= TRUE and  u.catégories= 'SITE_INFORMATIF' and u.categore ='webproject'")
    List<oldproduit> findSITE_INFORMATIF ();
    @Query(value=" SELECT u from oldproduit  u where u.pending= TRUE and  u.catégories= 'SITE_MARCHAND_OU_E_COMMERCE' and u.categore ='webproject'")
    List<oldproduit> findSITE_MARCHAND_OU_E_COMMERCE ();
    @Query(value=" SELECT u from oldproduit  u where u.pending= TRUE and  u.catégories= 'SITE_INSTITUTIONNEL' and u.categore ='webproject'")
    List<oldproduit> findSITE_INSTITUTIONNEL ();
    @Query(value=" SELECT u from oldproduit  u where u.pending= TRUE and  u.catégories= 'LINTRANET' and u.categore ='webproject'")
    List<oldproduit> findLINTRANET ();
    @Query(value=" SELECT u from oldproduit  u where u.pending= TRUE and  u.catégories= 'MINI_SITE' and u.categore ='webproject'")
    List<oldproduit> findMINI_SITE ();

    @Query(value=" SELECT u from oldproduit  u where u.pending= TRUE and  u.Mobileapps= 'Social_Media_Mobile_Apps' and u.categore ='mobilewebprojects'")
    List<oldproduit> Social_Media_Mobile_Apps ();
    @Query(value=" SELECT u from oldproduit  u where u.pending= TRUE and  u.Mobileapps= 'Utility_Mobile_Apps' and u.categore ='mobilewebprojects'")
    List<oldproduit> Utility_Mobile_Apps ();
    @Query(value=" SELECT u from oldproduit  u where u.pending= TRUE and  u.Mobileapps= 'Games_Entertainment_Mobile_Apps' and u.categore ='mobilewebprojects'")
    List<oldproduit> Games_Entertainment_Mobile_Apps ();
    @Query(value=" SELECT u from oldproduit  u where u.pending= TRUE and  u.Mobileapps= 'Productivity_Mobile_Apps' and u.categore ='mobilewebprojects'")
    List<oldproduit> Productivity_Mobile_Apps ();
    @Query(value=" SELECT u from oldproduit  u where u.pending= TRUE and  u.Mobileapps= 'News_Information_Outlets_Mobile_Apps' and u.categore ='mobilewebprojects'")
    List<oldproduit> News_Information_Outlets_Mobile_Apps ();






    @Query(value=" SELECT u from oldproduit  u where u.pending= TRUE ")
    List<oldproduit> find ();
    @Query(value=" SELECT u from oldproduit  u where u.pending= FALSE and  u.typev= 'NULL' ")
    List<oldproduit> findp ();
    @Query(value=" SELECT u from oldproduit  u where u.pending= FALSE and  u.typev= 'videofile' ")
    List<oldproduit> findpx ();
    @Query(value=" SELECT u from oldproduit  u where u.typev= 'videofile' ")
    List<oldproduit> findvideo ();
    @Query(value=" SELECT u from oldproduit  u where u.typev= 'NULL' ")
    List<oldproduit> findPWID ();

    @Query(value=" SELECT u from oldproduit  u where u.code= :code ")
    oldproduit findCode (@Param("code") String code);

    /*******************************************************************************/

    @Query(value=" SELECT u from oldproduit  u where u.categore= :categore and u.outil= :outil and u.pending= TRUE  ")
    List<oldproduit> findByCategoreAndOutil(@Param("outil") Outil outil, @Param("categore") Categore categore);

    @Query(value=" SELECT u from oldproduit  u where u.categore= :categore and u.outil1= :outil1 and u.pending= TRUE   ")
    List<oldproduit> findByCategoreAndOutil1(@Param("outil1") Outil outil1, @Param("categore") Categore categore);

    @Query(value=" SELECT u from oldproduit  u where u.categore= :categore and u.outil2= :outil2  and u.pending= TRUE  ")
    List<oldproduit> findByCategoreAndOutil2(@Param("outil2") Outil outil2, @Param("categore") Categore categore);

    @Query(value=" SELECT u from oldproduit  u where u.categore= :categore and u.outil3= :outil3  and u.pending= TRUE  ")
    List<oldproduit> findByCategoreAndOutil3(@Param("outil3") Outil outil3, @Param("categore") Categore categore);

    @Query(value=" SELECT u from oldproduit  u where u.categore= :categore and u.outil4= :outil4  and u.pending= TRUE  ")
    List<oldproduit> findByCategoreAndOutil4(@Param("outil4") Outil outil4, @Param("categore") Categore categore);

    /*******************************************************************************/
    @Query("SELECT COUNT(categore) FROM oldproduit  WHERE categore ='webproject'")
    float getNumberWebproject();
    @Query("SELECT COUNT(categore) FROM oldproduit  WHERE categore ='mobilewebprojects'")
    float getNumbermobilewebprojects();
    @Query("SELECT COUNT(categore) FROM oldproduit  WHERE categore ='designgraphique'")
    float getNumberdesigngraphique();

    @Query("SELECT COUNT(outil) FROM oldproduit  WHERE outil ='React'")
    float getNumberReact();
    @Query("SELECT COUNT(outil) FROM oldproduit  WHERE outil ='Angular'")
    float getNumberAngular();
    @Query("SELECT COUNT(outil) FROM oldproduit  WHERE outil ='Vue'")
    float getNumberVue();
    @Query("SELECT COUNT(outil) FROM oldproduit  WHERE outil ='Svelte'")
    float getNumberSvelte();
    @Query("SELECT COUNT(outil) FROM oldproduit  WHERE outil ='jQuery'")
    float getNumberjQuery();
    @Query("SELECT COUNT(outil) FROM oldproduit  WHERE outil ='Ember'")
    float getNumberEmber();
    @Query("SELECT COUNT(outil) FROM oldproduit  WHERE outil ='Backbone'")
    float getNumberBackbone();
    @Query("SELECT COUNT(outil) FROM oldproduit  WHERE outil ='SemanticUI'")
    float getNumberSemanticUI();
    @Query("SELECT COUNT(outil) FROM oldproduit  WHERE outil ='Foundation'")
    float getNumberFoundation();
    @Query("SELECT COUNT(outil) FROM oldproduit  WHERE outil ='Preact'")
    float getNumberPreact();

    @Query("SELECT COUNT(outil1) FROM oldproduit  WHERE outil1 ='Django'")
    float getNumberDjango();
    @Query("SELECT COUNT(outil1) FROM oldproduit  WHERE outil1 ='ExpressJS'")
    float getNumberExpressJS();
    @Query("SELECT COUNT(outil1) FROM oldproduit  WHERE outil1 ='Laravel'")
    float getNumberLaravel();
    @Query("SELECT COUNT(outil1) FROM oldproduit  WHERE outil1 ='Ruby'")
    float getNumberRuby();
    @Query("SELECT COUNT(outil1) FROM oldproduit  WHERE outil1 ='CakePHP'")
    float getNumberCakePHP();
    @Query("SELECT COUNT(outil1) FROM oldproduit  WHERE outil1 ='Flask'")
    float getNumberFlask();
    @Query("SELECT COUNT(outil1) FROM oldproduit  WHERE outil1 ='ASPDOTNETCore'")
    float getNumberASPDOTNETCore();
    @Query("SELECT COUNT(outil1) FROM oldproduit  WHERE outil1 ='SpringBoot'")
    float getNumberSpringBoot();
    @Query("SELECT COUNT(outil1) FROM oldproduit  WHERE outil1 ='Koa'")
    float getNumberKoa();
    @Query("SELECT COUNT(outil1) FROM oldproduit  WHERE outil1 ='Phoenix'")
    float getNumberPhoenix();

    /*******************************************************************************/


}
