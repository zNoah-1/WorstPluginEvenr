package zkyubz.poolcraft.lobbygames.poolsumo;

public class ThisPlugin {
    private static MainShit instance;

    static void setInstance(MainShit plInstance){
        instance = plInstance;
    }

    static MainShit getInstance(){
        return instance;
    }
}

