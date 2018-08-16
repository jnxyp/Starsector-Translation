package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

/**
 * An on-hit particle effect that kind of badly simulates sparks or bullets
 * ricocheting off armour.
 *
 * @author Eliza Weisman
 */
public class LinearMGSparks implements OnHitEffectPlugin
{

    // likelihood of creating sparks (0 - 1)
    private static final float SPARK_CHANCE = 0.7f;

    // -- stuff for tweaking particle characteristics ------------------------
    // color of spawned particles
    private static final Color PARTICLE_COLOR = new Color(155,225,255,255);
    // size of spawned particles (possibly in pixels?)
    private static final float PARTICLE_SIZE = 2f;
    // brightness of spawned particles (i have no idea what this ranges from)
    private static final float PARTICLE_BRIGHTNESS = 255f;
    // how long the particles last (i'm assuming this is in seconds)
    private static final float PARTICLE_DURATION = 0.6f;
    private static final int PARTICLE_COUNT = 1;

    // -- particle geometry --------------------------------------------------
    // cone angle in degrees
    private static final float CONE_ANGLE = 100f;
    // constant that effects the lower end of the particle velocity
    private static final float VEL_MIN = 0.1f;
    // constant that effects the upper end of the particle velocity
    private static final float VEL_MAX = 0.2f;

    // one half of the angle. used internally, don't mess with thos
    private static final float A_2 = CONE_ANGLE / 2;

    @Override
    public void onHit(DamagingProjectileAPI projectile,
            CombatEntityAPI target,
            Vector2f point,
            boolean shieldHit,
            CombatEngineAPI engine)
    {
        // Check if we hit a ship (not its shield)
        if (target instanceof ShipAPI
                && !shieldHit
                && Math.random() <= SPARK_CHANCE)
        {

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
            // soren: this is where you could add an EMP effect if you wanted
            // to kitbash this together with the pulser EMP script or something
        }
    }
}
