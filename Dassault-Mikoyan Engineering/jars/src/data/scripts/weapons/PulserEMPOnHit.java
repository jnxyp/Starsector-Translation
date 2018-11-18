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
 * A critical hit script for the Hybrid Pulser.
 *
 * This script does two things: 1. If we have struck a ship (not a shield), make
 * an explosion doing some random bonus energy damage, plus some extra EMP. 2.
 * Sometimes, randomly create an EMP arc. The damage and EMP for the arc are
 * calculated based on the weapon's base damage.
 *
 * @author Eliza Weisman
 */
public class PulserEMPOnHit implements OnHitEffectPlugin
{

    // Declare important values as constants so that our
    // code isn't littered with magic numbers. If we want to
    // re-use this effect, we can easily just copy this class
    // and tweak some of these constants to get a similar effect.
    // == explosions =========================================================
    // these explosions are created every time a projectile impacts on hull or
    // armour, regardless of whether or not we spawn an EMP arc.
    // -- explosion bonus damage ---------------------------------------------
    // this extra damage is always dealt to hull/armour regardless of whether
    // or not we make an EMP arc.
    private static final int EXPLOSION_DAMAGE_MIN = 0;
    private static final int EXPLOSION_DAMAGE_MAX = 25;
    // amount of EMP damage dealt by the explosion
    private static final float EXPLOSION_EMP = 0f;

    // -- explosion graphics -------------------------------------------------
    // color of the explosion
    private static final Color EXPLOSION_COLOR = new Color(125, 100, 255, 155);
    // radius of the explosion
    private static final float EXPLOSION_RADIUS = 25f;
    // how long the explosion lingers for
    private static final float EXPLOSION_DURATION = 0.3f;

    // == EMP arcs ===========================================================
    // EMP arcs are randomly triggered based on the ARC_CHANCE constant
    // chance to create an EMP arc
    // this should range from 0 to 1, where 0 is "never" and 1 is "always"
    private static final float ARC_CHANCE = 0.45f;
    // EMP arc maximum range
    private static final float ARC_RANGE = 100000f;

    // -- multiplier for additional weapon damage dealt by arcs --------------
    // the amount of energy damage dealt by the arc will be the weapon's damage
    // multiplied by this constant.
    private static final float ARC_DAMAGE_MULT = 0.1f;
    // multiplier for weapon EMP damage dealt by arcs
    private static final float ARC_EMP_MULT = 2.5f;

    // -- arc FX -------------------------------------------------------------
    // sound effect to play when an EMP arc happens
    private static final String ARC_SFX = "tachyon_lance_emp_impact";
    // how thick the arc effect should be
    private static final float ARC_WIDTH = 20f;
    // fringe color
    private static final Color ARC_FRINGE_COLOR = new Color(85, 60, 205, 225);
    // core color
    private static final Color ARC_CORE_COLOR = new Color(235, 175, 235, 255);

    // == don't mess with this stuff =========================================
    private static Random rng = new Random();

    // @Inline
    private static float explosionDamage()
    {
        return (float) (rng.nextInt(
                (EXPLOSION_DAMAGE_MAX - EXPLOSION_DAMAGE_MIN) + 1)
                + EXPLOSION_DAMAGE_MIN);
    }

    @Override
    public void onHit(DamagingProjectileAPI projectile,
            CombatEntityAPI target,
            Vector2f point,
            boolean shieldHit,
            CombatEngineAPI engine)
    {

        // check whether we've hit armour/hull
        if (target instanceof ShipAPI && !shieldHit)
        {
            // -- make an EMP arc ----------------------------------------------
            // check whether or not we want to apply critical damage
            if (Math.random() <= ARC_CHANCE)
            {
                // calculate arc EMP and energy damage
                float emp = projectile.getEmpAmount() * ARC_EMP_MULT;
                float dam = projectile.getDamageAmount() * ARC_DAMAGE_MULT;
                // spawn an EMP arc
                engine.spawnEmpArc(projectile.getSource(),
                        point, target, target,
                        DamageType.ENERGY,
                        dam,
                        emp, // emp
                        ARC_RANGE, // max range
                        ARC_SFX,
                        ARC_WIDTH, // thickness
                        ARC_FRINGE_COLOR,
                        ARC_CORE_COLOR
                );
            }
            // -- make an explosion ------------------------------------------
            // apply the extra damage to the target
            engine.applyDamage(target, point, // where to apply damage
                    explosionDamage(), // amount of damage
                    DamageType.HIGH_EXPLOSIVE, // damage type
                    EXPLOSION_EMP, // amount of EMP damage
                    false, // does this bypass shields? (no)
                    false, // does this deal soft flux? (no)
                    projectile.getSource());
            // get the target's velocity to render the crit FX
            Vector2f v_target = new Vector2f(target.getVelocity());

            // do visual effects
            engine.spawnExplosion(point, v_target,
                    EXPLOSION_COLOR, // color of the explosion
                    EXPLOSION_RADIUS,
                    EXPLOSION_DURATION
            );
        }
    }
}
