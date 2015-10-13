package com.geospatialcorporation.android.geomobile.models;

public class UserAccount {


    //region Getters & Setters
    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getCellPhone() {
        return CellPhone;
    }

    public void setCellPhone(String cellPhone) {
        CellPhone = cellPhone;
    }

    public String getOfficePhone() {
        return OfficePhone;
    }

    public void setOfficePhone(String officePhone) {
        OfficePhone = officePhone;
    }
    //endregion

    //region Properties
    private Integer Id;
    private String Email;
    private String FirstName;
    private String LastName;
    private String CellPhone;
    private String OfficePhone;

    public String getFormattedOfficePhone() {
        if(OfficePhone != null){
            return formatNumber(OfficePhone);
        }

        return OfficePhone;
    }

    public String getFormattedCellPhone() {
        if(CellPhone != null){
            return formatNumber(CellPhone);
        }

        return CellPhone;
    }

    private String formatNumber(String number){
        if(number.length() == 10) {
            return "(" + number.substring(0, 3) + ") " + number.substring(3, 6) + "-" + number.substring(6);
        } else {
            return number;
        }
    }
    //endregion

}
