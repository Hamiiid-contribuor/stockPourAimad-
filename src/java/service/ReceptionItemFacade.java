/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Abonne;
import bean.AvoirReception;
import bean.AvoirReceptionItem;
import bean.Commande;
import bean.CommandeItem;
import bean.Produit;
import bean.Reception;
import bean.ReceptionItem;
import java.math.BigDecimal;
import java.util.Iterator;
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
public class ReceptionItemFacade extends AbstractFacade<ReceptionItem> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;
    private @EJB
    ReceptionFacade receptionFacade;
    private @EJB
    CommandeItemFacade commandeItemFacade;
    private @EJB
    StockFacade stockFacade;
    private @EJB
    ProduitFacade produitFacade;
    private @EJB
    CommandeFacade commandeFacade;

    public List<ReceptionItem> findReceptionItemsByReception(Reception reception) {
        return em.createQuery("SELECT ri FROM ReceptionItem ri WHERE ri.reception.id=" + reception.getId() + "").getResultList();
    }

    public List<ReceptionItem> findReceptionItemsNonAvoirByReception(Reception reception) {
        return em.createQuery("SELECT ri FROM ReceptionItem ri WHERE ri.qte>ri.qteAvoir AND ri.reception.id=" + reception.getId() + "").getResultList();
    }

    @Override
    public void remove(ReceptionItem receptionItem) {
        em.createNativeQuery("DELETE FROM receptionitem WHERE id=" + receptionItem.getId()).executeUpdate();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReceptionItemFacade() {
        super(ReceptionItem.class);
    }

    public void updateQteAvoirReceptionItem(AvoirReceptionItem avoirReceptionItem, boolean isSave) {
        ReceptionItem receptionItem = (ReceptionItem) findReceptionItemByProduit(avoirReceptionItem.getProduit(), avoirReceptionItem.getAvoirReception().getReception().getReceptionItems())[1];
        if (isSave) {
            receptionItem.setQteAvoir(receptionItem.getQteAvoir().add(avoirReceptionItem.getQte()));
        } else {
            receptionItem.setQteAvoir(receptionItem.getQteAvoir().subtract(avoirReceptionItem.getQte()));
        }
        edit(receptionItem);
    }

    void updateQteAvoirReceptionItems(AvoirReception avoirReception, boolean isSave) {
        for (Iterator<AvoirReceptionItem> it = avoirReception.getAvoirReceptionItems().iterator(); it.hasNext();) {
            AvoirReceptionItem avoirReceptionItem = it.next();
            updateQteAvoirReceptionItem(avoirReceptionItem, isSave);
        }
    }

    public ReceptionItem clone(ReceptionItem receptionItem) {
        ReceptionItem clonedReceptionItem = new ReceptionItem();
        clonedReceptionItem.setMagasin(receptionItem.getMagasin());
        clonedReceptionItem.setProduit(receptionItem.getProduit());
        clonedReceptionItem.setQte(receptionItem.getQte());
        clonedReceptionItem.setQteAvoir(receptionItem.getQteAvoir());
        return clonedReceptionItem;
    }

    public Object[] findReceptionItemByProduit(Produit produit, List<ReceptionItem> receptionItems) {
        int index = 0;
        for (Iterator<ReceptionItem> it = receptionItems.iterator(); it.hasNext();) {
            ReceptionItem receptionItem = it.next();
            if (receptionItem.getProduit().equals(produit)) {
                return new Object[]{index, receptionItem};
            }
            index++;
        }
        return new Object[]{-1, null};
    }

    public void removeReceptionItem(Commande commande, Reception reception, ReceptionItem receptionItem, Abonne abonne, boolean isSave, boolean commit) {
        removeReceptionItemRequierePrepare(receptionItem, false, commit);
    }

    public void prepareCreateOrRemove(Commande commande, Reception reception, ReceptionItem receptionItem, CommandeItem myCommandeItem, Abonne abonne, boolean isSave) {
        receptionFacade.prepareCreateOrRemove(commande, reception, abonne, isSave, false, false);
        CommandeItem commandeItem;
        if (myCommandeItem == null) {
            commandeItem = commandeItemFacade.findCommandeItemByProduit(commande, receptionItem.getProduit());
        } else {
            commandeItem = myCommandeItem;
        }
        if (commande.getCommandeItems().indexOf(commandeItem) == -1) {
            commande.getCommandeItems().add(commandeItem);
        }
        commandeItem.setCommande(commande);
        receptionItem.setReception(reception);
        if (reception.getReceptionItems().indexOf(receptionItem) == -1) {
            reception.getReceptionItems().add(receptionItem);
        }
    }

    public int verifyRemoveReceptionItem(Commande commande, Reception reception, ReceptionItem receptionItem, CommandeItem myCommandeItem, Abonne abonne, boolean prepare) {
        if (prepare) {
            prepareCreateOrRemove(commande, reception, receptionItem, myCommandeItem, abonne, false);
        }
        if (commandeItemFacade.updateQteRecuCommandeItem(receptionItem, false, true) < 0) {
            return -1;
        } else if (stockFacade.updateOrCreateStock(receptionItem, false, true) < 0) {
            return -2;
        } else if (produitFacade.updateQteGlobalProduit(receptionItem, false, true) < 0) {
            return -3;
        }
        return 1;
    }

    public Object[] verifyRemoveReceptionItemsOfCommandeItem(Commande commande, CommandeItem commandeItem, Abonne abonne) {
        List<ReceptionItem> receptionItems = findByCommandeItem(commandeItem);
        for (Iterator<ReceptionItem> it = receptionItems.iterator(); it.hasNext();) {
            ReceptionItem receptionItem = it.next();
            Reception reception = receptionFacade.findReceptionByReceptionItem(receptionItem);
            int resVerifyRemoveReceptionItem = verifyRemoveReceptionItem(commande, reception, receptionItem, commandeItem, abonne, true);
            if (resVerifyRemoveReceptionItem < 0) {
                return new Object[]{resVerifyRemoveReceptionItem, receptionItem};
            }
        }
        return new Object[]{1, null};

    }

    public Object[] verifyRemoveReceptionItemsOfReception(Commande commande, Reception reception, Abonne abonne) {
        List<ReceptionItem> receptionItems = findReceptionItemsByReception(reception);
        for (Iterator<ReceptionItem> it = receptionItems.iterator(); it.hasNext();) {
            ReceptionItem receptionItem = it.next();
            int resVerifyRemoveReceptionItem = verifyRemoveReceptionItem(commande, reception, receptionItem, null, abonne, true);
            if (resVerifyRemoveReceptionItem < 0) {
                return new Object[]{resVerifyRemoveReceptionItem, receptionItem};
            }
        }
        return new Object[]{1, null};

    }

    private void removeReceptionItemRequierePrepare(ReceptionItem receptionItem, boolean downloadReception, boolean commit) {
        if (downloadReception) {
            receptionItem.setReception(receptionFacade.findReceptionByReceptionItem(receptionItem));
        }
        commandeItemFacade.updateQteRecuCommandeItem(receptionItem, false, false);
        stockFacade.updateOrCreateStock(receptionItem, false, false);
        produitFacade.updateQteGlobalProduit(receptionItem, false, false);
        commandeFacade.updateMontantTotalAndEtatReceptionCommande(receptionItem, false);
        receptionFacade.updateMontantTotalReception(receptionItem, false, commit);
        saveOrDeleteReceptionItem(receptionItem.getReception(), receptionItem, false);
    }

    public void removeReceptionItems(CommandeItem commandeItem) {
        List<ReceptionItem> receptionItems = findByCommandeItem(commandeItem);
        for (Iterator<ReceptionItem> it = receptionItems.iterator(); it.hasNext();) {
            ReceptionItem receptionItem = it.next();
            removeReceptionItemRequierePrepare(receptionItem, true, true);
        }

    }

    public void saveOrDeleteReceptionItems(Reception reception, boolean isSave) {
        for (Iterator<ReceptionItem> it = reception.getReceptionItems().iterator(); it.hasNext();) {
            ReceptionItem receptionItem = it.next();
            saveOrDeleteReceptionItem(reception, receptionItem, isSave);
        }

    }

    private int saveOrDeleteReceptionItem(Reception reception, ReceptionItem receptionItem, boolean isSave) {
        int res = 0;
        if (isSave) {
            receptionItem.setReception(reception);
            create(receptionItem);
            res = 1;
        } else {
            int index = reception.getReceptionItems().indexOf(receptionItem);
            if (index != -1) {
                // reception.getReceptionItems().remove(index);
                remove(receptionItem);
                res = 2;
            } else {
                res = -2;
            }
        }
        return res;
    }

    public List<ReceptionItem> findByCommandeItem(CommandeItem commandeItem) {
        return em.createQuery("SELECT recitm FROM ReceptionItem recitm WHERE recitm.produit.id="
                + commandeItem.getProduit().getId() + " AND recitm.reception.commande.id="
                + commandeItem.getCommande().getId()).getResultList();
    }

    public ReceptionItem findByReceptionAndProduit(Reception reception, Produit produit) {
        return (ReceptionItem) em.createQuery("SELECT recitm FROM ReceptionItem recitm WHERE recitm.produit.id="
                + produit.getId() + " AND recitm.reception.id="
                + reception.getId()).getSingleResult();
    }

    public void updateQteAvoirReceptionItem(Reception reception, AvoirReceptionItem avoirReceptionItem, boolean isSave) {
        ReceptionItem receptionItem = (ReceptionItem) findReceptionItemByProduit(avoirReceptionItem.getProduit(), reception.getReceptionItems())[1];
        if (isSave) {
            receptionItem.setQteAvoir(receptionItem.getQteAvoir().add(avoirReceptionItem.getQte()));
        } else {
            receptionItem.setQteAvoir(receptionItem.getQteAvoir().subtract(avoirReceptionItem.getQte()));
        }
        edit(receptionItem);
    }

    public int addReceptionItemToReception(ReceptionItem receptionItem, Reception reception, CommandeItem commandeItem) {
        if (receptionItem.getQte().compareTo(BigDecimal.ZERO) <= 0) {
            return -1;
        } else if (receptionItem.getQte().compareTo((commandeItem.getQte().subtract(commandeItem.getQteRecu()))) > 0) {
            return -2;
        }
        List<ReceptionItem> receptionItems = reception.getReceptionItems();
        Object[] myReceptionItemObject = findReceptionItemByProduit(receptionItem.getProduit(), receptionItems);
        if (myReceptionItemObject[1] != null) {
            return -3;
        }
        reception.setMontantTotal(reception.getMontantTotal().add((receptionItem.getQte().multiply(commandeItem.getPrix()))));
        reception.getReceptionItems().add(clone(receptionItem));
        return 1;
    }

    public int removeReceptionItemFromReception(ReceptionItem receptionItem, Reception reception) {
        List<ReceptionItem> receptionItems = reception.getReceptionItems();
        Object[] myReceptionItemObject = findReceptionItemByProduit(receptionItem.getProduit(), receptionItems);
        int index = new Integer(myReceptionItemObject[0] + "");
        if (index == -1) {
            return -1;
        }
        reception.getReceptionItems().remove(index);
        CommandeItem commandeItem = (CommandeItem) commandeItemFacade.findCommandeItemByProduit(reception.getCommande().getCommandeItems(), receptionItem.getProduit())[1];
        reception.setMontantTotal(reception.getMontantTotal().subtract((receptionItem.getQte().multiply(commandeItem.getPrix()))));
        return 1;
    }

//    void associate(Reception reception, List<ReceptionItem> receptionItems) {
//        for (ReceptionItem receptionItem : receptionItems) {
//            receptionItem.setReception(reception);
//        }
//    }
}
