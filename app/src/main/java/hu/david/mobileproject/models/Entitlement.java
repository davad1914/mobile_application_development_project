package hu.david.mobileproject.models;

public class Entitlement {
    private String function;
    private String action;

    public Entitlement(String function, String action) {
        this.function = function;
        this.action = action;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
