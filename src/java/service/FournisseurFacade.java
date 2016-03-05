/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Abonne;
import bean.Fournisseur;
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
public class FournisseurFacade extends AbstractFacade<Fournisseur> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;

    private Long generateId() {
        Long maxId = (Long) em.createQuery("SELECT MAX(fm.id) FROM Famille fm").getSingleResult();
        return (maxId == null ? 1l : maxId + 1);
    }

    @Override
    public void create(Fournisseur fournisseur) {
        fournisseur.setId(generateId());
        fournisseur.setBloquer(false);
        fournisseur.setDetailBloquage("");
        super.create(fournisseur);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FournisseurFacade() {
        super(Fournisseur.class);
    }

    public List<Fournisseur> findByAbonne(Abonne abonne, int deleted) {
        if (abonne != null && abonne.getId() != null) {
            String request = "SELECT fr FROM Fournisseur fr WHERE fr.abonne.id=" + abonne.getId();
//            if (deleted != -1) {
//                request += " AND fr.supprimer=" + deleted;
//            }
            System.out.println("downloading Fournisseur ...");
            System.out.println(request);
            return em.createQuery(request).getResultList();
        }
        return new ArrayList();
    }

}
