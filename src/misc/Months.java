package misc;

import java.io.Serializable;

/**
 * Months
 * Aid enum-class for DateSystem
 * Implements months of the year and bounds them to their corresponding rank
 */
public enum Months implements Serializable {

    JANUARY (1),
    FEBRUARY (2),
    MARCH (3),
    APRIL (4),
    MAY (5),
    JUNE (6),
    JULY (7),
    AUGUST (8),
    SEPTEMBER (9),
    OCTOBER (10),
    NOVEMBER (11),
    DECEMBER (12);

    private final Integer rank;

    public static final long serialVersionUID = 100L;

    Months(int code) {
        this.rank = code;
    }
    public int getCode(){
        return this.rank;
    }
}
