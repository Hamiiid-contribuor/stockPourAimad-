package controler;

import bean.Abonne;
import bean.User;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.Message;
import controler.util.MessageManager;
import controler.util.SessionUtil;
import service.UserFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import service.AbonneFacade;

@Named("userController")
@SessionScoped
public class UserController implements Serializable {

    @EJB
    private service.UserFacade ejbFacade;
    private List<User> items = null;
    private User selected;
    private Message message;
    private @EJB
    AbonneFacade abonneFacade;
    private static List<Abonne> abonnes;

   

    public static List<Abonne> getAbonnes() {
        if (abonnes == null) {
            abonnes = new ArrayList<>();
        }
        return abonnes;
    }

    private int findAbonnePosition(Abonne myAbonne) {
        int i = 0;
        for (Abonne abonne : getAbonnes()) {
            if (Objects.equals(myAbonne.getId(), abonne.getId())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private int findUserPosition(Abonne myAbonne, User myUser) {
        int i = 0;
        for (User user : myAbonne.getUsers()) {
            if (myUser.getLogin().equals(user.getLogin())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void attachUserToAbonneAndToSession(User user, Abonne myAbonne) {
        myAbonne.getUsers().add(user);
        getAbonnes().add(myAbonne);
        user.setAbonne(myAbonne);
        SessionUtil.setAttribute("user", user);
    }

    public int initUserSession(User user, Abonne myAbonne) {
        if (myAbonne == null || myAbonne.getId() == null) {
            return -1;
        } else {
//            myAbonne = abonneFacade.findByUser(user);
            int abonnePosition = findAbonnePosition(myAbonne);
            if (abonnePosition != -1) {
                int userPosition = findUserPosition(myAbonne, user);
                if (userPosition == -1) {
                    attachUserToAbonneAndToSession(user, myAbonne);
                    return 1;
                }
                return 2; // user se connecte sans se deconnecter au paravant
            } else {
                abonneFacade.initAbonneParams(myAbonne);
                attachUserToAbonneAndToSession(user, myAbonne);
                return 3;
            }
        }
    }
    
    
    public UserController() {
    }

    private void validteConnexionForm(int res) {
        message = MessageManager.createErrorMessage(res, "");
        if(res == -1){
            message.setText("Abonnement bloqué, Merci de contacter l'éditeur du logiciel");
        } else if (res == -2) {
            message.setText("Compte bloqué, Merci de contacter l'admin");
        } else if (res == -3) {
            message.setText("Erreur password, Réessayer SVP");
        } else if (res == -4) {
            message.setText("Erreur login, Réessayer SVP");
        }
        MessageManager.showMessage(message);

    }
    
    public String seConnnecter() {
        int res = ejbFacade.seConnnecter(getSelected());
        if (res > 0) {
           // selected.setAbonne(abonneFacade.findByUser(selected));
            int resInitSession = initUserSession(selected, selected.getAbonne());
//             SessionUtil.registerUser(selected); 
            return "/commande/Create";
        }
        validteConnexionForm(res);
        return null;
    }
    
    

    public User getSelected() {
        if (selected == null) {
            selected = new User();
        }
        return selected;
    }

    public void setSelected(User selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UserFacade getFacade() {
        return ejbFacade;
    }

    public User prepareCreate() {
        selected = new User();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UserCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UserUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UserDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<User> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
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

    public User getUser(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<User> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<User> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = User.class)
    public static class UserControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserController controller = (UserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userController");
            return controller.getUser(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof User) {
                User o = (User) object;
                return getStringKey(o.getLogin());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), User.class.getName()});
                return null;
            }
        }

    }

}
