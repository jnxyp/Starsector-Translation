package data.hullmods;

import java.util.List;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import org.apache.log4j.Logger;

/**
 *
 * @author LazyWizard
 */
public class DMEBlockedHullmodDisplayScript extends BaseEveryFrameCombatPlugin implements EveryFrameScript
{
    private static final Logger Log = Logger.getLogger(DMEBlockedHullmodDisplayScript.class);
    private static final String NOTIFICATION_HULLMOD = "DMEBlockedBlankHullmod";
    private static final String NOTIFICATION_SOUND = "cr_allied_critical";
    private static ShipAPI ship;

    public static void showBlocked(ShipAPI blocked)
    {
        stopDisplaying();

        ship = blocked;
        ship.getVariant().addMod(NOTIFICATION_HULLMOD);
        Global.getSoundPlayer().playUISound(NOTIFICATION_SOUND, 1f, 1f);
    }

    @Override
    public boolean isDone()
    {
        return false;
    }

    @Override
    public boolean runWhilePaused()
    {
        return true;
    }

    public static void stopDisplaying()
    {
        if (ship != null)
        {
            Log.debug("Removed from existing ship.");
            ship.getVariant().removeMod(NOTIFICATION_HULLMOD);
            ship = null;
        }
    }

    @Override
    public void advance(float amount)
    {
        stopDisplaying();
    }

    @Override
    public void advance(float amount, List<InputEventAPI> events)
    {
        stopDisplaying();
    }

    @Override
    public void init(CombatEngineAPI engine)
    {
        if (Global.getSettings().getCurrentState() != GameState.TITLE)
        {
            stopDisplaying();
            Global.getCombatEngine().removePlugin(this);
        }
    }
}
