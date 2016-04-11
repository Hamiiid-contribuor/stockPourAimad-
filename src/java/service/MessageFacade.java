/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Message;
import bean.MessageReception;
import bean.User;
import controler.util.DateUtil;
import controler.util.SessionUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ayoub
 */
@Stateless
public class MessageFacade extends AbstractFacade<Message> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;

    @EJB
    private MessageReceptionFacade messageReceptionFacade;

    @Override

    protected EntityManager getEntityManager() {
        return em;
    }

    public MessageFacade() {
        super(Message.class);
    }

    public List<Message> findMessageEnvoyeByUser(User user) {
        String requette = "Select m from Message m where m.user.login='" + user.getLogin() + "'";
        return em.createQuery(requette).getResultList();

    }

    private Long generateId() {
        Long max = (Long) em.createQuery("SELECT MAX(m.id) FROM Message m").getSingleResult();
        return max == null ? 1l : max + 1;
    }

    public void save(Message message, List<String> users) {
        message.setId(generateId());
        message.setUser(SessionUtil.getConnectedUser());
        message.setDateEnvoi(new Date());
        super.create(message);
        messageReceptionFacade.saveMessageReceptions(message, users);
    }
    
    

   

}
