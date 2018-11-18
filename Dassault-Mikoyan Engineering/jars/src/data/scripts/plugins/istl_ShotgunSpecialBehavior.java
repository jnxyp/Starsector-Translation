package data.scripts.plugins;

import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class istl_ShotgunSpecialBehavior extends BaseEveryFrameCombatPlugin {

    private static final Set<String> SHOTGUNPROJ_IDS1 = new HashSet<>(1);
    //private static final Set<String> SHOTGUNPROJ_IDS2 = new HashSet<>(1);
    //private static final Set<String> SHOTGUNPROJ_IDS3 = new HashSet<>(1);
    //private static final Set<String> SHOTGUNPROJ_IDS4 = new HashSet<>(1);



    static {
        //add Projectile IDs here.
        SHOTGUNPROJ_IDS1.add("istl_linearflakshot");
        //SHOTGUNPROJ_IDS2.add("projidgoeshere");
        //SHOTGUNPROJ_IDS3.add("projidgoeshere");
        //SHOTGUNPROJ_IDS4.add("projidgoeshere");
    }

    private CombatEngineAPI engine;

    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        if (engine == null) {
            return;
        }
        if (engine.isPaused()) {
            return;
        }

        List<DamagingProjectileAPI> projectiles = engine.getProjectiles();
        int size = projectiles.size();
        for (int i = 0; i < size; i++) {
            DamagingProjectileAPI proj = projectiles.get(i);
            String spec = proj.getProjectileSpecId();

            //Set shotgun properties here.  
            if (SHOTGUNPROJ_IDS1.contains(spec)) {
                Vector2f loc = proj.getLocation();
                Vector2f vel = proj.getVelocity();
                int shotCount = (7);
                for (int j = 0; j < shotCount; j++) {
                    Vector2f randomVel = MathUtils.getRandomPointOnCircumference(null, MathUtils.getRandomNumberInRange(
                                                                                 50f, 150f));
                    randomVel.x += vel.x;
                    randomVel.y += vel.y;
                    //spec + "_clone" means this will call the weapon (not projectile! you need a separate weapon) with the id "($projectilename)_clone".
                    engine.spawnProjectile(proj.getSource(), proj.getWeapon(), spec + "_clone", loc, proj.getFacing(),
                                           randomVel);
                }
                engine.removeEntity(proj);
            }
            
//            //Duplicate this commented-out section for additional weapons.  
//            if (SHOTGUNPROJ_IDS2.contains(spec)) {
//                Vector2f loc = proj.getLocation();
//                Vector2f vel = proj.getVelocity();
//                int shotCount = (20);
//                for (int j = 0; j < shotCount; j++) {
//                    Vector2f randomVel = MathUtils.getRandomPointOnCircumference(null, MathUtils.getRandomNumberInRange(
//                                                                                 60f, 180f));
//                    randomVel.x += vel.x;
//                    randomVel.y += vel.y;
//                    //spec + "_clone" means this will call the weapon (not projectile! you need a separate weapon) with the id "($projectilename)_clone".
//                    engine.spawnProjectile(proj.getSource(), proj.getWeapon(), spec + "_clone", loc, proj.getFacing(),
//                                           randomVel);
//                }
//                engine.removeEntity(proj);
//            }
        }
    }

    @Override
    public void init(CombatEngineAPI engine) {
        this.engine = engine;
    }
}
