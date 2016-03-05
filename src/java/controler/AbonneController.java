package controler;

import bean.Abonne;
import controler.util.FileUtil;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import java.io.IOException;
import service.AbonneFacade;

import java.io.Serializable;
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
import org.primefaces.model.UploadedFile;

@Named("abonneController")
@SessionScoped
public class AbonneController implements Serializable {

    @EJB
    private service.AbonneFacade ejbFacade;
    private List<Abonne> items = null;
    private Abonne selected;
    private UploadedFile file;

    private String validateView() {
        String msg = "";
        if (selected.getCheminDossier().equals("")) {
            msg = "Merci de spécifier votre chemin_dossier !!";
        }
        if (file.getSize() == 0) {
            msg += "\nVeuillez Ajouter une photo de votre Logo !!";
        }
        return msg;
    }

    public void save() throws IOException {
        String nomDossier = "Logo";
        String outputPath = "";
        System.out.println(validateView());
        if (validateView().equals("")) {
            System.out.println("in");
            getFacade().edit(selected);
            selected = getFacade().find(getFacade().generateId());
            selected.setCheminDossier(FileUtil.generateFiles(selected.getCheminDossier() + new Date().getTime()));
            outputPath = FileUtil.uploadV2(file, nomDossier, selected);
            selected.setLogo(outputPath);
            getFacade().edit(selected);
            file = null;
            JsfUtil.addSuccessMessage("Creation avec succes");
        } else {
            JsfUtil.addErrorMessage(validateView());
        }

    }

    public AbonneController() {
    }

    public Abonne getSelected() {
        return selected;
    }

    public void setSelected(Abonne selected) {
        this.selected = selected;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AbonneFacade getFacade() {
        return ejbFacade;
    }

    public Abonne prepareCreate() {
        selected = new Abonne();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AbonneCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AbonneUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AbonneDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Abonne> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
                    save();
                } else if (persistAction == PersistAction.UPDATE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                //JsfUtil.addSuccessMessage(successMessage);
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

    public Abonne getAbonne(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Abonne> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Abonne> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Abonne.class)
    public static class AbonneControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AbonneController controller = (AbonneController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "abonneController");
            return controller.getAbonne(getKey(value));
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
            if (object instanceof Abonne) {
                Abonne o = (Abonne) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Abonne.class.getName()});
                return null;
            }
        }

    }

}
