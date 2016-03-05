/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Abonne;
import bean.Magasin;
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
public class MagasinFacade extends AbstractFacade<Magasin> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;

    private Long generateId() {
        Long maxId = (Long) em.createQuery("SELECT MAX(mg.id) FROM Magasin mg").getSingleResult();
        return (maxId == null ? 1l : maxId + 1);
    }

    @Override
    public void create(Magasin magasin) {
        magasin.setId(generateId());
        super.create(magasin);
    }

    public List<Magasin> findByAbonne(Abonne abonne, int deleted) {
        if (abonne != null && abonne.getId() != null) {
            String request = "SELECT mg FROM Magasin mg WHERE mg.abonne.id=" + abonne.getId();
//            if (deleted != -1) {
//                request += " and mg.supprimer=" + deleted;
//            }
            System.out.println("Downloading Magasin...");
            System.out.println(request);
            return em.createQuery(request).getResultList();
        }
        return new ArrayList();

    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MagasinFacade() {
        super(Magasin.class);
    }

}
