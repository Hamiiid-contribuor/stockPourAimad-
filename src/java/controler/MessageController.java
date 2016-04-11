package controler;

import bean.Message;
import bean.MessageReception;
import bean.User;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import service.MessageFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import service.MessageReceptionFacade;

@Named("messageController")
@SessionScoped
public class MessageController implements Serializable {

    @EJB
    private service.MessageFacade ejbFacade;
    @EJB
    private MessageReceptionFacade messageReceptionFacade;

    private List<Message> items;
    private List<MessageReception> messageReceptions;
    private MessageReception seleMessageReception;
    private Message selected;
    private List<User> users;
    private List<String> selectedDistinataire;
    private User user;
    private Message critereObj;
    private Date dateEnvoiMin;
    private Date dateEnvoiMax ;
    private User envoyePour;
    private User envoyePar;

    public MessageController() {
        this.dateEnvoiMin = new Date();
        dateEnvoiMin.setHours(0);
        dateEnvoiMin.setMinutes(0);
        dateEnvoiMin.setSeconds(0);
    }

    public List<MessageReception> getMessageReceptions() {
        if(messageReceptions == null ){
            messageReceptions = messageReceptionFacade.findMessageForMe();
        }
        return messageReceptions;
    }

    public void setMessageReceptions(List<MessageReception> messageReceptions) {
        this.messageReceptions = messageReceptions;
    }

    public MessageReception getSeleMessageReception() {
        if(seleMessageReception == null){
            seleMessageReception = new MessageReception();
        }
        return seleMessageReception;
    }

    public void setSeleMessageReception(MessageReception seleMessageReception) {
        this.seleMessageReception = seleMessageReception;
    }
    
    
    
    
    

    public User getEnvoyePour() {
        if(envoyePour == null){
            envoyePour = new User();
        }
        
        return envoyePour;
    }

    public void setEnvoyePour(User envoyePour) {
        this.envoyePour = envoyePour;
    }
    public User getEnvoyePar() {
        if(envoyePar == null){
            envoyePar = new User();
        }
        
        return envoyePar;
    }

    public void setEnvoyePar(User envoyePar) {
        this.envoyePar = envoyePar;
    }
    
    

    public Date getDateEnvoiMin() {
        return dateEnvoiMin;
    }

    public void setDateEnvoiMin(Date dateEnvoiMin) {
        this.dateEnvoiMin = dateEnvoiMin;
    }

    public Date getDateEnvoiMax() {
        return dateEnvoiMax;
    }

    public void setDateEnvoiMax(Date dateEnvoiMax) {
        this.dateEnvoiMax = dateEnvoiMax;
    }
    
    

    public Message getCritereObj() {
        if(critereObj == null){
            critereObj = new Message();
        }
        return critereObj;
    }

    public void setCritereObj(Message critereObj) {
        this.critereObj = critereObj;
    }
    
    

    public List<String> getSelectedDistinataire() {
        if(selectedDistinataire == null){
            selectedDistinataire = new ArrayList<>();
        }
        return selectedDistinataire;
    }

    public void setSelectedDistinataire(List<String> selectedDistinataire) {
        this.selectedDistinataire = selectedDistinataire;
    }
    
    

    public User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

 

    public List<Message> findMessageEnvoye() {
        return ejbFacade.findMessageEnvoyeByUser(SessionUtil.getConnectedUser());
    }
    
    
    public void updateMessageinfo(){
        messageReceptionFacade.updateMessageInfo(seleMessageReception);
    }

    public void createMessage() {
        System.out.println("hani hna");
        Message clonedMessage = cloneMessage();
        System.out.println("3ayat 3la clone");
        // items.add(clonedMessage);
        System.out.println("clone ajouter l lista");
        ejbFacade.edit(clonedMessage);
        System.out.println("ajouter l base donne");
    }

    public Message cloneMessage() {

        Message message = new Message();
        message.setCorps(selected.getCorps());
        message.setDateEnvoi(selected.getDateEnvoi());
        message.setUser(SessionUtil.getConnectedUser());
        message.setObjet(selected.getObjet());
        message.setLu(false);
        System.out.println("salat methode dial clone");
        return message;

    }

    public List<User> getUsers() {
        if (users == null) {
            users = new ArrayList<>();
        }
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Message getSelected() {
        if (selected == null) {
            selected = new Message();
        }
        return selected;
    }

    public void setSelected(Message selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MessageFacade getFacade() {
        if (ejbFacade == null) {
            ejbFacade = new MessageFacade();

        }
        return ejbFacade;
    }

    public Message prepareCreate() {
        selected = new Message();
        initializeEmbeddableKey();
        return selected;
    }

    public List<User> findUserByAbonne() {

        users = SessionUtil.getConnectedUser().getAbonne().getUsers();
        return users;

    }
    
    public void findByCreteria(){
        messageReceptions = messageReceptionFacade.findByCreteria(envoyePour,envoyePar, dateEnvoiMin, dateEnvoiMax);
    }

    public void create() {
        System.out.println("hahowa 3ayat 3la methode");
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MessageCreated"));
        System.out.println("chi tkharbi9a");
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        System.out.println("salat methode");
        JsfUtil.addSuccessMessage("Message Envoye avec succes");
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MessageUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MessageDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Message> getItems() {
        if (items == null) {
            //items = messageReceptionFacade.findByCreteria(selected, envoyePour, envoyePar, dateEnvoiMin, dateEnvoiMax);
             //items = ejbFacade.findMessageForMe(user) ;      //ejbFacade.findMessageForMe(SessionUtil.getConnectedUser());
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
                    System.out.println(selectedDistinataire);
                    ejbFacade.save(selected, selectedDistinataire);
                    

                } else if (persistAction == PersistAction.UPDATE) {
                    System.out.println("hnaya ");
                    getFacade().edit(selected);
                    System.out.println("sala");
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }
    
    public void findMyBoite(){
        messageReceptions = messageReceptionFacade.findMessageForMe();
    }

    public Message getMessage(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Message> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Message> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Message.class)
    public static class MessageControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MessageController controller = (MessageController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "messageController");
            return controller.getMessage(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Message) {
                Message o = (Message) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Message.class.getName()});
                return null;
            }
        }

    }

}
