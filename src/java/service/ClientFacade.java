/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Abonne;
import bean.Client;
import bean.Famille;
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
public class ClientFacade extends AbstractFacade<Client> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    private Long generateId() {
        Long maxId = (Long) em.createQuery("SELECT MAX(cl.id) FROM Client cl").getSingleResult();
        return (maxId == null ? 1l : maxId + 1);
    }

    @Override
    public void create(Client client) {
        client.setId(generateId());
        client.setBloquer(false);
        client.setDetailBloquage("");
        super.create(client);
    }

    public List<Client> findByAbonne(Abonne abonne, int deleted) {
        if (abonne != null && abonne.getId() != null) {
            String requette = "SELECT cl FROM Client cl WHERE cl.abonne.id=" + abonne.getId();
            
            System.out.println("downloading Client...");
            System.out.println(requette);
            return em.createQuery(requette).getResultList();
        }
        return new ArrayList<>();

    }

    public ClientFacade() {
        super(Client.class);
    }

}
