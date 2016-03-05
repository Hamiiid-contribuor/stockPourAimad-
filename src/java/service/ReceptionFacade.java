/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Abonne;
import bean.AvoirReceptionItem;
import bean.Commande;
import bean.CommandeItem;
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
 * @author abdelmoughit
 */
@Stateless
public class ReceptionFacade extends AbstractFacade<Reception> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;
    @EJB
    private CommandeItemFacade commandeItemFacade;
    @EJB
    private CommandeFacade commandeFacade;
    @EJB
    private ReceptionItemFacade receptionItemFacade;
    @EJB
    private StockFacade stockFacade;
    @EJB
    private ProduitFacade produitFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReceptionFacade() {
        super(Reception.class);
    }

    public void save(Commande commande, Reception reception, Abonne abonne) {
        prepareCreateOrRemove(commande, reception, abonne, true, true, false);
        save(reception);
    }

//    public void remove(Commande commande, Reception reception, Abonne abonne) {
//        removeRequierePrepare(reception);
//    }
    public Object[] verifyRemove(Commande commande, Reception reception, Abonne abonne) {
        prepareCreateOrRemove(commande, reception, abonne, false, true, true);
        if (commande.getReceptions().indexOf(reception) == -1) {
            commande.getReceptions().add(reception);
        }
        Object[] resVerifyRemoveReceptionItem = receptionItemFacade.verifyRemoveReceptionItemsOfReception(commande, reception, abonne);
        if (new Integer(resVerifyRemoveReceptionItem[0] + "") < 0) {
            return resVerifyRemoveReceptionItem;
        }
        return new Object[]{1, null};

    }

    public void removeRequierePrepare(Reception reception) {
        commandeItemFacade.updateQteRecuCommandeItems(reception, false);
        stockFacade.updateStock(reception, false, false);
        produitFacade.updateQteGlobalProduit(reception, false, false);
        commandeFacade.updateMontantTotalAndEtatReceptionCommande(reception, false);
        receptionItemFacade.saveOrDeleteReceptionItems(reception, false);
        removeUsingNativeQuery(reception);
    }

    public void prepareCreateOrRemove(Commande commande, Reception reception, Abonne abonne, boolean isSave, boolean downloadCommandeItems, boolean downloadReceptionItems) {
        if (isSave) {
            reception.setId(generateId());
        }
        reception.setCommande(commande);
        reception.getCommande().setAbonne(abonne);
        if (downloadCommandeItems) {
            reception.getCommande().setCommandeItems(commandeItemFacade.findCommadeItemsByIdCmd(reception.getCommande()));
            for (Iterator<CommandeItem> it = reception.getCommande().getCommandeItems().iterator(); it.hasNext();) {
                CommandeItem commandeItem = it.next();
                commandeItem.setCommande(commande);
            }
        }
        if (downloadReceptionItems) {
            reception.setReceptionItems(receptionItemFacade.findReceptionItemsByReception(reception));
            for (Iterator<ReceptionItem> it = reception.getReceptionItems().iterator(); it.hasNext();) {
                ReceptionItem receptionItem = it.next();
                receptionItem.setReception(reception);
            }
        }
    }

    private void save(Reception reception) {
        super.create(reception);
        receptionItemFacade.saveOrDeleteReceptionItems(reception, true);
        commandeItemFacade.updateQteRecuCommandeItems(reception, true);
        stockFacade.updateStock(reception, true, false);
        produitFacade.updateQteGlobalProduit(reception, true, false);
        commandeFacade.updateMontantTotalAndEtatReceptionCommande(reception, true);
    }

    private Long generateId() {
        Long maxId = (Long) em.createQuery("SELECT MAX(r.id) FROM Reception r").getSingleResult();
        return (maxId == null ? 1l : maxId + 1);
    }

    public List<Reception> findReceptionByCommande(Commande commande) {
        return em.createQuery("SELECT r FROM Reception r WHERE r.commande.id=" + commande.getId() + "").getResultList();
    }

    public Reception findReceptionByReceptionItem(ReceptionItem receptionItem) {
        return (Reception) em.createQuery("SELECT r FROM Reception r, ReceptionItem ritem WHERE "
                + "ritem.reception.id=r.id AND ritem.id=" + receptionItem.getId() + "").getSingleResult();
    }

    public List<Reception> findReceptionByCommandeItem(CommandeItem commandeItem) {
        return (List<Reception>) em.createQuery("SELECT r FROM Reception r, ReceptionItem ritem, Commande c, CommandeItem citem WHERE "
                + "ritem.reception.id=r.id AND citem.commande.id=c.id AND citem.produit.id=ritem.produit.id AND "
                + "citem.id=" + commandeItem.getId() + "").getSingleResult();
    }

    public void removeUsingNativeQuery(Reception reception) {
        System.out.println("DELETE FROM reception WHERE id=" + reception.getId());
        em.createNativeQuery("DELETE FROM reception WHERE id=" + reception.getId()).executeUpdate();
    }

    public int updateMontantTotalReception(ReceptionItem receptionItem, boolean isSave, boolean commit) {
        Reception reception = receptionItem.getReception();
        CommandeItem commandeItem = (CommandeItem) commandeItemFacade.findCommandeItemByProduit(reception.getCommande().getCommandeItems(), receptionItem.getProduit())[1];
        BigDecimal montantTotal = null;
        int res = 0;
        if (isSave) {
            montantTotal = reception.getMontantTotal().add((commandeItem.getPrix().multiply(receptionItem.getQte())));
            res = 1;
        } else {
            montantTotal = reception.getMontantTotal().subtract((commandeItem.getPrix().multiply(receptionItem.getQte())));
            if (montantTotal.compareTo(BigDecimal.ZERO) >= 0) {
                res = 2;
            } else {
                res = -1;
            }
        }
        if (res > 0) {
            reception.setMontantTotal(montantTotal);
            if (commit) {
                edit(reception);
            }
        }
        return res;
    }

    void updateQteAvoirReceptionItem(AvoirReceptionItem avoirReceptionItem, boolean isSave, boolean commit) {
        Reception reception = avoirReceptionItem.getAvoirReception().getReception();
        ReceptionItem receptionItem = (ReceptionItem) receptionItemFacade.findReceptionItemByProduit(avoirReceptionItem.getProduit(), reception.getReceptionItems())[1];
        if (isSave) {
            receptionItem.setQteAvoir(receptionItem.getQteAvoir().add(avoirReceptionItem.getQte()));
        } else {
            receptionItem.setQteAvoir(receptionItem.getQteAvoir().subtract(avoirReceptionItem.getQte()));
        }
        if (commit) {
            receptionItemFacade.edit(receptionItem);
        }
    }

}
