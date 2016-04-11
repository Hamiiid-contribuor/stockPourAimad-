/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Abonne;
import bean.DevisCommande;
import bean.DevisCommandeItem;
import bean.Famille;
import bean.Fournisseur;
import bean.Produit;
import bean.Projet;
import bean.Responsable;
import bean.User;
import controler.util.DateUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.sf.jasperreports.engine.JRException;
import util.DevisCommandePdf;

/**
 *
 * @author moulaYounes
 */
@Stateless
public class DevisCommandeFacade extends AbstractFacade<DevisCommande> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;

    @EJB
    private DevisCommandeItemFacade devisCommandeItemFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DevisCommandeFacade() {
        super(DevisCommande.class);
    }

    public void deleteDeviCommande(DevisCommande devisCommande) {
        remove(devisCommande); //supprimer le devis tout simplement
        System.out.println("mon devis est bien Supprimmer !!");
    }

    public BigDecimal calculerMontantTotalDevisCommande(List<DevisCommandeItem> devisCommandeItems) {
        BigDecimal montantTotal = new BigDecimal(0);
        for (int i = 0; i < devisCommandeItems.size(); i++) {
            DevisCommandeItem item = devisCommandeItems.get(i);
            montantTotal = montantTotal.add(item.getPrix().multiply(item.getQte()));
            // la sommes des prix* qte desCommandeItem
        }
        return montantTotal;
    }

    public void createDevisCommande(DevisCommande devisCommande, User user) {
        //pour l'id c'est auto_Incriment // ghalat achef il est generer  
        devisCommande.setId(generateId());
        devisCommande.setAbonne(user.getAbonne());
        //il faut attacher la liste des devisCommandeItem au DevisCommande 
//        devisCommande.setMontantTotal(calculerMontantTotalDevisCommande(devisCommande.getDevisCommandeItems()));
        create(devisCommande);
        devisCommandeItemFacade.createDevisCommandeItemsOnGlobal(devisCommande);

    }

    private Long generateId() {
        Long maxId = (Long) em.createQuery("SELECT MAX(dcmd.id) FROM DevisCommande dcmd").getSingleResult();
        return (maxId == null ? 1l : maxId + 1);
    }

    @Override
    public void create(DevisCommande devisCommande) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String requette = "INSERT INTO `devisCommande` (`ID`, `COMMENTAIRE`, `DATEDEVISCOMMANDE`, `MONTANTTOTAL`"
                + ", `TVA`, `ABONNE_ID`,`FOURNISSEUR_ID`,`PROJET_ID`,`RESPONSABLE_ID` )"
                + "VALUES ('" + generateId() + "', '" + devisCommande.getCommentaire() + "' ,'" + simpleDateFormat.format(devisCommande.getDateDevisCommande()) + "',"
                + " '" + devisCommande.getMontantTotal() + "','" + devisCommande.getTva() + "','" + devisCommande.getAbonne().getId() + "',"
                + "' " + devisCommande.getFournisseur().getId() + "' ,'" + devisCommande.getProjet().getId() + "',"
                + "' " + devisCommande.getResponsable().getId() + "')";

        em.createNativeQuery(requette).executeUpdate();
    }

    public void updateMontantTotalDevisCommade(DevisCommande devisCommande, BigDecimal montantTotal) {
        devisCommande.setMontantTotal(montantTotal);
        edit(devisCommande);

    }

    public List<DevisCommande> findDevisCommandeByAbonne(Abonne abonne) {
        return em.createQuery("SELECT dc FROM DevisCommande dc WHERE dc.abonne.id=" + abonne.getId()).getResultList();
    }

    public List<DevisCommande> findByCriteres(DevisCommande devisCommande, Abonne abonne, Date dateDevisCommandeMin, Date dateDevisCommandeMax) {
        System.out.println("ha howa dkhel elService ");
        String requtte = "SELECT dc FROM DevisCommande dc WHERE 1=1 ";

//        if (devisCommande.getAbonne() != null && devisCommande.getAbonne().getId() != null) {
//            requtte += " and dc.abonne.id =" + devisCommande.getAbonne().getId();
        if (devisCommande.getProjet() != null && devisCommande.getProjet().getId() != null) {
            requtte += " and dc.projet.id=" + devisCommande.getProjet().getId();
        }
        if (devisCommande.getResponsable() != null && devisCommande.getResponsable().getId() != null) {
            requtte += " and dc.responsable.id=" + devisCommande.getResponsable().getId();
        }
        if (devisCommande.getFournisseur() != null && devisCommande.getFournisseur().getId() != null) {
            requtte += " and dc.fournisseur.id=" + devisCommande.getFournisseur().getId();
        }

        if (dateDevisCommandeMax != null) {
            requtte += " and dc.dateDevisCommande <= '" + DateUtil.getSqlDate(dateDevisCommandeMax) + "'";
        }
        if (dateDevisCommandeMin != null) {
            requtte += " and dc.dateDevisCommande >= '" + DateUtil.getSqlDate(dateDevisCommandeMin) + "'";
        }

        System.out.println("Requette =" + requtte);
        return em.createQuery(requtte).getResultList();
//        } else {
//            System.out.println("hadchi alem3em taydkhel el else direk");
//            return new ArrayList();
//        }

    }

    //******************************generate Pdf *************************************
    public void generatePdf(DevisCommande devisCommande) throws JRException, IOException {

        String requette = "SELECT r FROM Responsable r WHERE r.id =" + devisCommande.getResponsable().getId();
        devisCommande.setResponsable((Responsable) em.createQuery(requette).getSingleResult());

        String requette1 = "SELECT p FROM Projet p  WHERE p.id =" + devisCommande.getProjet().getId();
        devisCommande.setProjet((Projet) em.createQuery(requette1).getSingleResult());

        String requette2 = "SELECT f FROM Fournisseur f  WHERE f.id =" + devisCommande.getFournisseur().getId();
        devisCommande.setFournisseur((Fournisseur) em.createQuery(requette2).getSingleResult());

        String requette3 = "SELECT dci FROM DevisCommandeItem dci WHERE dci.devisCommande.id = " + devisCommande.getId();
        List<DevisCommandeItem> devisCommandeItems = em.createQuery(requette3).getResultList();

        for (int i = 0; i < devisCommandeItems.size(); i++) {
            DevisCommandeItem item = devisCommandeItems.get(i);
            String requette4 = "SELECT p FROM Produit p WHERE p.id = " + item.getProduit().getId();
            Produit produit = (Produit) em.createQuery(requette4).getSingleResult();

            String requette5 = "SELECT f FROM Famille f WHERE f.id = " + produit.getFamille().getId();
            produit.setFamille((Famille) em.createQuery(requette5).getSingleResult());
            
            item.setProduit(produit);
        }
        devisCommande.setDevisCommandeItems(devisCommandeItems);
        DevisCommandePdf.generateTransactionPdf(devisCommande);
    }

}
