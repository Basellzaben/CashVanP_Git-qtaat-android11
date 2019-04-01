package com.cds_jo.GalaxySalesApp;

/**
 * Created by Hp on 14/06/2016.
 */
public enum SCR_ACTIONS {
    open (1),
    Insert(2),
    Modify(3),
    Delete(4),
    Write(5),
    Report (6),
    Print (7),
    Read (8),
    Share (9),
    SaveAndPrint (10),
    CancelApproved(11),
    FinallApproval (12),
    DeleteRow(13),
    Agree (14),
    Payment(15);

    private final int value;
    SCR_ACTIONS(int type)
    {
        this.value = type;
    }


    public int getValue() {

        return this.value;

    }

}
