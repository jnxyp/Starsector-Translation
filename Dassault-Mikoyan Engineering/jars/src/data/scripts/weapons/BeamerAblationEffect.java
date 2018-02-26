package data.scripts.weapons;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.BeamEffectPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.ShipAPI;

import java.awt.Color;

public abstract class BeamerAblationEffect implements BeamEffectPlugin
{

    /**
     * Likelihood of the beam causing an ablation explosion. This should be
     * within the range (0 - 1) and should be very small.
     */
    abstract protected float ablationChance();

    /**
     * How large the explosion effect should be.
     */
    abstract protected float explosionSize();

    // I took this from the 'core' color of the Howler projectile.
    // It can be changed
    private static final Color EXPLOSION_COLOR = new Color(125, 100, 255, 155);

    // placeholder, please change this once you have a nice explosion sound :)
    private static final String SFX = "istl_energy_crit";

    @Override
    public void advance(float amount, CombatEngineAPI engine, BeamAPI beam)
    {
        if (beam.getDamageTarget() instanceof ShipAPI)
        {

            ShipAPI ship = (ShipAPI) beam.getDamageTarget();
            boolean hitShield = ship.getShield() != null
                    && ship.getShield().isWithinArc(beam.getTo());

            if (!hitShield && Math.random() <= ablationChance())
            {
                Vector2f dir = Vector2f.sub(beam.getTo(), beam.getFrom(), new Vector2f());
                if (dir.lengthSquared() > 0)
                {
                    dir.normalise();
                }
                dir.scale(2f);
                Vector2f point = Vector2f.sub(beam.getTo(), dir, new Vector2f());
                float damageAmount = beam.getDamage().getDamage() * 1.5f;

                // apply the extra damage to the target
                engine.applyDamage(ship, point, // where to apply damage
                        damageAmount, // amount of damage
                        DamageType.ENERGY, // damage type
                        25f, // amount of EMP damage (none)
                        false, // does this bypass shields? (no)
                        false, // does this deal soft flux? (no)
                        beam.getSource());

                // get the target's velocity to render the crit FX
                Vector2f v_target = new Vector2f(ship.getVelocity());

                // do visual effects
                engine.spawnExplosion(point, v_target,
                        EXPLOSION_COLOR,
                        explosionSize(),
                        1.2f // how long the explosion lingers for
                );
                // play a sound
                Global.getSoundPlayer()
                        .playSound(SFX, 1.1f, 0.5f,
                                ship.getLocation(),
                                v_target);
            }

        }
    }

}
