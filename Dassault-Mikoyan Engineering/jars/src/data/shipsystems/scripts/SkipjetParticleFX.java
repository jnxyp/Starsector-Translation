package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.ShipEngineControllerAPI.ShipEngineAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;

import org.lwjgl.util.vector.Vector2f;

import org.lazywizard.lazylib.VectorUtils;
import static org.lazywizard.lazylib.MathUtils.getRandomNumberInRange;

import java.awt.Color;

import data.scripts.DMEUtils;

public class SkipjetParticleFX
{

    // === TWEAKABLE IN SYSTEM SCRIPTS ======================================
    // -- Base size for particles -------------------------------------------
    // This is modified slightly based on a random number and the current
    // effect level of the skipjet system.
    private final float baseSize;
    // -- Base duration of particles -----------------------------------------
    // This increases slightly as the effect level spools up.
    private final float baseDuration;
    // -- Base duration of particles -----------------------------------------
    // This increases slightly as the effect level spools up.
    private final float baseBrightness;
    // -- Base chance to spawn particles this frame --------------------------
    // This chance is checked per engine every frame. It's modified based on
    // the skipjet system's current effect level.
    private final float baseChance;
    // -- Multiplier for particle velocity -----------------------------------
    // The particle velocity is based on the ship's current velocity,
    // multiplied by this value.
    private final float velMult;
    // -- Maximum angle from the engine vector for particle velocity ----------
    // Particles will be spawned with velocity vectors up to this many degrees
    // from either side of the engine's vector.
    private final float coneAngle;
    // -- Color of skipjets when fully spooled up ----------------------------
    // This should be the color that the skipjet system fades the engines
    // toward.
    // Particle color fades from the engine's base color to this as the effect
    // level increases.
    private final Color fullColor;

    // === TWEAKABLE HERE ===================================================
    // weight given to the engine's contribution (to the ship's total
    // velocity) when determining particle chance and size
    private static final float CONTRIBUTION_WEIGHT = 0.2f;
    // weight given to the skipjet system's effect level when determining
    // particle chance and size
    private static final float EFFECT_LEVEL_WEIGHT = 0.05f;

    public SkipjetParticleFX(float baseSize,
            float baseDuration,
            float baseBrightness,
            float baseChance,
            float velMult,
            float coneAngle,
            Color fullColor)
    {
        this.baseSize = baseSize;
        this.baseDuration = baseDuration;
        this.baseBrightness = baseBrightness;
        this.baseChance = baseChance;
        this.velMult = velMult;
        this.coneAngle = coneAngle;
        this.fullColor = fullColor;
    }

    /**
     * Apply the skipjet particle FX to a ship with a current effect level.
     *
     * @param ship the ship using skipjets
     * @param combat the CombatEngineAPI instance (for spawning particles)
     * @param effectLevel the current system effect level.
     */
    public void apply(ShipAPI ship,
            CombatEngineAPI combat,
            float effectLevel)
    {

        // base velocity vector for the ship this frame
        Vector2f baseVel = ship.getVelocity().negate(new Vector2f());

        for (ShipEngineAPI engine : ship.getEngineController()
                .getShipEngines())
        {
            // random chance for some particle jittering fx
            float chance = (effectLevel * EFFECT_LEVEL_WEIGHT)
                    + (engine.getContribution() * CONTRIBUTION_WEIGHT);

            if (engine.isActive() // don't spawn particles for disabled engiens
                    && !engine.isSystemActivated() // or for system-only engines
                    && Math.random() <= baseChance + chance)
            {

                // random angle for particle jitter
                float angle = getRandomNumberInRange(-coneAngle, coneAngle);

                Vector2f vel // this particle's velocity vector
                        = (Vector2f) VectorUtils.rotate(baseVel, angle, new Vector2f())
                        .scale(velMult);

                float size = baseSize * (effectLevel + (float) Math.random());

                Color color // interpolate the color between the engine's base
                        // color and the skipjet color based on effectLevel
                        = DMEUtils.lerpRGB(engine.getEngineColor(),
                                fullColor,
                                effectLevel);
                // spawn the particle
                combat.addSmoothParticle(engine.getLocation(),
                        vel,
                        size,
                        baseBrightness + chance,
                        baseDuration + chance,
                        color
                );

            }

        }
    }
}
