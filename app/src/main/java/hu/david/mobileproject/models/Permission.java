package hu.david.mobileproject.models;

import java.time.Period;
import java.util.Date;

public class Permission {
    private String id;
    private String href;
    private Date date;
    private String description;
    //Javaban a period valtja ki a TimePeriod tipust
    private Period period;
    private InvolvementIdentificationRef user;
    private InvolvementIdentificationRef granter;
    private Privilege[] privilege;
    private AssetInvolvementRole[] assetUserRole;
}
