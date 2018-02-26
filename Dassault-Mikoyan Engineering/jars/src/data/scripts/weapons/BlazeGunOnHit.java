package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;

import org.lwjgl.util.vector.Vector2f;

import java.util.Random;
import java.awt.Color;

/**
 * A simple on-hit effect for HE guns like the Howler
 *
 * @author Eliza Weisman
 */
public class BlazeGunOnHit implements OnHitEffectPlugin
{

    // Declare important values as constants so that our
    // code isn't littered with magic numbers. If we want to
    // re-use this effect, we can easily just copy this class
    // and tweak some of these constants to get a similar effect.
    // minimum amount of extra damage
    private static final int CRIT_DAMAGE_MIN = 0;
    // maximum amount of extra damage dealt
    private static final int CRIT_DAMAGE_MAX = 15;
    // probability (0-1) of dealing a critical hit
    private static final float CRIT_CHANCE = 1.0f;
    // I took this from the 'core' color of the Howler projectile.
    // It can be changed
    // private static final Color EXPLOSION_COLOR = new Color(255,225,200,200);
    private static final Color EXPLOSION_COLOR = new Color(255, 120, 75, 255);

    // placeholder, please change this once you have a nice explosion sound :)
    private static final String SFX = "istl_ballistic_crit";

    private static Random rng = new Random();

    // if we were using LazyLib, we could just use its'
    // `getRandomNumberInRange()` method, but this works fine too.
    private static float damageAmount()
    {
        return (float) (rng.nextInt((CRIT_DAMAGE_MAX - CRIT_DAMAGE_MIN) + 1) + CRIT_DAMAGE_MIN);
    }

    @Override
    public void onHit(DamagingProjectileAPI projectile,
            CombatEntityAPI target,
            Vector2f point,
            boolean shieldHit,
            CombatEngineAPI engine)
    {

        // check whether or not we want to apply critical damage
        if (target instanceof ShipAPI && !shieldHit && Math.random() <= CRIT_CHANCE)
        {

            // apply the extra damage to the target
            engine.applyDamage(target, point, // where to apply damage
                    damageAmount(), // amount of damage
                    DamageType.ENERGY, // damage type
                    0f, // amount of EMP damage (none)
                    false, // does this bypass shields? (no)
                    false, // does this deal soft flux? (no)
                    projectile.getSource());

            // get the target's velocity to render the crit FX
            Vector2f v_target = new Vector2f(target.getVelocity());

            // do visual effects
            engine.spawnExplosion(point, v_target,
                    EXPLOSION_COLOR, // color of the explosion
                    25f, // sets the size of the explosion
                    0.6f // how long the explosion lingers for
            );
        }
    }

}
