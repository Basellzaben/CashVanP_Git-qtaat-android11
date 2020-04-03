package com.cds_jo.GalaxySalesApp.assist;

public class Cls_Man_Vac {
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getManId() {
        return ManId;
    }

    public void setManId(String manId) {
        ManId = manId;
    }

    public String getCustId() {
        return CustId;
    }

    public void setCustId(String custId) {
        CustId = custId;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    String Id;
    String ManId;
    String CustId;
    String FromDate;
    String ToDate;
    String Note;

    public String getProcedureType() {
        return ProcedureType;
    }

    public void setProcedureType(String procedureType) {
        ProcedureType = procedureType;
    }

    public String getVacationType() {
        return VacationType;
    }

    public void setVacationType(String vacationType) {
        VacationType = vacationType;
    }

    String ProcedureType;
    String VacationType;

    public String getProcedureType_Desc() {
        return ProcedureType_Desc;
    }

    public void setProcedureType_Desc(String procedureType_Desc) {
        ProcedureType_Desc = procedureType_Desc;
    }

    public String getVacationType_Desc() {
        return VacationType_Desc;
    }

    public void setVacationType_Desc(String vacationType_Desc) {
        VacationType_Desc = vacationType_Desc;
    }

    String ProcedureType_Desc;

    public String getVacDays() {
        return VacDays;
    }

    public void setVacDays(String vacDays) {
        VacDays = vacDays;
    }

    String VacDays ;
    String VacationType_Desc;
}
