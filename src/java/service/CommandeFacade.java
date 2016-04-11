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
import bean.Reception;
import bean.ReceptionItem;
import bean.User;
import controler.util.DateUtil;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class CommandeFacade extends AbstractFacade<Commande> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;

    private @EJB
    CommandeItemFacade commandeItemFacade;
    
    @Override
    public void remove(Commande commande){
        
    }

    //************************************** UPDATE FOR RECEPTION ***************
    public void updateMontantTotalAndEtatReceptionCommande(Reception reception, boolean isSave) {
        updateEtatReceptionCommande(reception, false);
        updateMontantTotalReception(reception, isSave, false);
        edit(reception.getCommande());
    }

    public void updateMontantTotalAvoirAndEtatReceptionCommande(AvoirReception avoirReception, boolean isSave) {
        updateEtatReceptionCommande(avoirReception.getReception(), false);
        updateMontantTotalAvoirReception(avoirReception, isSave, false);
        edit(avoirReception.getReception().getCommande());
    }

    private void updateEtatReceptionCommande(Reception reception, boolean commit) {
        List<CommandeItem> commandeItems = reception.getCommande().getCommandeItems();
        int nbrProduitReceptionneeIntegralement = 0;
        int nbrProduitJamaisReceptionnee = 0;
        int etatReception = 0;
        for (CommandeItem commandeItem : commandeItems) {
            if (commandeItem.getQte().compareTo((commandeItem.getQteRecu().subtract(commandeItem.getQteAvoir()))) == 0) {
                nbrProduitReceptionneeIntegralement++;
            } else if ((commandeItem.getQteRecu().subtract(commandeItem.getQteAvoir())).compareTo(BigDecimal.ZERO) == 0) {
                nbrProduitJamaisReceptionnee++;
            }
        }
        if (nbrProduitReceptionneeIntegralement == commandeItems.size()) {
            etatReception = 2;
        } else if (nbrProduitJamaisReceptionnee == commandeItems.size()) {
            etatReception = 0;
        } else {
            etatReception = 1;
        }
        if (etatReception != reception.getCommande().getEtatReception()) {
            reception.getCommande().setEtatReception(etatReception);//2 true
            if (commit) {
                edit(reception.getCommande());
            }
        }

    }

    private int updateMontantTotalReception(Reception reception, boolean isSave, boolean commit) {
        BigDecimal montantTotalReception = null;
        int res = 0;
        if (isSave) {
            montantTotalReception = reception.getCommande().getMontantTotalReception().add((reception.getMontantTotal()));
            res = 1;
        } else {
            montantTotalReception = reception.getCommande().getMontantTotalReception().subtract((reception.getMontantTotal()));
            if (montantTotalReception.compareTo(BigDecimal.ZERO) >= 0) {
                res = 2;
            } else {
                res = -1;
            }
        }
        if (res > 0) {
            reception.getCommande().setMontantTotalReception(montantTotalReception);
            if (commit) {
                edit(reception.getCommande());
            }
        }
        return res;

    }

    private void updateMontantTotalAvoirReception(AvoirReception avoirReception, boolean isSave, boolean commit) {
        BigDecimal montantTotalAvoir = null;
        if (isSave) {
            montantTotalAvoir = avoirReception.getReception().getCommande().getMontantTotalAvoir().add((avoirReception.getMontant()));
        } else {
            montantTotalAvoir = avoirReception.getReception().getCommande().getMontantTotalAvoir().subtract((avoirReception.getMontant()));
        }
        avoirReception.getReception().getCommande().setMontantTotalAvoir(montantTotalAvoir);
        if (commit) {
            edit(avoirReception.getReception().getCommande());
        }
    }

    //************************************** UPDATE FOR RECEPTION ITEM ***************
    public void updateMontantTotalAndEtatReceptionCommande(ReceptionItem receptionItem, boolean isSave) {
        updateEtatReceptionCommande(receptionItem.getReception(), false);
        updateMontantTotalReception(receptionItem, isSave, false);
        edit(receptionItem.getReception().getCommande());
    }

    public void updateMontantTotalAndEtatReceptionCommande(AvoirReceptionItem avoirReceptionItem, boolean isSave) {
        updateEtatReceptionCommande(avoirReceptionItem.getAvoirReception().getReception(), false);
        updateMontantTotalAvoir(avoirReceptionItem, isSave, false);
        edit(avoirReceptionItem.getAvoirReception().getReception().getCommande());
    }

    private void updateMontantTotalReception(ReceptionItem receptionItem, boolean isSave, boolean commit) {
        Reception reception = receptionItem.getReception();
        CommandeItem commandeItem = (CommandeItem) commandeItemFacade.findCommandeItemByProduit(reception.getCommande().getCommandeItems(), receptionItem.getProduit())[1];
        BigDecimal montantTotalReception = null;
        if (isSave) {
            montantTotalReception = reception.getCommande().getMontantTotalReception().add((commandeItem.getPrix().multiply(receptionItem.getQte())));
        } else {
            montantTotalReception = reception.getCommande().getMontantTotalReception().subtract((commandeItem.getPrix().multiply(receptionItem.getQte())));
        }
        reception.getCommande().setMontantTotalReception(montantTotalReception);
        if (commit) {
            edit(reception.getCommande());
        }
    }

    private void updateMontantTotalAvoir(AvoirReceptionItem avoirReceptionItem, boolean isSave, boolean commit) {
        Reception reception = avoirReceptionItem.getAvoirReception().getReception();
        CommandeItem commandeItem = (CommandeItem) commandeItemFacade.findCommandeItemByProduit(reception.getCommande().getCommandeItems(), avoirReceptionItem.getProduit())[1];
        BigDecimal montantTotalAvoir = null;
        if (isSave) {
            montantTotalAvoir = reception.getCommande().getMontantTotalAvoir().add((commandeItem.getPrix().multiply(avoirReceptionItem.getQte())));
        } else {
            montantTotalAvoir = reception.getCommande().getMontantTotalAvoir().subtract((commandeItem.getPrix().multiply(avoirReceptionItem.getQte())));
        }
        reception.getCommande().setMontantTotalAvoir(montantTotalAvoir);
        if (commit) {
            edit(reception.getCommande());
        }
    }
//***********************************

    public int createCommande(Commande commande, User user) {
        commande.setAbonne(user.getAbonne());
        if (!verifierExistenceReference(commande, 0, false)) {
            commande.setId(generateId());
            create(commande);
            commandeItemFacade.createCommandeItems(commande);
            return 1;
        }
        return -1;
    }

    private Long generateId() {
        Long maxId = (Long) em.createQuery("SELECT MAX(cmd.id) FROM Commande cmd").getSingleResult();
        return (maxId == null ? 1l : maxId + 1);
    }

    public boolean verifierExistenceReference(Commande commande, int deleted, boolean isUpdateAction) {
        Abonne abonne = commande.getAbonne();
        if (abonne != null && abonne.getId() != null) {
            String requtte = "SELECT c FROM Commande c WHERE 1=1";
            requtte += " and c.abonne.id =" + abonne.getId();
            if (commande.getReference() != null && !commande.getReference().equals("")) {
                requtte += " and c.reference ='" + commande.getReference() + "'";
            }
            if (isUpdateAction) {
                requtte += " and c.id <> " + commande.getId();
            }
            List resultat = em.createQuery(requtte).getResultList();
            return (!resultat.isEmpty());
        }
        return true;
    }

    public List<Commande> findByCriteres(Commande commande, Abonne abonne, int deleted, int etatPaiement, int etatReception, Date dateCommandeMin, Date dateCommandeMax, Date dateEcheanceMin, Date dateEcheanceMax) {
        String requtte = "SELECT c FROM Commande c WHERE 1=1 ";
        if (abonne != null && abonne.getId() != null) {
            requtte += " and c.abonne.id =" + abonne.getId();
            if (commande.getReference() != null && !commande.getReference().equals("")) {
                requtte += " and c.reference ='" + commande.getReference() + "'";
            }
            if (commande.getProjet() != null && commande.getProjet().getId() != null) {
                requtte += " and c.projet.id=" + commande.getProjet().getId();
            }
            if (commande.getResponsable() != null && commande.getResponsable().getId() != null) {
                requtte += " and c.responsable.id=" + commande.getResponsable().getId();
            }
            if (commande.getFournisseur() != null && commande.getFournisseur().getId() != null) {
                requtte += " and c.fournisseur.id=" + commande.getFournisseur().getId();
            }
            if (etatPaiement == 0) {//1=pas de paiement
                requtte += " and c.paiement = 0";
            } else if (etatPaiement == 1) {//2=EnCour
                requtte += " and c.paiement < c.montantTotal and c.paiement <> 0";
            } else if (etatPaiement == 2) {//3=payé
                requtte += " and c.paiement = c.montantTotal";
            }
            if (etatReception != -1) {
                requtte += " and c.etatReception = " + etatReception;
            }
            if (dateCommandeMax != null) {
                requtte += " and c.dateCommande <= '" + DateUtil.getSqlDate(dateCommandeMax) + "'";
            }
            if (dateCommandeMin != null) {
                requtte += " and c.dateCommande >= '" + DateUtil.getSqlDate(dateCommandeMin) + "'";
            }
            if (dateEcheanceMax != null) {
                requtte += " and c.dateEchance <= '" + DateUtil.getSqlDate(dateEcheanceMax) + "'";
            }
            if (dateEcheanceMin != null) {
                requtte += " and c.dateEchance >= '" + DateUtil.getSqlDate(dateEcheanceMin) + "'";
            }
            System.out.println("Requette =" + requtte);
            return em.createQuery(requtte).getResultList();
        } else {
            return new ArrayList();
        }

    }

    @Override
    public void create(Commande commande) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("haaa respp ==> " + commande.getResponsable().getId());
        em.createNativeQuery("INSERT INTO `commande` (`ID`, `COMMENTAIRE`, `DATECOMMANDE`, `DATEECHANCE`"
                + ", `MONTANTTOTAL`, `REFERENCE`,`REFERENCESUFFIX`,`REFERENCEPRIFFIX`,`REFERENCEINDEX` ,"
                + " `TVA`, `ABONNE_ID`, `FOURNISSEUR_ID`, `PROJET_ID`, `RESPONSABLE_ID`,"
                + " `ETATRECEPTION`, `PAIEMENT`, `PAIEMENTEFFETENCOUR`"
                + ", `MONTANTTOTALRECEPTION`, `MONTANTTOTALAVOIR`) "
                + "VALUES ('" + generateId() + "', '" + commande.getCommentaire() + "' ,'"
                + simpleDateFormat.format(commande.getDateCommande()) + "',"
                + "  '" + simpleDateFormat.format(commande.getDateEchance()) + "',"
                + " '" + commande.getMontantTotal() + "', "
                + " '" + commande.getReference() + "', '" + commande.getReferenceSuffix() + ""
                + "', '" + commande.getReferencePriffix() + "', '" + commande.getReferenceIndex() + "', "
                + " '" + commande.getTva() + "', "
                + "'" + commande.getAbonne().getId() + "', '" + commande.getFournisseur().getId() + "',"
                + " '" + commande.getProjet().getId() + "', '" + commande.getResponsable().getId() + "'"
                + " ,0,0,0,0,0)").executeUpdate();
    }

    public Long generateReferenceIndexCommande(Commande commande) {
        if (commande.getAbonne() != null && commande.getAbonne().getId() != null) {
            String requette = "SELECT MAX(c.referenceIndex) FROM Commande c WHERE c.abonne.id=" + commande.getAbonne().getId();
            if (commande.getReferencePriffix() != null && !commande.getReferencePriffix().equals("")) {
                requette += " and c.referencePriffix='" + commande.getReferencePriffix() + "'";
            }
            if (commande.getReferenceSuffix() != null && !commande.getReferenceSuffix().equals("")) {
                requette += " and c.referenceSuffix='" + commande.getReferenceSuffix() + "'";
            }
            Long max = (Long) em.createQuery(requette).getSingleResult();
            return max == null ? 1l : max + 1;
        }
        return -1L;
    }

    public List<Commande> findCommandeByAbonne(Abonne abonne) {
        return em.createQuery("SELECT c FROM Commande c WHERE c.abonne.id=" + abonne.getId()).getResultList();
    }

    public int constructReference(Commande commande) {
        String reference = "";
        if (commande.getReferencePriffix() != null && !commande.getReferencePriffix().equals("")) {
            reference = commande.getReferencePriffix() + "-";
        }
        if (commande.getReferenceIndex() != null && !commande.getReferenceIndex().equals(0)) {
            reference += commande.getReferenceIndex();
        } else {
            return -1;
        }
        if (commande.getReferenceSuffix() != null && !commande.getReferenceSuffix().equals("")) {
            reference += ("-" + commande.getReferenceSuffix());
        }
        commande.setReference(reference);
        return 1;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CommandeFacade() {
        super(Commande.class);
    }


}
