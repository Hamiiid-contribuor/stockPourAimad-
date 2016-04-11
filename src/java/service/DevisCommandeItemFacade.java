/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.DevisCommande;
import bean.DevisCommandeItem;
import bean.Produit;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author moulaYounes
 */
@Stateless
public class DevisCommandeItemFacade extends AbstractFacade<DevisCommandeItem> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DevisCommandeItemFacade() {
        super(DevisCommandeItem.class);
    }

    @EJB
    private DevisCommandeFacade devisCommandeFacade;

   

    private void associateItemsToDevisCommande(DevisCommande devisCommande, DevisCommandeItem devisCommandeItem) {
        devisCommandeItem.setDevisCommande(devisCommande);
        if (devisCommande.getDevisCommandeItems().indexOf(devisCommandeItem) == -1) {
            devisCommande.getDevisCommandeItems().add(devisCommandeItem);
        }
    }

    public void removeDevisCommandeItem(DevisCommandeItem devisCommandeItem) {
        devisCommandeItem.getDevisCommande().setMontantTotal(devisCommandeItem.getDevisCommande().getMontantTotal().subtract((devisCommandeItem.getPrix().multiply(devisCommandeItem.getQte()))));
        devisCommandeFacade.edit(devisCommandeItem.getDevisCommande());
        remove(devisCommandeItem);
    }

    public void removeDevisCommandeItemFromDevisCommande(DevisCommande devisCommande, DevisCommandeItem devisCommandeItem) {
        devisCommande.getDevisCommandeItems().remove(devisCommande.getDevisCommandeItems().indexOf(devisCommandeItem));//remove from list 
        devisCommande.setMontantTotal(devisCommande.getMontantTotal().subtract(devisCommandeItem.getPrix().multiply(devisCommandeItem.getQte())));//mise ajour du montant totale 
        devisCommandeFacade.edit(devisCommande); //update devisCommande
        remove(devisCommandeItem); //remmove item from database
    }
    
    public void removeAllDevisCommandeItemsFromDevisCommande(DevisCommande devisCommande) {
       em.createQuery("DELETE FROM DevisCommandeItem ditem WHERE ditem.devisCommande.id="+devisCommande.getId()).executeUpdate();
    }

    public void createDevisCommandeItemForAnExistingDevisCommande(DevisCommandeItem devisCommandeItem, DevisCommande devisCommande) {

        devisCommandeItem.setDevisCommande(devisCommande); // on attache l'item a son devisCommande
        devisCommandeItem.getDevisCommande().setMontantTotal(devisCommandeItem.getDevisCommande().getMontantTotal().add(devisCommandeItem.getPrix().multiply(devisCommandeItem.getQte())));
        // on augment le montantTotale du devisCommande 
        create(devisCommandeItem);// on envoye l'item au dataBase 
        devisCommandeFacade.edit(devisCommandeItem.getDevisCommande());
        // on met a jour notre devisCommande (changement au niveu montantTotal)
    }

    public void createDevisCommandeItemsOnGlobal(DevisCommande devisCommande) {
        List<DevisCommandeItem> devisCommandeItems = devisCommande.getDevisCommandeItems();
        for (int i = 0; i < devisCommandeItems.size(); i++) {
            DevisCommandeItem item = devisCommandeItems.get(i);
            item.setDevisCommande(devisCommande);
            // attacher le devisCommande a chaque item 
            create(item);
        }
    }
    
    public DevisCommandeItem cloneDevisCommande (DevisCommande devisCommande , DevisCommandeItem devisCommandeItemToBeCloned ,Produit produit){
        DevisCommandeItem devisCommandeItem = new DevisCommandeItem();
        devisCommandeItem.setProduit(produit);
        devisCommandeItem.setDevisCommande(devisCommande);
        devisCommandeItem.setPrix(devisCommandeItemToBeCloned.getPrix());
        devisCommandeItem.setQte(devisCommandeItemToBeCloned.getQte());
        return devisCommandeItem;
    }

    
     public List<DevisCommandeItem> findDevisCommandeItemsByIdDevisCommande(DevisCommande devisCommande) {
        System.out.println("SELECT dcmdi FROM DevisCommandeItem dcmdi WHERE dcmdi.devisCommande.id=" + devisCommande.getId());
        return em.createQuery("SELECT dcmdi FROM DevisCommandeItem dcmdi WHERE dcmdi.devisCommande.id=" + devisCommande.getId()).getResultList();
    }
}
