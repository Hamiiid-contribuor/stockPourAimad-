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
public class MessageReceptionFacade extends AbstractFacade<MessageReception> {
    
    @EJB
    private MessageFacade messageFacade;

    

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<MessageReception> findMessageReceptionByUser(User user) {
        String requette = "Select mr from MessageReception mr where mr.distinataire.login='" + user.getLogin() + "'";
        return em.createQuery(requette).getResultList();

    }

    public void saveMessageReceptions(Message message, List<String> users) {
        for (String user : users) {
            MessageReception messageReception = new MessageReception();
            messageReception.setMessage(message);
            messageReception.getDistinataire().setLogin(user);
            create(messageReception);
        }

    }
    
    public List<MessageReception> findByCreteria(User envoyePour,User envoyePar, Date dateMin , Date dateMax){
        //String requette = "Select distinct(m) From Message m , MessageReception mr WHERE mr.message.id=m.id";
      String requette=" Select distinct(mr) from MessageReception mr ,Message m where (mr.distinataire.login='" + SessionUtil.getConnectedUser().getLogin() + "' or mr.message.user.login='"+SessionUtil.getConnectedUser().getLogin()+"') and mr.message.id=m.id";
        if(envoyePar != null && envoyePar.getLogin() != null){
            requette +=" AND mr.message.user.login ='"+envoyePar.getLogin()+"'";
        }
        if(envoyePour != null && envoyePour.getLogin() != null){
            requette+=" AND mr.distinataire.login='"+envoyePour.getLogin()+"'";
        }
        if (dateMax != null) {
                requette += " AND mr.message.dateEnvoi <= '" + DateUtil.getSqlDate(dateMax) + "'";
            }
            if (dateMin != null) {
                requette += " AND mr.message.dateEnvoi >= '" + DateUtil.getSqlDate(dateMin) + "'";
            }
            
//         if(envoyePar == null && envoyePour == null && dateMax == null && dateMin == null  ) {
//                return findMessageForMe(SessionUtil.getConnectedUser());
//            }
            System.out.println(requette);
         return em.createQuery(requette).getResultList();
    }
    
     

    public MessageReceptionFacade() {
        super(MessageReception.class);
    }

    public void updateMessageInfo(MessageReception selected) {
        System.out.println(selected);
       String requette="Select mr from MessageReception mr where mr.id='"+selected.getId()+"' and (mr.distinataire.login='"+SessionUtil.getConnectedUser().getLogin()+"' or mr.message.user.login='"+SessionUtil.getConnectedUser().getLogin()+"')";
        System.out.println(requette);
       MessageReception  messageReception = (MessageReception) em.createQuery(requette).getSingleResult();
       
      System.out.println(messageReception);
       if(messageReception.getDistinataire().getLogin().equals(SessionUtil.getConnectedUser().getLogin())){
           
       
       if(messageReception.getDateLecture() == null && messageReception.isLu()== false){
           messageReception.setDateLecture(new Date());
           messageReception.setLu(true);
           Date date = messageReception.getDateLecture();
           
           int year = date.getYear() + 1900;
           
           date.setMonth(date.getMonth()+1);
           messageReception.setDetail("Lu à : " + year+"/"+date.getMonth()+"/"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds());
       }
       edit(messageReception);
       }
       
       if(messageReception.getMessage().getUser().getLogin().equals(SessionUtil.getConnectedUser().getLogin())){
           if(messageReception.getMessage().getDateLecture() == null && messageReception.getMessage().isLu() == false){
               
           
           messageReception.getMessage().setLu(true);
           messageReception.getMessage().setDateLecture(new Date());
           messageReception.getMessage().setDetail("Lu à : " + messageReception.getMessage().getDateLecture());
           messageFacade.edit(messageReception.getMessage());
           }
       }
       
        
           
        
    }
    
    public List<MessageReception> findMessageForMe() {
   
       String requette=" Select distinct( mr) from MessageReception mr  where mr.distinataire.login='" + SessionUtil.getConnectedUser().getLogin() + "'";
      return em.createQuery(requette).getResultList();
    
    }

}
