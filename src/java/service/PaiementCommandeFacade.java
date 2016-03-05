/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Commande;
import bean.PaiementCommande;
import java.math.BigDecimal;
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
public class PaiementCommandeFacade extends AbstractFacade<PaiementCommande> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;
    @EJB
    private CommandeFacade commandeFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PaiementCommandeFacade() {
        super(PaiementCommande.class);
    }

    @Override
    public void remove(PaiementCommande paiementCommande) {
        if (paiementCommande.getCommande() != null && paiementCommande.getCommande().getId() != null) {
            if (paiementCommande.getMode() == 1 || (paiementCommande.getMode() != 1 && paiementCommande.getEncaisser())) { // ESPECE OU  EFET ENCAISSE
                paiementCommande.getCommande().setPaiement(paiementCommande.getCommande().getPaiement().subtract(paiementCommande.getMontant()));

            } else if (paiementCommande.getMode() != 1 && !paiementCommande.getEncaisser()) { // EFFET NON ENCAISSE
                paiementCommande.getCommande().setPaiementEffetEnCour(paiementCommande.getCommande().getPaiementEffetEnCour().subtract(paiementCommande.getMontant()));
            }
            paiementCommande.getCommande().getPaiementCommandes().remove(paiementCommande.getCommande().getPaiementCommandes().indexOf(paiementCommande));
            commandeFacade.edit(paiementCommande.getCommande());
            super.remove(paiementCommande);
        }
    }

    public int save(PaiementCommande paiementCommande) { // save(PaiementEmplyee(Paiement p)
        Commande commande = (paiementCommande.getCommande());
        BigDecimal paiementTotalEffetNonPayeInclus = commande.getPaiement().add(commande.getPaiementEffetEnCour()).add(paiementCommande.getMontant());
        if (commande.getMontantTotal().compareTo(paiementTotalEffetNonPayeInclus) >= 0) {
            if (paiementCommande.getMode() != 1) {
                commande.setPaiementEffetEnCour(commande.getPaiementEffetEnCour().add(paiementCommande.getMontant()));
                paiementCommande.setEncaisser(false);
            } else {
                commande.setPaiement(commande.getPaiement().add(paiementCommande.getMontant()));
                paiementCommande.setEncaisser(true);
            }
            commandeFacade.edit(commande);
            create(paiementCommande);
            return 1;
        }
        return -1;
    }

    public List<PaiementCommande> findAllPaiementNoEncaisserByCommande(Commande commande) {
        return em.createQuery("SELECT p FROM PaiementCommande p where p.commande.id=" + commande.getId() + "").getResultList();

    }

    public void changerEtatEncaissement(PaiementCommande paiementCommande) {
        //   paiementCommande.setCommande(commandeFacade.find(paiementCommande.getCommande().getId()));
        if (paiementCommande.getEncaisser() == false) {
            decaisser(paiementCommande);
        } else {
            encaisser(paiementCommande);
        }
        commandeFacade.edit(paiementCommande.getCommande());
        edit(paiementCommande);
    }

    private void encaisser(PaiementCommande paiementCommande) {
        paiementCommande.setEncaisser(true);
        paiementCommande.getCommande().setPaiement(paiementCommande.getCommande().getPaiement().add(paiementCommande.getMontant()));
        paiementCommande.getCommande().setPaiementEffetEnCour(paiementCommande.getCommande().getPaiementEffetEnCour().subtract(paiementCommande.getMontant()));
    }

    private void decaisser(PaiementCommande paiementCommande) {
        paiementCommande.setEncaisser(false);
        paiementCommande.getCommande().setPaiement(paiementCommande.getCommande().getPaiement().subtract(paiementCommande.getMontant()));
        paiementCommande.getCommande().setPaiementEffetEnCour(paiementCommande.getCommande().getPaiementEffetEnCour().add(paiementCommande.getMontant()));
    }

    public List<PaiementCommande> findPaiementByCommande(Commande commande) {
        return em.createQuery("SELECT p FROM PaiementCommande p WHERE p.commande.id=" + commande.getId() + "").getResultList();
    }
}
