package misc;

import java.io.Serializable;

/**
 * MyPair
 * Pre-made Pair class from Java would not work :(
 * Basic implementation (for integers only)
 * Holds head, value
 */
public record MyPair(Integer head, Integer value) implements Serializable {

    public static final long serialVersionUID = 10L;

    //Getters
    public Integer getHead() {
        return this.head;
    }
    public Integer getValue() {
        return this.value;
    }
}

