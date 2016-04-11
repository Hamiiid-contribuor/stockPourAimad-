/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Abonne;
import bean.DevisCommande;
import bean.DevisDemmande;
import bean.DevisDemmandeItem;
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
public class DevisDemmandeFacade extends AbstractFacade<DevisDemmande> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DevisDemmandeFacade() {
        super(DevisDemmande.class);
    }

    @EJB
    private DevisDemmandeItemFacade devisDemmandeItemFacade = new DevisDemmandeItemFacade();

    //*****************************hamid********************************************************
    public void createDevisDemmande(DevisDemmande devisDemmande, User user) {
        devisDemmande.setId(generateId());
        devisDemmande.setAbonne(user.getAbonne());
        //il faut attacher la liste des devisCommandeItem au DevisCommande 
//        devisCommande.setMontantTotal(calculerMontantTotalDevisCommande(devisCommande.getDevisCommandeItems()));
        create(devisDemmande);
        devisDemmandeItemFacade.createDevisDemmandeItemsOnGlobal(devisDemmande);

    }

    private Long generateId() {
        Long maxId = (Long) em.createQuery("SELECT MAX(ddmd.id) FROM DevisDemmande ddmd").getSingleResult();
        return (maxId == null ? 1l : maxId + 1);
    }

    @Override
    public void create(DevisDemmande devisDemmande) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String requette = "INSERT INTO `devisDemmande` (`ID`, `COMMENTAIRE`, `DATEDEVISDEMMANDE`, `MONTANTTOTAL`"
                + ", `TVA`, `ABONNE_ID`,`CLIENT_ID`,`PROJET_ID`,`RESPONSABLE_ID` )"
                + "VALUES ('" + generateId() + "', '" + devisDemmande.getCommentaire() + "' ,'" + simpleDateFormat.format(devisDemmande.getDateDevisDemmande()) + "',"
                + " '" + devisDemmande.getMontantTotal() + "','" + devisDemmande.getTva() + "','" + devisDemmande.getAbonne().getId() + "',"
                + "' " + devisDemmande.getClient().getId() + "' ,'" + devisDemmande.getProjet().getId() + "',"
                + "' " + devisDemmande.getResponsable().getId() + "')";

        em.createNativeQuery(requette).executeUpdate();
    }

    public BigDecimal calculerMontantTotalDevisDemmande(List<DevisDemmandeItem> devisDemmandeItems) {
        BigDecimal montantTotal = new BigDecimal(0);
        for (int i = 0; i < devisDemmandeItems.size(); i++) {
            DevisDemmandeItem item = devisDemmandeItems.get(i);
            montantTotal = montantTotal.add(item.getPrix().multiply(item.getQte()));
            // la sommes des prix* qte desCommandeItem
        }
        return montantTotal;
    }

    public List<DevisDemmande> findByCriteres(DevisDemmande devisDemmande, Abonne abonne, Date dateDevisDemmandeMin, Date dateDevisDemmandeMax) {
        System.out.println("otaydkhel hta leService");
        String requtte = "SELECT dd FROM DevisDemmande dd WHERE 1=1 ";
//
//        if (devisDemmande.getAbonne() != null && devisDemmande.getAbonne().getId() != null) {
//            requtte += " and dc.abonne.id =" + devisDemmande.getAbonne().getId();

            if (devisDemmande.getProjet() != null && devisDemmande.getProjet().getId() != null) {
                requtte += " and dd.projet.id=" + devisDemmande.getProjet().getId();
            }
            if (devisDemmande.getResponsable() != null && devisDemmande.getResponsable().getId() != null) {
                requtte += " and dd.responsable.id=" + devisDemmande.getResponsable().getId();
            }
            if (devisDemmande.getClient() != null && devisDemmande.getClient().getId() != null) {
                requtte += " and dd.client.id=" + devisDemmande.getClient().getId();
            }
            if (dateDevisDemmandeMax != null) {
                requtte += " and dd.dateDevisDemmande <= '" + DateUtil.getSqlDate(dateDevisDemmandeMax) + "'";
            }
            if (dateDevisDemmandeMin != null) {
                requtte += " and dd.dateDevisDemmande >= '" + DateUtil.getSqlDate(dateDevisDemmandeMin) + "'";
            }

            System.out.println("Requette =" + requtte);
            List<DevisDemmande> hhhh =  em.createQuery(requtte).getResultList();
            System.out.println("ha lista --->"+hhhh);
            return hhhh;
//        } else {
//            System.out.println("tahada taydkhel el else direk !!!!!!");
//            return new ArrayList();
//        }

    }
    
    public void deleteDevisDemmande (DevisDemmande devisDemmande){
        remove(devisDemmande);
    }

}
