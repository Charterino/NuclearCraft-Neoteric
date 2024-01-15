package igentuman.nc.util;

import net.minecraftforge.fml.ModList;

public class ModUtil {
    protected static boolean initialized = false;
    protected static boolean isMekanismLoadeed;
    protected static boolean ccLoaded;
    protected static boolean ieLoaded;
    protected static boolean gtLoaded;
    protected static void initialize()
    {
        if(initialized)
            return;
        initialized = true;
        isMekanismLoadeed = ModList.get().isLoaded("mekanism");
        ccLoaded = ModList.get().isLoaded("computercraft");
        ieLoaded = ModList.get().isLoaded("immersiveengineering");
        gtLoaded = ModList.get().isLoaded("gtceu");
    }

    public static boolean isMekanismLoadeed() {
        initialize();
        return isMekanismLoadeed;
    }

    public static boolean isCcLoaded() {
        initialize();
        return ccLoaded;
    }

    public static boolean isIeLoaded() {
        initialize();
        return ieLoaded;
    }

    public static boolean isGtLoaded() {
        initialize();
        return gtLoaded;
    }
}
