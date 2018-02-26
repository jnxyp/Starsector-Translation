package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;

import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;

/**
 * A simple on-hit effect for HE guns like the Howler
 *
 * @author Eliza Weisman
 */
public class HeavyLinearOnHit implements OnHitEffectPlugin
{

    // Declare important values as constants so that our
    // code isn't littered with magic numbers. If we want to
    // re-use this effect, we can easily just copy this class
    // and tweak some of these constants to get a similar effect.
    // minimum amount of extra damage
    private static final int EXTRA_DAMAGE = 60;
    // I took this from the 'core' color of the Howler projectile.
    // It can be changed
    // private static final Color EXPLOSION_COLOR = new Color(155,225,255,255);
    private static final Color EXPLOSION_COLOR = new Color(155,225,255,255);

    // placeholder, please change this once you have a nice explosion sound :)
    private static final String SFX = "istl_energy_crit";

    @Override
    public void onHit(DamagingProjectileAPI projectile,
            CombatEntityAPI target,
            Vector2f point,
            boolean shieldHit,
            CombatEngineAPI engine)
    {

        // check whether or not we want to apply critical damage
        if (target instanceof ShipAPI && !shieldHit)
        {

            // apply the extra damage to the target
            engine.applyDamage(target, point, // where to apply damage
                    EXTRA_DAMAGE, // amount of damage
                    DamageType.ENERGY, // damage type
                    75f, // amount of EMP damage
                    false, // does this bypass shields? (no)
                    false, // does this deal soft flux? (no)
                    projectile.getSource());

            // get the target's velocity to render the crit FX
            Vector2f v_target = new Vector2f(target.getVelocity());

            // do visual effects
            engine.spawnExplosion(point, v_target,
                    EXPLOSION_COLOR, // color of the explosion
                    45f, // sets the size of the explosion
                    0.6f // how long the explosion lingers for
            );
        }
    }

}
