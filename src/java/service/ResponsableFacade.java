/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Abonne;
import bean.Responsable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author moulaYounes
 */
@Stateless
public class ResponsableFacade extends AbstractFacade<Responsable> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;

    public List<Responsable> findByAbonne(Abonne abonne, int deleted) {
        String request = "SELECT resp FROM Responsable resp WHERE resp.abonne.id=" + abonne.getId();
        System.out.println("downloading Responsable ...");
        System.out.println("requete=" + request);
//        if (deleted != -1) {
//            request += " AND resp.supprimer=" + deleted;
//        }
        return em.createQuery(request).getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ResponsableFacade() {
        super(Responsable.class);
    }

}
