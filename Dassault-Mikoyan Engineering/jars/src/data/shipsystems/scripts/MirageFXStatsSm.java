package data.shipsystems.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;

import java.awt.Color;

import data.scripts.DMEUtils;

import static org.lazywizard.lazylib.MathUtils.getRandomPointOnCircumference;
import static org.lazywizard.lazylib.MathUtils.getRandomNumberInRange;

import org.lwjgl.util.vector.Vector2f;

/**
 * FX for the Dassault-Mikoyan MIRAGE System.
 *
 * "MIRAGE", "MIRAGE System", and the phrase "MIRAGE System operational" are
 * registered trademarks of Dassault-Mikoyan Engineering.
 *
 * DISCLAIMER: The MIRAGE system is not a Phase Skimmer. Any resemblance to a
 * Phase Skimmer is purely coincidental.
 *
 * If you are pregnant, intend to become pregnant, have ever been pregnant, or
 * were the result of a pregnancy, do not operate MIRAGE System. Do not operate
 * spacecraft or other heavy machinery while under the effects of the MIRAGE
 * System. Prolonged exposure to the hyperspace pseudo-fluid brane may result in
 * headaches, body aches, vomiting, death, depression, space madness,
 * hallucinations, unreality, hyperreality, relativistic, non-relativistic, and
 * pseudo-relativistic time dilation, and other side effects. If you experience
 * an erection that lasts longer than 4 hours, contact a medical professional.
 * Your delta-V may vary.
 *
 * @author Eliza Weisman
 */
public class MirageFXStatsSm implements ShipSystemStatsScript
{
    // -- Colors for particles ---------------------------------------------
    // The particles generated will range randomly between these.
    private static final Color PARTICLE_COLOR_1 = new Color(145, 175, 255);
    // I picked this color arbitrarily, please make it something not
    // shitty.
    private static final Color PARTICLE_COLOR_2 = new Color(85, 115, 215);

    // -- Particle size -----------------------------------------------------
    // The particles generated will range randomly between the minimum and
    // maximum sizes.
    private static final float PARTICLE_SIZE_MIN = 4f;
    private static final float PARTICLE_SIZE_MAX = 8f;
    // Max number of particles drawn per frame.
    private static final int PARTICLE_MAX = 21;
    // How long particles last for
    private static final float PARTICLE_DURATION = 0.66f;
    // Multiplier for particle velocity
    private static final float PARTICLE_VELOCITY_MULT = 1.2f;
    // Modifier for particle max starting radius.
    // The ship's collision radius is multiplied by this to get the
    // max particle range.
    private static final float MAX_RADIUS_MULT = 1.25f;

    @Override
    public void apply(MutableShipStatsAPI stats,
            String id, State state,
            float effectLevel)
    {
        // instanceof also acts as a null check
        if (!(stats.getEntity() instanceof ShipAPI))
        {
            return;
        }

        ShipAPI ship = (ShipAPI) stats.getEntity();
        CombatEngineAPI engine = Global.getCombatEngine();

        // if the MIRAGE system is charging, draw some GN particles
        // getting sucked in.
        if (state == State.IN)
        {
            // Soren, if you like, I could also add some ShaderLib effects
            // here.
            //   - Eliza
            final Vector2f loc = new Vector2f(ship.getLocation());
            final float min_radius = ship.getCollisionRadius();
            final float max_radius = min_radius * MAX_RADIUS_MULT;

            final int n_particles = Math.round(
                    // As the MIRAGE charges up, the number of particles spawned
                    // decreases. This is so we don't make a weird-looking particle
                    // ring as we jump.
                    PARTICLE_MAX - (effectLevel * PARTICLE_MAX)
            );

            for (int i = 0; i < n_particles; i++)
            {
                float radius = getRandomNumberInRange(min_radius, max_radius);
                // particle starting position
                Vector2f pos = getRandomPointOnCircumference(loc, radius);
                // particle starting velocity
                Vector2f vel = (Vector2f) Vector2f.sub(loc, pos, new Vector2f())
                        .scale(PARTICLE_VELOCITY_MULT);
                final float size = getRandomNumberInRange(PARTICLE_SIZE_MIN,
                        PARTICLE_SIZE_MAX);
                final float brightness = (float) Math.random();
                final Color color
                        = DMEUtils.lerpRGB(PARTICLE_COLOR_2, PARTICLE_COLOR_1,
                                // if you would rather it be random, comment out
                                // "effectLevel" and uncomment the next line. - eliza
                                effectLevel
                        // (float)Math.random()
                        );

                engine.addSmoothParticle(pos, vel, size, brightness,
                        PARTICLE_DURATION,
                        color
                );
            }
        }
    }

    @Override
    public StatusData getStatusData(int index, State state, float effectLevel)
    {
        if (state == State.IN && index == 0)
        {
            return new StatusData("Skipspace jaunt in progress", false);
        }

        return null;
    }

    @Override
    public void unapply(MutableShipStatsAPI stats, String id)
    {
    }

}
