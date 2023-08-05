package misc;

import java.io.Serializable;

/**
 * Features
 * All accommodations should be judged around some discreet global features
 * like wi-fi, parking, smoking-free areas, pets e.t.c.
 */
public enum Features implements Serializable {
    WIFI,       //is it free?
    PARKING,
    SMOKING,    //is it allowed?
    PETS,       //are they allowed?
    ELEVATOR,
    BAR,
    POOL,
    SPA,
    PROXIMITY;  //is it close to the city center?

    public static final long serialVersionUID = 33L;
}
