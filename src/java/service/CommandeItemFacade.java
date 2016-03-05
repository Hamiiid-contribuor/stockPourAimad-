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
import bean.Famille;
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
public class CommandeItemFacade extends AbstractFacade<CommandeItem> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;
    private @EJB
    ReceptionItemFacade receptionItemFacade;
    private @EJB
    ReceptionFacade receptionFacade;
    private @EJB
    CommandeFacade commandeFacade;
    private @EJB
    ProduitFacade produitFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Object[] verifyRemoveCommandeItem(CommandeItem commandeItem, Commande commande, Abonne abonne) {
        associate(commande, commandeItem);
        Object[] resVerifyRemoveReceptionItems = receptionItemFacade.verifyRemoveReceptionItemsOfCommandeItem(commande, commandeItem, abonne);
        if (new Integer(resVerifyRemoveReceptionItems[0] + "") < 0) {
            return resVerifyRemoveReceptionItems;
        }
        return new Object[]{1, null};

    }

//    public void prepare(CommandeItem commandeItem, Commande commande, Reception reception, ReceptionItem receptionItem, Abonne abonne, boolean isSave, boolean commit, boolean downloadReception) {
//        receptionItemFacade.prepareCreateOrRemove(commande, reception, receptionItem, abonne, isSave);
//        associate(commande, commandeItem);
//    }
    private void associate(Commande commande, CommandeItem commandeItem) {
        commandeItem.setCommande(commande);
        if (commande.getCommandeItems().indexOf(commandeItem) == -1) {
            commande.getCommandeItems().add(commandeItem);
        }
    }

    @Override
    public void remove(CommandeItem commandeItem) {
        receptionItemFacade.removeReceptionItems(commandeItem);
        commandeItem.getCommande().setMontantTotal(commandeItem.getCommande().getMontantTotal().subtract((commandeItem.getPrix().multiply(commandeItem.getQte()))));
        commandeFacade.edit(commandeItem.getCommande());
        super.remove(commandeItem);
    }

    public Object[] verifyRemoveCommandeItems(List<CommandeItem> commandeItems, Commande commande, Reception reception, Abonne abonne, boolean isSave, boolean commit, boolean downloadReception) {
//        for (CommandeItem commandeItem : commandeItems) {
//            commandeItem.setCommande(commande);
//            List<ReceptionItem> receptionItems = receptionItemFacade.findByCommandeItem(commandeItem);
//            Object[] resVerifyRemoveReceptionItems = verifyRemoveCommandeItem(commandeItem, commande, reception, null, abonne, isSave, commit, downloadReception)
//            if (new Integer(resVerifyRemoveReceptionItems[0] + "") < 0) {
//                return resVerifyRemoveReceptionItems;
//            }
//        }
        return new Object[]{1, null};

    }

    public void remove(Commande commande, List<CommandeItem> commandeItems) {
        for (CommandeItem commandeItem : commandeItems) {
            commandeItem.setCommande(commande);
            remove(commandeItem);
        }
    }

    public void createCommandeItemOfExistingCommande(CommandeItem commandeItem, Commande commande) {
        commandeItem.setCommande(commande);
        commande.setMontantTotal(commande.getMontantTotal().add(commandeItem.getPrix().multiply(commandeItem.getQte())));
        create(commandeItem);
        commandeFacade.edit(commandeItem.getCommande());
    }

    public void createCommandeItems(Commande commande) {
        List<CommandeItem> commandeItems = commande.getCommandeItems();
        for (CommandeItem commandeItem : commandeItems) {
            commandeItem.setCommande(commande);
            create(commandeItem);
        }
    }

    public CommandeItemFacade() {
        super(CommandeItem.class);
    }

    public void updateQteRecuCommandeItems(Reception reception, boolean isSave) {
        for (ReceptionItem receptionItem : reception.getReceptionItems()) {
            updateQteRecuCommandeItem(receptionItem, isSave, false);
        }
    }

    public void updateQteAvoirCommandeItems(AvoirReception avoirReception, boolean isSave) {
        for (AvoirReceptionItem avoirReceptionItem : avoirReception.getAvoirReceptionItems()) {
            updateQteAvoirCommandeItem(avoirReceptionItem, isSave);
        }
    }

    public int updateQteRecuCommandeItem(ReceptionItem receptionItem, boolean isSave, boolean checkMode) {
        Reception reception = receptionItem.getReception();
        List<CommandeItem> commandeItems = reception.getCommande().getCommandeItems();
        Produit produit = receptionItem.getProduit();
        CommandeItem commandeItem = (CommandeItem) findCommandeItemByProduit(commandeItems, produit)[1];
        int res = 0;
        if (isSave) {
            if (!checkMode) {
                commandeItem.setQteRecu(commandeItem.getQteRecu().add(receptionItem.getQte()));
            }
            res = 1;
        } else {
            if (commandeItem.getQteRecu().compareTo(receptionItem.getQte()) >= 0) {
                System.out.println("updateQteRecuCommandeItem  ==> commandeItem.getQteRecu().compareTo(receptionItem.getQte()) >= 0 == true");
                if (!checkMode) {
                    commandeItem.setQteRecu(commandeItem.getQteRecu().subtract(receptionItem.getQte()));
                }
                res = 2;
            } else {
                System.out.println("updateQteRecuCommandeItem ==> commandeItem.getQteRecu().compareTo(receptionItem.getQte()) >= 0 == true");
                res = -1;
            }
        }
        if (res > 0 && !checkMode) {
            edit(commandeItem);
        }
        return res;
    }

//    public int verifyUpdateQteRecuCommandeItem(ReceptionItem receptionItem, boolean isSave) {
//        Reception reception = receptionItem.getReception();
//        List<CommandeItem> commandeItems = reception.getCommande().getCommandeItems();
//        Produit produit = receptionItem.getProduit();
//        CommandeItem commandeItem = (CommandeItem) findCommandeItemByProduit(commandeItems, produit)[1];
//        int res = 0;
//        if (isSave) {
//            return 1;
//        } else {
//            if (commandeItem.getQteRecu().compareTo(receptionItem.getQte()) >= 0) {
//                return 2;
//            } else {
//                return -1;
//            }
//        }
//
//    }
    public void updateQteAvoirCommandeItem(AvoirReceptionItem avoirReceptionItem, boolean isSave) {
        Reception reception = avoirReceptionItem.getAvoirReception().getReception();
        ReceptionItem receptionItem = (ReceptionItem) receptionItemFacade.findReceptionItemByProduit(avoirReceptionItem.getProduit(), reception.getReceptionItems())[1];
        CommandeItem commandeItem = (CommandeItem) findCommandeItemByProduit(reception.getCommande().getCommandeItems(), receptionItem.getProduit())[1];
        if (isSave) {
            commandeItem.setQteAvoir(commandeItem.getQteAvoir().add(avoirReceptionItem.getQte()));
        } else {
            commandeItem.setQteAvoir(commandeItem.getQteAvoir().subtract(avoirReceptionItem.getQte()));
        }
        edit(commandeItem);
    }

    public Object[] findCommandeItemByProduit(List<CommandeItem> commandeItems, Produit produit) {
        int index = 0;
        for (CommandeItem commandeItem : commandeItems) {
            if (produit.equals(commandeItem.getProduit())) {
                return new Object[]{index, commandeItem};
            }
            index++;
        }
        return new Object[]{-1, null};
    }

    public int removeCommandeItemFromCommande(Commande commande, CommandeItem commandeItem) {
        Object[] myCommandeItemObject = findCommandeItemByProduit(commande.getCommandeItems(), commandeItem.getProduit());
        int index = new Integer(myCommandeItemObject[0] + "");
        if (index == -1) {
            return -1;
        }
        commande.getCommandeItems().remove(index);
        commande.setMontantTotal(commande.getMontantTotal().subtract((commandeItem.getQte().multiply(commandeItem.getPrix()))));
        return 1;
    }

    public int addCommandeItemToCommande(Commande commande, CommandeItem commandeItem) {
        List<CommandeItem> commandeItems = commande.getCommandeItems();
        Object[] myCommandeItemObject = findCommandeItemByProduit(commandeItems, commandeItem.getProduit());
        if (myCommandeItemObject[1] != null) {
            return -1;
        }
        commande.setMontantTotal(commande.getMontantTotal().add((commandeItem.getQte().multiply(commandeItem.getPrix()))));
        commande.getCommandeItems().add(commandeItem);
        return 1;

    }

    public CommandeItem findCommandeItemByProduit(Commande commande, Produit produit) {
        return (CommandeItem) em.createQuery("SELECT cmditem FROM CommandeItem cmditem WHERE"
                + " cmditem.produit.id=" + produit.getId()
                + " AND cmditem.commande.id=" + commande.getId()).getSingleResult();
    }

    public List<CommandeItem> findCommadeItemsByIdCmd(Commande commande) {
        System.out.println("SELECT cmdi FROM CommandeItem cmdi WHERE cmdi.commande.id=" + commande.getId());
        return em.createQuery("SELECT cmdi FROM CommandeItem cmdi WHERE cmdi.commande.id=" + commande.getId()).getResultList();
    }

    public List<CommandeItem> findCommadeItemsEnCourByIdCmd(Commande commande) {
        return em.createQuery("SELECT cmdi FROM CommandeItem cmdi WHERE cmdi.qte > cmdi.qteRecu - cmdi.qteAvoir  and cmdi.commande.id=" + commande.getId()).getResultList();
    }

    public CommandeItem cloneCommandeItem(Commande commande, CommandeItem commandeItemToBeCloned, Produit produit, Famille famille) {
        CommandeItem commandeItem = new CommandeItem();
        commandeItem.setCommande(commande);
        commandeItem.setPrix(commandeItemToBeCloned.getPrix());
        commandeItem.setQte(commandeItemToBeCloned.getQte());
        commandeItem.setProduit(produitFacade.cloneProduitWithFamille(produit, famille));
        commandeItem.setQteAvoir(new BigDecimal(0));
        commandeItem.setQteRecu(new BigDecimal(0));
        return commandeItem;
    }

    public BigDecimal calculerMontantTotal(List<CommandeItem> commandeItems) {
        BigDecimal montantTotal = new BigDecimal(0);
        for (CommandeItem item : commandeItems) {
            montantTotal = montantTotal.add(item.getPrix().multiply(item.getQte()));
        }
        return montantTotal;
    }

//    void associate(Commande commande, List<CommandeItem> commandeItems) {
//        for (CommandeItem commandeItem : commandeItems) {
//            commandeItem.setCommande(commande);
//        }
//    }
}
