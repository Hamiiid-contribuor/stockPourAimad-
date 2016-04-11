package controler;

import bean.Commande;
import bean.DevisCommande;
import bean.DevisCommandeItem;
import bean.Produit;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.Message;
import controler.util.MessageManager;
import controler.util.SessionUtil;
import java.io.IOException;
import service.DevisCommandeFacade;

import java.io.Serializable;
import java.util.Date;
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
import net.sf.jasperreports.engine.JRException;
import service.DevisCommandeItemFacade;

@Named("devisCommandeController")
@SessionScoped
public class DevisCommandeController implements Serializable {

    @EJB
    private service.DevisCommandeFacade ejbFacade;
    @EJB
    private service.DevisCommandeItemFacade devisCommandeItemFacade;
    private List<DevisCommande> items = null;
    private DevisCommande selected;
    private Message message;
    private DevisCommandeItem devisCommandeItem;
    private Produit selectedProduit = new Produit();
    private int booleanSwitch = 0;
    private DevisCommande objetRecherche;
    private Date dateDevisCommandeMin;
    private Date dateDevisCommandeMax;

    public DevisCommandeController() {
    }

    public DevisCommande getSelected() {
        if (selected == null) {
            selected = new DevisCommande();
        }
        return selected;
    }

    public void setSelected(DevisCommande selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DevisCommandeFacade getFacade() {
        return ejbFacade;
    }

    public DevisCommande prepareCreate() {
        selected = new DevisCommande();
        initializeEmbeddableKey();
        return selected;
    }

    public Produit getSelectedProduit() {
        if (selectedProduit == null) {
            selectedProduit = new Produit();
        }
        return selectedProduit;
    }

    public Date getDateDevisCommandeMin() {
        return dateDevisCommandeMin;
    }

    public void setDateDevisCommandeMin(Date dateDevisCommandeMin) {
        this.dateDevisCommandeMin = dateDevisCommandeMin;
    }

    public Date getDateDevisCommandeMax() {
        return dateDevisCommandeMax;
    }

    public void setDateDevisCommandeMax(Date dateDevisCommandeMax) {
        this.dateDevisCommandeMax = dateDevisCommandeMax;
    }

    public DevisCommande getObjetRecherche() {
        if (objetRecherche == null) {
            objetRecherche = new DevisCommande();
        }
        return objetRecherche;
    }

    public void setObjetRecherche(DevisCommande objetRecherche) {
        this.objetRecherche = objetRecherche;
    }

    public void setSelectedProduit(Produit selectedProduit) {
        this.selectedProduit = selectedProduit;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DevisCommandeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DevisCommandeUpdated"));
        items.set(items.indexOf(selected), ejbFacade.find(selected.getId())); // cancel valu

    }

    public void destroy() {
        devisCommandeItemFacade.removeAllDevisCommandeItemsFromDevisCommande(selected);
        selected.getDevisCommandeItems().clear();//vider la lists des items attacher a mon devis 
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DevisCommandeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
//            ejbFacade.deleteDeviCommande(selected);
            items.remove(items.indexOf(selected));
            selected = new DevisCommande(); // Remove selection
        }
    }

    public void destroy(DevisCommande devisCommande) {
        selected = devisCommande;
        destroy();

    }

    public void update(DevisCommande devisCommande) {
        selected = devisCommande;
    }

    public List<DevisCommande> getItems() {
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

    public DevisCommande getDevisCommande(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<DevisCommande> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<DevisCommande> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public DevisCommandeFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(DevisCommandeFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public DevisCommandeItemFacade getDevisCommandeItemFacade() {
        return devisCommandeItemFacade;
    }

    public int getBooleanSwitch() {
        return booleanSwitch;
    }

    public void setBooleanSwitch(int booleanSwitch) {
        this.booleanSwitch = booleanSwitch;
    }

    public void setDevisCommandeItemFacade(DevisCommandeItemFacade devisCommandeItemFacade) {
        this.devisCommandeItemFacade = devisCommandeItemFacade;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public DevisCommandeItem getDevisCommandeItem() {
        if (devisCommandeItem == null) {
            devisCommandeItem = new DevisCommandeItem();
        }
        return devisCommandeItem;
    }

    public void setDevisCommandeItem(DevisCommandeItem devisCommandeItem) {
        this.devisCommandeItem = devisCommandeItem;
    }

    @FacesConverter(forClass = DevisCommande.class)
    public static class DevisCommandeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DevisCommandeController controller = (DevisCommandeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "devisCommandeController");
            return controller.getDevisCommande(getKey(value));
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
            if (object instanceof DevisCommande) {
                DevisCommande o = (DevisCommande) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), DevisCommande.class.getName()});
                return null;
            }
        }

    }

    //*************************************************Hamid***************************************************
    private void validateView() {
        if (getSelected().getProjet().getId() == null) {
            message = MessageManager.createErrorMessage(-1, "Merci de selectionner un projet");
        } else if (getSelected().getResponsable().getId() == null) {
            message = MessageManager.createErrorMessage(-2, "Merci de selectionner un Responsable");
        } else if (getSelected().getFournisseur().getId() == null) {
            message = MessageManager.createErrorMessage(-3, "Merci de selectionner un Fournisseur");
        } else if (getSelected().getDevisCommandeItems().isEmpty()) {
            message = MessageManager.createErrorMessage(-4, "Merci de selectionner un Produit");
        } else {
            message = MessageManager.createInfoMessage(1, "DevisCommande crée avec succes");
        }

    }

    public void createDevisCommande() {
        validateView();
        if (message.getResultat() > 0) {
            ejbFacade.createDevisCommande(getSelected(), SessionUtil.getConnectedUser());
            items.add(selected);
            selected = new DevisCommande();
        }
        MessageManager.showMessage(message);

    }

    private void createDevisCommandeItem(Boolean devisCommandeExist) {
        DevisCommandeItem devisCommandeItemCloned = devisCommandeItemFacade.cloneDevisCommande(selected, devisCommandeItem, selectedProduit);
        getSelected().getDevisCommandeItems().add(devisCommandeItemCloned);
        if (devisCommandeExist) {
            devisCommandeItemFacade.createDevisCommandeItemForAnExistingDevisCommande(devisCommandeItem, selected);
        } else {
            getSelected().setMontantTotal(ejbFacade.calculerMontantTotalDevisCommande(getSelected().getDevisCommandeItems()));
        }
    }

    public void createDevisCommandeItem() {
        createDevisCommandeItem(false);
        devisCommandeItem = new DevisCommandeItem();
    }

    public void createDevisCommandeItemForExestingDevisCommande() {
        createDevisCommandeItem(true);
    }

    public void deleteDevisCommandeItem(DevisCommandeItem devisCommandeItemToDelete) {
        getSelected().getDevisCommandeItems().remove(getSelected().getDevisCommandeItems().indexOf(devisCommandeItemToDelete));
        getSelected().setMontantTotal(ejbFacade.calculerMontantTotalDevisCommande(getSelected().getDevisCommandeItems()));
    }

    private void clearView() {
        getSelected().getDevisCommandeItems().clear(); // ca sert  a quoi cet clear ?????
    }

    private void clearOtherViews(DevisCommande devisCommande) {
        if (getSelected() != null && devisCommande != null) {
            if (!Objects.equals(getSelected().getId(), devisCommande.getId())) {
                clearView();

            }
        }
    }

    public void findDevisCommandeItemsByIdDevisCommande(DevisCommande devisCommande) {
        booleanSwitch = 1;
        clearOtherViews(devisCommande);
        selected = devisCommande;
        selected.setDevisCommandeItems(devisCommandeItemFacade.findDevisCommandeItemsByIdDevisCommande(devisCommande));
    }

    private Message validateSerarchForm() {
        if (dateDevisCommandeMin != null && dateDevisCommandeMax != null) {
            if (dateDevisCommandeMin.getTime() > dateDevisCommandeMax.getTime()) {
                message = MessageManager.createErrorMessage(-1, "la dateDevisCommandeMax doit etre superieur au dateDevisCommandeMin");
            }
        } else {
            message = MessageManager.createErrorMessage(1, ""); // on affiche rien dans ce cas la 
        }
        return message;

    }

    public void findByCriteres() {
        validateSerarchForm();
        if (message != null && message.getResultat() > 0) {
            items = ejbFacade.findByCriteres(objetRecherche, SessionUtil.getConnectedUser().getAbonne(), dateDevisCommandeMin, dateDevisCommandeMax);
            SessionUtil.setAttribute("ListaLesDevisCommande", items);//pourqoui enregistrer cet list dans la session ??
            selected = new DevisCommande();
        } else if (message.getResultat() < 0) {
            MessageManager.showMessage(message);
        }
        clearView();
    }

    public void removeDevisCommandeItem(DevisCommandeItem devisCommandeItem) {
        this.devisCommandeItem = devisCommandeItem;
        this.devisCommandeItem.setDevisCommande(selected);
        removeDevisCommandeItem();
    }

    public void removeDevisCommandeItem() {

        devisCommandeItemFacade.removeDevisCommandeItem(devisCommandeItem);
        selected.getDevisCommandeItems().remove((devisCommandeItem));

    }
    
    //*********************************generatePdf ************************
    
    
    public void generatePdf (DevisCommande devisCommande) throws JRException, IOException{
        System.out.println("ha sahbna libaydouz el jasper ------> "+devisCommande);
        ejbFacade.generatePdf(devisCommande);
        FacesContext.getCurrentInstance().responseComplete();
    }
}
