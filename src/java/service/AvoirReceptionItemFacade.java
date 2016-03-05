/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.AvoirReception;
import bean.AvoirReceptionItem;
import bean.CommandeItem;
import bean.Produit;
import bean.Reception;
import bean.ReceptionItem;
import java.math.BigDecimal;
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
public class AvoirReceptionItemFacade extends AbstractFacade<AvoirReceptionItem> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;
    private @EJB
    ReceptionFacade receptionFacade;
    private @EJB
    ReceptionItemFacade receptionItemFacade;
    private @EJB
    CommandeItemFacade commandeItemFacade;
    private @EJB
    StockFacade stockFacade;
    private @EJB
    ProduitFacade produitFacade;
    private @EJB
    CommandeFacade commandeFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AvoirReceptionItemFacade() {
        super(AvoirReceptionItem.class);
    }

    public AvoirReceptionItem clone(AvoirReceptionItem avoirReceptionItem) {
        AvoirReceptionItem clonedaAvoirReceptionItem = new AvoirReceptionItem();
        clonedaAvoirReceptionItem.setMagasin(avoirReceptionItem.getMagasin());
        clonedaAvoirReceptionItem.setProduit(avoirReceptionItem.getProduit());
        clonedaAvoirReceptionItem.setQte(avoirReceptionItem.getQte());
        return clonedaAvoirReceptionItem;
    }

    public Object[] findAvoirReceptionItemByProduit(Produit produit, List<AvoirReceptionItem> avoirReceptionItems) {
        int index = 0;
        for (AvoirReceptionItem avoirReceptionItem : avoirReceptionItems) {
            if (avoirReceptionItem.getProduit().equals(produit)) {
                return new Object[]{index, avoirReceptionItem};
            }
            index++;
        }
        return new Object[]{-1, null};

    }

    public void removeAvoirReceptionItem(AvoirReceptionItem avoirReceptionItem, boolean commit) {
        commandeItemFacade.updateQteAvoirCommandeItem(avoirReceptionItem, false);
        stockFacade.updateOrCreateStock(avoirReceptionItem, false,false);
        produitFacade.updateQteGlobalProduit(avoirReceptionItem, false,false);
        commandeFacade.updateMontantTotalAndEtatReceptionCommande(avoirReceptionItem, false);
        receptionFacade.updateQteAvoirReceptionItem(avoirReceptionItem, false, commit);
        saveOrDeleteAvoirReceptionItem(avoirReceptionItem, false);
    }

    public void removeAvoirReceptionItems(CommandeItem commandeItem) {
        List<AvoirReceptionItem> avoirReceptionItems = findByCommandeItem(commandeItem);
        for (AvoirReceptionItem avoirReceptionItem : avoirReceptionItems) {
            removeAvoirReceptionItem(avoirReceptionItem, true);
        }
    }

    public void saveOrDeleteAvoirReceptionItems(AvoirReception avoirReception, boolean isSave) {
        for (AvoirReceptionItem avoirReceptionItem : avoirReception.getAvoirReceptionItems()) {
            saveOrDeleteAvoirReceptionItem(avoirReceptionItem, isSave);
        }

    }

    private void saveOrDeleteAvoirReceptionItem(AvoirReceptionItem avoirReceptionItem, boolean isSave) {
        if (isSave) {
            create(avoirReceptionItem);
        } else {
            remove(avoirReceptionItem);
        }
    }

    public List<AvoirReceptionItem> findByCommandeItem(CommandeItem commandeItem) {
        return em.createQuery("SELECT avrecitm FROM AvoirReceptionItem avrecitm WHERE avrecitm.produit.id="
                + commandeItem.getProduit().getId() + " AND avrecitm.avoirRreception.reception.commande.id="
                + commandeItem.getCommande().getId()).getResultList();
    }

    public int addAvoirReceptionItemToAvoirReception(AvoirReceptionItem avoirReceptionItem, AvoirReception avoirReception, ReceptionItem receptionItem) {
        if (avoirReceptionItem.getQte().compareTo(BigDecimal.ZERO) <= 0) {
            return -1;
        }
        if ((receptionItem.getQte().subtract(receptionItem.getQteAvoir())).compareTo(avoirReceptionItem.getQte()) < 0) {
            return -2;
        }
        List<AvoirReceptionItem> avoirReceptionItems = avoirReception.getAvoirReceptionItems();
        Object[] myAvoirReceptionItemObject = findAvoirReceptionItemByProduit(avoirReceptionItem.getProduit(), avoirReceptionItems);
        if (myAvoirReceptionItemObject[1] != null) {
            return -3;
        }
        CommandeItem commandeItem = (CommandeItem) commandeItemFacade.findCommandeItemByProduit(avoirReception.getReception().getCommande().getCommandeItems(), avoirReceptionItem.getProduit())[1];
        avoirReception.setMontant(avoirReception.getMontant().add((avoirReceptionItem.getQte().multiply(commandeItem.getPrix()))));
        avoirReception.getAvoirReceptionItems().add(clone(avoirReceptionItem));
        return 1;
    }

    public int removeAvoirReceptionItemFromAvoirReception(AvoirReceptionItem avoirReceptionItem, AvoirReception avoirReception) {
        List<AvoirReceptionItem> avoirReceptionItems = avoirReception.getAvoirReceptionItems();
        Object[] myAvoirReceptionItemObject = findAvoirReceptionItemByProduit(avoirReceptionItem.getProduit(), avoirReceptionItems);
        int index = new Integer(myAvoirReceptionItemObject[0] + "");
        if (index == -1) {
            return -1;
        }
        avoirReception.getAvoirReceptionItems().remove(index);
        CommandeItem commandeItem = (CommandeItem) commandeItemFacade.findCommandeItemByProduit(avoirReception.getReception().getCommande().getCommandeItems(), avoirReceptionItem.getProduit())[1];
        avoirReception.setMontant(avoirReception.getMontant().subtract((avoirReceptionItem.getQte().multiply(commandeItem.getPrix()))));
        return 1;
    }

    void associate(AvoirReception avoirReception, List<AvoirReceptionItem> avoirReceptionItems) {
        for (AvoirReceptionItem avoirReceptionItem : avoirReceptionItems) {
            avoirReceptionItem.setAvoirReception(avoirReception);
        }
    }

}
