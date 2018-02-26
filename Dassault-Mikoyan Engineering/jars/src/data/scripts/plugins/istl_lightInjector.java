package data.scripts.plugins;

import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipEngineControllerAPI.ShipEngineAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.dark.shaders.light.LightShader;
import org.dark.shaders.light.StandardLight;
import org.lwjgl.util.vector.Vector2f;

public class istl_lightInjector extends BaseEveryFrameCombatPlugin
{
    private static final String DATA_KEY = "DME_LightInjector";
    private static final float SYSTEM_IN_TIME = 1.5f;
    private static final float SYSTEM_OUT_TIME = 0.75f;
    private static final Vector2f ZERO = new Vector2f();
    private CombatEngineAPI engine;

    @Override
    public void advance(float amount, List<InputEventAPI> events)
    {
        if (engine == null)
        {
            return;
        }

        if (engine.isPaused())
        {
            return;
        }

        final LocalData localData = (LocalData) engine.getCustomData().get(DATA_KEY);
        final Map<ShipAPI, StandardLight> lights = localData.lights;

        List<ShipAPI> ships = engine.getShips();
        int shipsSize = ships.size();
        for (int i = 0; i < shipsSize; i++)
        {
            ShipAPI ship = ships.get(i);
            if (ship.isHulk())
            {
                continue;
            }

            ShipSystemAPI system = ship.getSystem();
            if (system != null)
            {
                String id = system.getId();
                switch (id)
                {
                    case "istl_pulsejets":
                        if (system.isActive()) {
                            Vector2f location = null;
                            if (ship.getEngineController() == null) {
                                break;
                            }
                            List<ShipEngineAPI> engines = ship.getEngineController().getShipEngines();
                            int num = 0;
                            int enginesSize = engines.size();
                            for (int j = 0; j < enginesSize; j++) {
                                ShipEngineAPI eng = engines.get(j);
                                if (eng.isActive() && !eng.isDisabled()) {
                                    num++;
                                    if (location == null) {
                                        location = new Vector2f(eng.getLocation());
                                    } else {
                                        Vector2f.add(location, eng.getLocation(), location);
                                    }
                                }
                            }
                            if (location == null) {
                                break;
                            }

                            location.scale(1f / num);

                            if (lights.containsKey(ship)) {
                                StandardLight light = lights.get(ship);

                                light.setLocation(location);

                                if ((system.isActive() && !system.isOn()) || system.isChargedown()) {
                                    if (!light.isFadingOut()) {
                                        light.fadeOut(0.5f);
                                    }
                                }
                            } else {
                                StandardLight light = new StandardLight(location, ZERO, ZERO, null);
                                float intensity = (float) Math.sqrt(ship.getCollisionRadius()) / 25f;
                                float size = intensity * 400f;

                                light.setIntensity(intensity);
                                light.setSize(size);
                                Color color = new Color(105, 145, 255, 255);
                                light.setColor(color);
                                light.fadeIn(0.1f);

                                lights.put(ship, light);
                                LightShader.addLight(light);
                            }
                        }
                        break;
                    case "istl_pulsejets_ftr":
                        if (system.isActive()) {
                            Vector2f location = null;
                            if (ship.getEngineController() == null) {
                                break;
                            }
                            List<ShipEngineAPI> engines = ship.getEngineController().getShipEngines();
                            int num = 0;
                            int enginesSize = engines.size();
                            for (int j = 0; j < enginesSize; j++) {
                                ShipEngineAPI eng = engines.get(j);
                                if (eng.isActive() && !eng.isDisabled()) {
                                    num++;
                                    if (location == null) {
                                        location = new Vector2f(eng.getLocation());
                                    } else {
                                        Vector2f.add(location, eng.getLocation(), location);
                                    }
                                }
                            }
                            if (location == null) {
                                break;
                            }

                            location.scale(1f / num);

                            if (lights.containsKey(ship)) {
                                StandardLight light = lights.get(ship);

                                light.setLocation(location);

                                if ((system.isActive() && !system.isOn()) || system.isChargedown()) {
                                    if (!light.isFadingOut()) {
                                        light.fadeOut(0.5f);
                                    }
                                }
                            } else {
                                StandardLight light = new StandardLight(location, ZERO, ZERO, null);
                                float intensity = (float) Math.sqrt(ship.getCollisionRadius()) / 25f;
                                float size = intensity * 400f;

                                light.setIntensity(intensity);
                                light.setSize(size);
                                Color color = new Color(100, 100, 255, 255);
                                light.setColor(color);
                                light.fadeIn(0.1f);

                                lights.put(ship, light);
                                LightShader.addLight(light);
                            }
                        }
                        break;
                    case "istl_jetinjector":
                        if (system.isActive()) {
                            Vector2f location = null;
                            if (ship.getEngineController() == null) {
                                break;
                            }
                            List<ShipEngineAPI> engines = ship.getEngineController().getShipEngines();
                            int num = 0;
                            int enginesSize = engines.size();
                            for (int j = 0; j < enginesSize; j++) {
                                ShipEngineAPI eng = engines.get(j);
                                if (eng.isActive() && !eng.isDisabled()) {
                                    num++;
                                    if (location == null) {
                                        location = new Vector2f(eng.getLocation());
                                    } else {
                                        Vector2f.add(location, eng.getLocation(), location);
                                    }
                                }
                            }
                            if (location == null) {
                                break;
                            }

                            location.scale(1f / num);

                            if (lights.containsKey(ship)) {
                                StandardLight light = lights.get(ship);

                                light.setLocation(location);

                                if ((system.isActive() && !system.isOn()) || system.isChargedown()) {
                                    if (!light.isFadingOut()) {
                                        light.fadeOut(1f);
                                    }
                                }
                            } else {
                                StandardLight light = new StandardLight(location, ZERO, ZERO, null);
                                float intensity = (float) Math.sqrt(ship.getCollisionRadius()) / 25f;
                                float size = intensity * 400f;

                                light.setIntensity(intensity);
                                light.setSize(size);
                                Color color = new Color(100, 100, 255, 255);
                                light.setColor(color);
                                light.fadeIn(2f);

                                lights.put(ship, light);
                                LightShader.addLight(light);
                            }
                        }
                        break;
                    case "istl_dockjets":
                        if (system.isActive()) {
                            Vector2f location = null;
                            if (ship.getEngineController() == null) {
                                break;
                            }
                            List<ShipEngineAPI> engines = ship.getEngineController().getShipEngines();
                            int num = 0;
                            int enginesSize = engines.size();
                            for (int j = 0; j < enginesSize; j++) {
                                ShipEngineAPI eng = engines.get(j);
                                if (eng.isActive() && !eng.isDisabled()) {
                                    num++;
                                    if (location == null) {
                                        location = new Vector2f(eng.getLocation());
                                    } else {
                                        Vector2f.add(location, eng.getLocation(), location);
                                    }
                                }
                            }
                            if (location == null) {
                                break;
                            }

                            location.scale(1f / num);

                            if (lights.containsKey(ship)) {
                                StandardLight light = lights.get(ship);

                                light.setLocation(location);

                                if ((system.isActive() && !system.isOn()) || system.isChargedown()) {
                                    if (!light.isFadingOut()) {
                                        light.fadeOut(1f);
                                    }
                                }
                            } else {
                                StandardLight light = new StandardLight(location, ZERO, ZERO, null);
                                float intensity = (float) Math.sqrt(ship.getCollisionRadius()) / 40f;
                                float size = intensity * 600f;

                                light.setIntensity(intensity);
                                light.setSize(size);
                                Color color = null;
                                if (!ship.getEngineController().getShipEngines().isEmpty()) {
                                    color = ship.getEngineController().getShipEngines().get(0).getEngineColor();
                                }
                                if (color != null) {
                                    light.setColor(color);
                                }
                                light.fadeIn(0.25f);

                                lights.put(ship, light);
                                LightShader.addLight(light);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        Iterator<Map.Entry<ShipAPI, StandardLight>> iter = lights.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry<ShipAPI, StandardLight> entry = iter.next();
            ShipAPI ship = entry.getKey();

            if ((ship.getSystem() != null && !ship.getSystem().isActive()) || !ship.isAlive())
            {
                StandardLight light = entry.getValue();

                light.unattach();
                light.fadeOut(0);
                iter.remove();
            }

        }
    }

    @Override
    public void init(CombatEngineAPI engine)
    {
        this.engine = engine;
        engine.getCustomData().put(DATA_KEY, new LocalData());
    }

    private static final class LocalData
    {
        final Map<ShipAPI, StandardLight> lights = new LinkedHashMap<>(100);
    }
}
