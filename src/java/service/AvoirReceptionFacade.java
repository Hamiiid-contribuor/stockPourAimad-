/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Abonne;
import bean.AvoirReception;
import bean.Commande;
import bean.Reception;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author moulaYounes
 */
@Stateless
public class AvoirReceptionFacade extends AbstractFacade<AvoirReception> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;
    @EJB
    private CommandeItemFacade commandeItemFacade;
    @EJB
    private CommandeFacade commandeFacade;
    @EJB
    private ReceptionItemFacade receptionItemFacade;
    @EJB
    private AvoirReceptionItemFacade avoirReceptionItemFacade;
    @EJB
    private StockFacade stockFacade;
    @EJB
    private ProduitFacade produitFacade;

    public void create(AvoirReception avoirReception, Commande commande, Reception reception, Abonne abonne) {
        prepareCreate(avoirReception, commande, reception, abonne);
        save(avoirReception);
    }

    private void prepareCreate(AvoirReception avoirReception, Commande commande, Reception reception, Abonne abonne) {
        avoirReception.setId(generateId());
        avoirReception.setReception(reception);
        avoirReception.getReception().setCommande(commande);
        avoirReception.getReception().getCommande().setAbonne(abonne);
        avoirReception.getReception().setReceptionItems(receptionItemFacade.findReceptionItemsByReception(avoirReception.getReception()));
        avoirReception.getReception().getCommande().setCommandeItems(commandeItemFacade.findCommadeItemsByIdCmd(avoirReception.getReception().getCommande()));
       // receptionItemFacade.associate(avoirReception.getReception(), avoirReception.getReception().getReceptionItems());
       // commandeItemFacade.associate(avoirReception.getReception().getCommande(), avoirReception.getReception().getCommande().getCommandeItems());
        avoirReceptionItemFacade.associate(avoirReception, avoirReception.getAvoirReceptionItems());
    }

    private void save(AvoirReception avoirReception) {
        super.create(avoirReception);
        avoirReceptionItemFacade.saveOrDeleteAvoirReceptionItems(avoirReception, true); // avoirReceptionItem lase9 f avoirReception
        commandeItemFacade.updateQteAvoirCommandeItems(avoirReception, true);//avoirReception lase9 f List<AvoirReceptionItem>  o AvoirReceptionItem lase9 f AvoirReception li lase9 f Reception li lase9 f List<ReceptionItem>
        receptionItemFacade.updateQteAvoirReceptionItems(avoirReception, true);
        stockFacade.updateStock(avoirReception, true,false); //reception.getCommande().getAbonne()
        produitFacade.updateQteGlobalProduit(avoirReception, true,false);
        commandeFacade.updateMontantTotalAvoirAndEtatReceptionCommande(avoirReception, true); //reception.getCommande().getCommandeItems()
    }

    @Override
    public void remove(AvoirReception avoirReception) {
        commandeItemFacade.updateQteAvoirCommandeItems(avoirReception, false);
        receptionItemFacade.updateQteAvoirReceptionItems(avoirReception, true);
        stockFacade.updateStock(avoirReception, false,false);
        produitFacade.updateQteGlobalProduit(avoirReception, false,false);
        commandeFacade.updateMontantTotalAvoirAndEtatReceptionCommande(avoirReception, false);
        avoirReceptionItemFacade.saveOrDeleteAvoirReceptionItems(avoirReception, false);
        removeUsingNativeQuery(avoirReception);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AvoirReceptionFacade() {
        super(AvoirReception.class);
    }

    private Long generateId() {
        Long maxId = (Long) em.createQuery("SELECT MAX(avrec.id) FROM AvoirReception avrec").getSingleResult();
        return (maxId == null ? 1l : maxId + 1);
    }

    public void removeUsingNativeQuery(AvoirReception avoirReception) {
        em.createNativeQuery("DELETE FROM avoirreception WHERE id=" + avoirReception.getId()).executeUpdate();
    }

}
