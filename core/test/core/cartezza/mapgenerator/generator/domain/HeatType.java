package core.cartezza.mapgenerator.generator.domain;

/**
 * Created by zachcarter on 12/15/16.
 */
public enum HeatType {
    Coldest(0),
    Colder(1),
    Cold(2),
    Warm(3),
    Warmer(4),
    Warmest(5);

    private int numVal;

    HeatType(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }

    /**
     * Created by zachcarter on 12/15/16.
     */
    public static class MathHelper {

        public static int mod(int x, int m)
        {
            int r = x % m;
            return r < 0 ? r + m : r;
        }
    }
}
