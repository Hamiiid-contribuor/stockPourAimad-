package controler;

import bean.Abonne;
import bean.Famille;
import bean.Magasin;
import bean.Produit;
import bean.Stock;
import bean.User;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import controler.util.SessionUtil;
import service.StockFacade;

import java.io.Serializable;
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

@Named("stockController")
@SessionScoped
public class StockController implements Serializable {

    @EJB
    private service.StockFacade ejbFacade;
    @EJB
    private service.ProduitFacade produitFacade;
    private List<Stock> items = null;
    private Stock selected;
    private List<Produit> produits;
    private Magasin selectedMagasin;
    private Produit selectedProduit;
    private Famille selectedFamille;
    private int seuilAlert=-1;

    public void findByCriteres() {
        System.out.println("hani f controleur o ha les params ==> ");
        System.out.println("seuilAlert ==> "+seuilAlert);
        System.out.println("abonne ==> "+SessionUtil.getConnectedUser().getAbonne());
        System.out.println(" selectedMagasin  ==> "+ selectedMagasin);
        System.out.println("selectedProduit ==> "+selectedProduit);
        System.out.println("selectedFamille ==> "+selectedFamille);
        items=ejbFacade.findByCriteres(SessionUtil.getConnectedUser().getAbonne(), selectedMagasin, selectedProduit, selectedFamille, seuilAlert);
    }

    public void findProduitByFamille() {
        produits = produitFacade.findProduitByFamille(selectedFamille, 0);
    }

    public StockController() {
    }

    public Stock getSelected() {
        if (selected == null) {
            selected = new Stock();
        }
        return selected;
    }

    public void setSelected(Stock selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private StockFacade getFacade() {
        return ejbFacade;
    }

    public Stock prepareCreate() {
        selected = new Stock();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setAbonne(SessionUtil.getConnectedUser().getAbonne());
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("StockCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("StockUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("StockDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Stock> getItems() {
        if (items == null) {
            items = getFacade().findByAbonne(SessionUtil.getConnectedUser().getAbonne());
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

    public Stock getStock(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Stock> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Stock> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Stock.class)
    public static class StockControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StockController controller = (StockController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "stockController");
            return controller.getStock(getKey(value));
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
            if (object instanceof Stock) {
                Stock o = (Stock) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Stock.class.getName()});
                return null;
            }
        }

    }

    public Magasin getSelectedMagasin() {
        if(selectedMagasin==null){
            selectedMagasin= new Magasin();
        }
        return selectedMagasin;
    }

    public void setSelectedMagasin(Magasin selectedMagasin) {
        this.selectedMagasin = selectedMagasin;
    }

    public Produit getSelectedProduit() {
          if(selectedProduit==null){
            selectedProduit= new Produit();
        }
        return selectedProduit;
    }

    public void setSelectedProduit(Produit selectedProduit) {
        this.selectedProduit = selectedProduit;
    }

    public Famille getSelectedFamille() {
         if(selectedFamille==null){
            selectedFamille= new Famille();
        }
        return selectedFamille;
    }

    public void setSelectedFamille(Famille selectedFamille) {
        this.selectedFamille = selectedFamille;
    }

    public int getSeuilAlert() {
        return seuilAlert;
    }

    public void setSeuilAlert(int seuilAlert) {
        this.seuilAlert = seuilAlert;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }

}
