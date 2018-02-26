package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import data.hullmods.DMEBlockedHullmodDisplayScript;
import exerelin.campaign.SectorManager;

import data.scripts.world.ISTLGen;
import org.dark.shaders.light.LightData;
import org.dark.shaders.util.ShaderLib;
import org.dark.shaders.util.TextureData;
//import data.scripts.world.systems.Nikolaev;
//import data.scripts.world.systems.Martinique;


public class DMEModPlugin extends BaseModPlugin
{
    // Missiles and weapons that use custom AI
    // public static final String GENERIC_WEAPON_ID = "generic_weapon";
    public static final boolean isExerelin;

        static
    {
        boolean foundExerelin;
        try
        {
            Global.getSettings().getScriptClassLoader().loadClass("data.scripts.world.ExerelinGen");
            foundExerelin = true;
        }
        catch (ClassNotFoundException ex)
        {
            foundExerelin = false;
        }

        isExerelin = foundExerelin;
    }
    
    private static void initDME()
    {
        //new Nikolaev().generate(Global.getSector());
        //new Martinique().generate(Global.getSector());
        //new Kostroma().generate(Global.getSector());
        //new Besson().generate(Global.getSector());
        //new Aleph().generate(Global.getSector());
        
        new ISTLGen().generate(Global.getSector());
    }

    @Override
    public void onNewGame()
    {
        if (isExerelin && !SectorManager.getCorvusMode())
        {
            return;
        }
        initDME();
    }
    
    @Override
    public void onGameLoad(boolean newGame)
    {
        Global.getSector().addTransientScript(new DMEBlockedHullmodDisplayScript());
    }
    
    
      @Override
    public void onApplicationLoad()
    {
        boolean hasLazyLib = Global.getSettings().getModManager().isModEnabled("lw_lazylib");
        if (!hasLazyLib) {
            throw new RuntimeException("Dassault-Mikoyan Engineering requires LazyLib!");
        }
        boolean hasGraphicsLib = Global.getSettings().getModManager().isModEnabled("shaderLib");
        if (hasGraphicsLib) {
            ShaderLib.init();
            LightData.readLightDataCSV("data/lights/istl_light_data.csv");
            TextureData.readTextureDataCSV("data/lights/istl_texture_data.csv");
        }
        if (!hasGraphicsLib) {
            throw new RuntimeException("Dassault-Mikoyan Engineering requires GraphicsLib!");
        }
    }
}
