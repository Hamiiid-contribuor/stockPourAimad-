/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Abonne;
import bean.User;
import java.util.Objects;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author moulaYounes
 */
@Stateless
public class AbonneFacade extends AbstractFacade<Abonne> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;

    private @EJB
    FamilleFacade familleFacade;
    private @EJB
    FournisseurFacade fournisseurFacade;
    private @EJB
    ResponsableFacade responsableFacade;
    private @EJB
    ProjetFacade projetFacade;
    private @EJB
    ClientFacade clientFacade;
    private @EJB
    MagasinFacade magasinFacade;

    public int initAbonneParams(Abonne myAbonne) {
        if (myAbonne == null || myAbonne.getId() == null) {
            return -1;
        }
        myAbonne.setFamilles(familleFacade.findByAbonne(myAbonne));
        myAbonne.setFournisseurs(fournisseurFacade.findByAbonne(myAbonne, 0));
        myAbonne.setResponsables(responsableFacade.findByAbonne(myAbonne, 0));
        myAbonne.setProjets(projetFacade.findByAbonne(myAbonne, 0));
        myAbonne.setClients(clientFacade.findByAbonne(myAbonne, 0));
        myAbonne.setMagasins(magasinFacade.findByAbonne(myAbonne, 0));
        return 1;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AbonneFacade() {
        super(Abonne.class);
    }

    public Abonne findByUser(User user) {
        if(user!=null && user.getLogin()!=null){
        String requette="SELECT ab FROM Abonne ab, User us WHERE us.abonne.id= ab.id and us.login='"+user.getLogin()+"'";
            System.out.println(" Abonne findByUser ==> "+requette);
        return (Abonne) em.createQuery(requette).getSingleResult();
        }
        return new Abonne();
    }

     public long generateId() {
        if (em.createQuery("SELECT MAX(a.id) FROM Abonne AS a ").getResultList().get(0) == null) {
            return 1;
        }
        return (long) em.createQuery("SELECT MAX(a.id) FROM Abonne AS a ").getResultList().get(0);
    }
}
