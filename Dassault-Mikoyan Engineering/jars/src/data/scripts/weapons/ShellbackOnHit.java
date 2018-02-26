package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;

import org.lwjgl.util.vector.Vector2f;

import org.lazywizard.lazylib.MathUtils;

import java.util.Random;
import java.awt.Color;

/**
 * A simple on-hit effect for HE guns like the Howler
 *
 * @author Eliza Weisman
 */
public class ShellbackOnHit implements OnHitEffectPlugin
{

    // Declare important values as constants so that our
    // code isn't littered with magic numbers. If we want to
    // re-use this effect, we can easily just copy this class
    // and tweak some of these constants to get a similar effect.
    // -- crit damage -------------------------------------------------------
    // minimum amount of extra damage
    private static final int CRIT_DAMAGE_MIN = 50;
    // maximum amount of extra damage dealt
    private static final int CRIT_DAMAGE_MAX = 100;
    // probability (0-1) of dealing a critical hit
    private static final float CRIT_CHANCE = 0.15f;

    //  -- crit fx ----------------------------------------------------------
    // I took this from the 'core' color of the Howler projectile.
    // It can be changed
    private static final Color EXPLOSION_COLOR = new Color(100, 110, 255, 255);

    // placeholder, please change this once you have a nice explosion sound :)
    private static final String SFX = "istl_ballistic_crit";

    // -- stuff for tweaking particle characteristics ------------------------
    // color of spawned particles
    private static final Color PARTICLE_COLOR = new Color(100, 110, 255, 255);
    // size of spawned particles (possibly in pixels?)
    private static final float PARTICLE_SIZE = 4f;
    // brightness of spawned particles (i have no idea what this ranges from)
    private static final float PARTICLE_BRIGHTNESS = 255f;
    // how long the particles last (i'm assuming this is in seconds)
    private static final float PARTICLE_DURATION = 0.8f;
    private static final int PARTICLE_COUNT = 5;

    // -- particle geometry --------------------------------------------------
    // cone angle in degrees
    private static final float CONE_ANGLE = 150f;
    // constant that effects the lower end of the particle velocity
    private static final float VEL_MIN = 0.15f;
    // constant that effects the upper end of the particle velocity
    private static final float VEL_MAX = 0.25f;

    // one half of the angle. used internally, don't mess with thos
    private static final float A_2 = CONE_ANGLE / 2;

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
                    MathUtils.getRandomNumberInRange(
                            CRIT_DAMAGE_MIN, CRIT_DAMAGE_MAX),
                    DamageType.ENERGY, // damage type
                    100f, // amount of EMP damage (none)
                    false, // does this bypass shields? (no)
                    false, // does this deal soft flux? (no)
                    projectile.getSource());

            // do visual effects ---------------------------------------------
            engine.spawnExplosion(point,
                    target.getVelocity(),
                    EXPLOSION_COLOR, // color of the explosion
                    60f, // sets the size of the explosion
                    0.6f // how long the explosion lingers for
            );

            float speed = projectile.getVelocity().length();
            float facing = projectile.getFacing();
            for (int i = 0; i <= PARTICLE_COUNT; i++)
            {
                float angle = MathUtils.getRandomNumberInRange(facing - A_2,
                        facing + A_2);
                float vel = MathUtils.getRandomNumberInRange(speed * -VEL_MIN,
                        speed * -VEL_MAX);
                Vector2f vector = MathUtils.getPointOnCircumference(null,
                        vel,
                        angle);
                engine.addHitParticle(point,
                        vector,
                        PARTICLE_SIZE,
                        PARTICLE_BRIGHTNESS,
                        PARTICLE_DURATION,
                        PARTICLE_COLOR);
            }
        }
    }

}
