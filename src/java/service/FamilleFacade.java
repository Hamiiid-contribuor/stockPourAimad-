/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Abonne;
import bean.Famille;
import bean.SuperFamille;
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
public class FamilleFacade extends AbstractFacade<Famille> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;

    private Long generateId() {
        Long maxId = (Long) em.createQuery("SELECT MAX(fm.id) FROM Famille fm").getSingleResult();
        return (maxId == null ? 1l : maxId + 1);
    }

    @Override
    public void create(Famille famille) {
        famille.setId(generateId());
        super.create(famille);
    }

    public List<Famille> findByAbonne(Abonne abonne) {
        if (abonne != null && abonne.getId() != null) {
            String requette = "SELECT fa FROM Famille fa WHERE  fa.abonne.id=" + abonne.getId();
            System.out.println("downloading famille ...");
            System.out.println(requette);
            return em.createQuery(requette).getResultList();
        }
        return new ArrayList();

    }

    public Famille cloneFamille(Famille familleToBeCloned) {
        Famille famille = new Famille();
        famille.setId(familleToBeCloned.getId());
        famille.setLibelle(familleToBeCloned.getLibelle());
        return famille;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FamilleFacade() {
        super(Famille.class);
    }

      public List<Famille> findFamilleBySuperFamille(SuperFamille superFamille) {

        String requette = "SELECT f FROM Famille f WHERE f.superFamille.id = " + superFamille.getId();
        return em.createQuery(requette).getResultList();
    }
}
