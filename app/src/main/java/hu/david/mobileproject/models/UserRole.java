package hu.david.mobileproject.models;

public class UserRole {
    private String id;
    private String href;
    private String involvementRole;
    //private Entitlement[] entitlement;


    public UserRole() {
    }

    public UserRole(String id, String href, String involvementRole) {
        this.id = id;
        this.href = href;
        this.involvementRole = involvementRole;
        //this.entitlement = new Entitlement[]{new Entitlement("Netflix configuration", "R&W"), new Entitlement("Sport basic package", "watch")};
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getInvolvementRole() {
        return involvementRole;
    }

    public void setInvolvementRole(String involvementRole) {
        this.involvementRole = involvementRole;
    }

    /*public Entitlement[] getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(Entitlement[] entitlement) {
        this.entitlement = entitlement;
    }*/
}
