/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Abonne;
import bean.SuperFamille;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hamid
 */
@Stateless
public class SuperFamilleFacade extends AbstractFacade<SuperFamille> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SuperFamilleFacade() {
        super(SuperFamille.class);
    }
    
    
    public List<SuperFamille> findSuperFamilleByAbonne(Abonne abonne){
        
        String requette = "SELECT sp FROM SuperFamille sp WHERE sp.abonne.id = "+abonne.getId();
        return em.createQuery(requette).getResultList();
    }
    
}
