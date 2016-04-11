/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.DevisDemmande;
import bean.DevisDemmandeItem;
import bean.Produit;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author moulaYounes
 */
@Stateless
public class DevisDemmandeItemFacade extends AbstractFacade<DevisDemmandeItem> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DevisDemmandeItemFacade() {
        super(DevisDemmandeItem.class);
    }

    @EJB
    private DevisDemmandeFacade devisDemmandeFacade;

    public DevisDemmandeFacade getDevisDemmandeFacade() {
        return devisDemmandeFacade;
    }

    public void setDevisDemmandeFacade(DevisDemmandeFacade devisDemmandeFacade) {
        this.devisDemmandeFacade = devisDemmandeFacade;
    }

    //****************************hamid*********************
    public void createDevisDemmandeItemsOnGlobal(DevisDemmande devisDemmande) {
        List<DevisDemmandeItem> devisDemmandeItems = devisDemmande.getDevisDemmandeItems();
        for (int i = 0; i < devisDemmandeItems.size(); i++) {
            DevisDemmandeItem item = devisDemmandeItems.get(i);
            item.setDevisDemmande(devisDemmande);
            // attacher le devisCommande a chaque item 
            create(item);
        }
    }

    public void createDevisCommandeItemForAnExistingDevisDemmande(DevisDemmandeItem devisDemmandeItem, DevisDemmande devisDemmande) {

        devisDemmandeItem.setDevisDemmande(devisDemmande); // on attache l'item a son devisDemmande
        devisDemmandeItem.getDevisDemmande().setMontantTotal(devisDemmandeItem.getDevisDemmande().getMontantTotal().add(devisDemmandeItem.getPrix().multiply(devisDemmandeItem.getQte())));
        // on augment le montantTotale du devisCommande 
        create(devisDemmandeItem);// on envoye l'item au dataBase 
        devisDemmandeFacade.edit(devisDemmandeItem.getDevisDemmande());
        // on met a jour notre devisCommande (changement au niveu montantTotal)
    }

    public DevisDemmandeItem cloneDevisDemmande(DevisDemmande devisDemmande, DevisDemmandeItem devisDemmandeItemToBeCloned, Produit produit) {
        DevisDemmandeItem devisDemmandeItem = new DevisDemmandeItem();
        devisDemmandeItem.setProduit(produit);
        devisDemmandeItem.setDevisDemmande(devisDemmande);
        devisDemmandeItem.setPrix(devisDemmandeItemToBeCloned.getPrix());
        devisDemmandeItem.setQte(devisDemmandeItemToBeCloned.getQte());
        return devisDemmandeItem;
    }

    public List<DevisDemmandeItem> findDevisDemmandeItemsByIdDevisDemmande(DevisDemmande devisDemmande) {
        System.out.println("SELECT ddmdi FROM DevisDemmandeItem ddmdi WHERE ddmdi.devisDemmande.id=" + devisDemmande.getId());
        return em.createQuery("SELECT ddmdi FROM DevisDemmandeItem ddmdi WHERE ddmdi.devisDemmande.id=" + devisDemmande.getId()).getResultList();
    }

    public void removeDevisDemmandeItem(DevisDemmandeItem devisDemmandeItem) {
        devisDemmandeItem.getDevisDemmande().setMontantTotal(devisDemmandeItem.getDevisDemmande().getMontantTotal().subtract((devisDemmandeItem.getPrix().multiply(devisDemmandeItem.getQte()))));
        devisDemmandeFacade.edit(devisDemmandeItem.getDevisDemmande());
        remove(devisDemmandeItem);
    }

    public void removeDevisDemmandeItemFromDevisDemmande(DevisDemmande devisDemmande, DevisDemmandeItem devisDemmandeItem) {
        devisDemmande.getDevisDemmandeItems().remove(devisDemmande.getDevisDemmandeItems().indexOf(devisDemmandeItem));//remove from list 
        devisDemmande.setMontantTotal(devisDemmande.getMontantTotal().subtract(devisDemmandeItem.getPrix().multiply(devisDemmandeItem.getQte())));//mise ajour du montant totale 
        devisDemmandeFacade.edit(devisDemmande); //update devisCommande
        remove(devisDemmandeItem); //remmove item from database
    }

    public void removeAllDevisDemmandeItemsFromDevisDemmande(DevisDemmande devisDemmande) {
       String requette = "DELETE from DevisDemmandeItem ddi WHERE ddi.devisDemmande.id = "+devisDemmande.getId();
       em.createQuery(requette).executeUpdate();
    }
}
