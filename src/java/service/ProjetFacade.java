/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Abonne;
import bean.Projet;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author moulaYounes
 */
@Stateless
public class ProjetFacade extends AbstractFacade<Projet> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProjetFacade() {
        super(Projet.class);
    }

    public List<Projet> findByAbonne(Abonne abonne, int deleted) {
        if (abonne != null && abonne.getId() != null) {
            String request = "SELECT pr FROM Projet pr WHERE pr.abonne.id=" + abonne.getId();
            System.out.println("downloading Projet ...");
         
            System.out.println(request);
            return em.createQuery(request).getResultList();
        }
        return new ArrayList();

    }

}
