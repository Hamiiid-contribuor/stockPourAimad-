/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Abonne;
import bean.AvoirReception;
import bean.AvoirReceptionItem;
import bean.CommandeItem;
import bean.Famille;
import bean.Magasin;
import bean.Produit;
import bean.Reception;
import bean.ReceptionItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author moulaYounes
 */
@Stateless
public class ProduitFacade extends AbstractFacade<Produit> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;
    private @EJB
    FamilleFacade familleFacade;

    private int updateQteGlobalProduit(Produit produit, BigDecimal qte, boolean isSave, boolean checkMode) {
        int res = 0;
        if (isSave) {
            if (!checkMode) {
                produit.setQteGlobalStock(produit.getQteGlobalStock().add(qte));
            }
            res = 1;
        } else {
            if (produit.getQteGlobalStock().compareTo(qte) >= 0) {
                System.out.println("updateOrCreateStock ==> produit.getQteGlobalStock().compareTo(qte) >= 0 : true");
                if (!checkMode) {
                    produit.setQteGlobalStock(produit.getQteGlobalStock().subtract(qte));
                }
                res = 2;
            } else {
                System.out.println("updateOrCreateStock ==> produit.getQteGlobalStock().compareTo(qte) >= 0 : false");
                res = -1;
            }
        }
        if (res >= 0 && !checkMode) {
            edit(produit);
        }
        return res;
    }

    public int updateQteGlobalProduit(AvoirReceptionItem avoirReceptionItem, boolean isSave, boolean checkMode) {
        return updateQteGlobalProduit(avoirReceptionItem.getProduit(), avoirReceptionItem.getQte(), !isSave, checkMode);
    }

    public int updateQteGlobalProduit(ReceptionItem receptionItem, boolean isSave, boolean checkMode) {
        return updateQteGlobalProduit(receptionItem.getProduit(), receptionItem.getQte(), isSave, checkMode);
    }

    public void updateQteGlobalProduit(Reception reception, boolean isSave, boolean checkMode) {
        for (ReceptionItem receptionItem : reception.getReceptionItems()) {
            updateQteGlobalProduit(receptionItem, isSave, checkMode);
        }
    }

    public void updateQteGlobalProduit(AvoirReception avoirReception, boolean isSave, boolean checkMode) {
        for (AvoirReceptionItem avoirReceptionItem : avoirReception.getAvoirReceptionItems()) {
            updateQteGlobalProduit(avoirReceptionItem, isSave, checkMode);
        }
    }

    public List<Produit> findByAbonne(Abonne abonne, int deleted) {
        if (abonne != null && abonne.getId() != null) {
            String request = "SELECT pr FROM Produit pr WHERE pr.abonne.id=" + abonne.getId();
//            if (deleted != -1) {
//                request += " and pr.supprimer=" + deleted;
//            }
            System.out.println("List<Produit> findByAbonne ==> " + request);
            return em.createQuery(request).getResultList();
        }
        return new ArrayList();

    }

    public List<Produit> findProduitByMagasinAndFamille(Magasin magasin, Famille famille, int deleted) {
        List<Produit> myProduits = new ArrayList();
        if (magasin != null) {
            List<Object[]> results = em.createQuery(findProduitByMagasinAndFamilleRequest(magasin, famille, deleted)).getResultList();
            for (Object[] result : results) {
                Produit produit = new Produit((Long) result[0], (String) result[1], (String) result[2], (String) result[3], (BigDecimal) result[4], (BigDecimal) result[5]);
                myProduits.add(produit);
            }
        }
        return myProduits;
    }

    public List<Produit> findProduitByFamille(Famille famille, int deleted) {
        List<Produit> myProduits = new ArrayList();
        if (famille != null) {
            List<Object[]> results = em.createQuery(findProduitByMagasinAndFamilleRequest(null, famille, deleted)).getResultList();
            for (Object[] result : results) {
                Produit produit = new Produit((Long) result[0], (String) result[1], (String) result[2], (String) result[3], (BigDecimal) result[4]);
                myProduits.add(produit);
            }
        }
        return myProduits;
    }

    private String findProduitByMagasinAndFamilleRequest(Magasin magasin, Famille famille, int deleted) {
        String request = null;
        if (magasin != null && magasin.getId() != null) {
            request = "SELECT pr.id,pr.reference,pr.libelle , pr.uniteMesure.nom ,pr.qteGlobalStock,st.qte FROM Produit pr "
                    + " , Stock st, UniteMesure unm WHERE unm.id=pr.uniteMesure.id AND st.produit.id=pr.id "
                    + " AND st.magasin.id=" + magasin.getId();
        } else {
            request = "SELECT pr.id,pr.reference,pr.libelle , pr.uniteMesure.nom ,pr.qteGlobalStock FROM Produit pr "
                    + " , UniteMesure unm WHERE unm.id=pr.uniteMesure.id ";
        }
        if (famille != null && famille.getId() != null) {
            request += " AND pr.famille.id=" + famille.getId();
        }
//        if (deleted != -1) {
//            request += " AND pr.supprimer=" + deleted;
//        }
        System.out.println(" findProduitByMagasinAndFamilleRequest=> request=" + request);
        return (request);
    }

    private List<Produit> extractProduitFromCommandeItems(List<CommandeItem> commandeItems) {
        List<Produit> produits = new ArrayList<>();
        for (CommandeItem commandeItem : commandeItems) {
            produits.add(commandeItem.getProduit());
        }
        return produits;
    }

    private List<Produit> extractProduitFromReceptionItems(List<ReceptionItem> receptionItems) {
        List<Produit> produits = new ArrayList<>();
        for (ReceptionItem receptionItem : receptionItems) {
            produits.add(receptionItem.getProduit());
        }
        return produits;
    }

    private int findIndexOfProduit(List<Produit> produits, Produit produit) {
        int i = 0;
        for (Produit myProduit : produits) {
            if (Objects.equals(produit.getId(), myProduit.getId())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public int findIndexOfProduitInCommandeItems(List<CommandeItem> commandeItems, Produit produit) {
        return findIndexOfProduit(extractProduitFromCommandeItems(commandeItems), produit);
    }

    public int positionOfProduitInReceptionItems(List<ReceptionItem> receptionItems, Produit produit) {
        return findIndexOfProduit(extractProduitFromReceptionItems(receptionItems), produit);
    }

    public Produit cloneProduitWithFamille(Produit produitToBeCloned, Famille familleToBeCloned) {
        Produit produit = new Produit();
        produit.setId(produitToBeCloned.getId());
        produit.setLibelle(produitToBeCloned.getLibelle());
        produit.setQteGlobalStock(produitToBeCloned.getQteGlobalStock());
        produit.setFamille(familleFacade.cloneFamille(familleToBeCloned));
        return produit;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProduitFacade() {
        super(Produit.class);
    }

}
