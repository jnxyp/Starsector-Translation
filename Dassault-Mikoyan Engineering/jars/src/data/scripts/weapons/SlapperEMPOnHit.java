package data.scripts.weapons;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;

public class SlapperEMPOnHit implements OnHitEffectPlugin
{

    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
            Vector2f point, boolean shieldHit, CombatEngineAPI engine)
    {
        if ((float) Math.random() > 0.6f && !shieldHit && target instanceof ShipAPI)
        {

            float emp = projectile.getEmpAmount() * 2.0f;
            float dam = projectile.getDamageAmount() * 0.25f;

            engine.spawnEmpArc(projectile.getSource(), point, target, target,
                    DamageType.ENERGY,
                    dam,
                    emp, // emp 
                    100000f, // max range 
                    "tachyon_lance_emp_impact",
                    20f, // thickness
                    new Color(165, 165, 150, 225),
                    new Color(255, 255, 255, 255)
            );

            //engine.spawnProjectile(null, null, "plasma", point, 0, new Vector2f(0, 0));
        }
    }
}
