package com.paragon.client.managers;

import com.paragon.Paragon;
import com.paragon.client.systems.module.Module;
import com.paragon.client.systems.module.Category;
import com.paragon.client.systems.module.hud.HUDModule;
import com.paragon.client.systems.module.hud.impl.*;
import com.paragon.client.systems.module.impl.client.*;
import com.paragon.client.systems.module.impl.combat.*;
import com.paragon.client.systems.module.impl.misc.*;
import com.paragon.client.systems.module.impl.movement.*;
import com.paragon.client.systems.module.impl.render.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Wolfsurge
 */
public class ModuleManager {

    private final List<Module> modules;

    public ModuleManager() {
        MinecraftForge.EVENT_BUS.register(this);

        Paragon.INSTANCE.getLogger().info("Initialising Module Manager");

        modules = Arrays.asList(
                // Combat
                new Aura(),
                new AutoCrystal(),
                new BowRelease(),
                new Criticals(),
                new Offhand(),
                new Replenish(),
                new Surround(),

                // Movement
                new ElytraFlight(),
                new Flight(),
                new NoFall(),
                new NoSlow(),
                new ReverseStep(),
                new Scaffold(),
                new Sprint(),
                new Step(),
                new Velocity(),

                // Render
                new AspectRatio(),
                new BlockHighlight(),
                new Breadcrumbs(),
                new BreakESP(),
                new Chams(),
                new ChinaHat(),
                new ESP(),
                new Fullbright(),
                new HitColour(),
                new HoleESP(),
                new Nametags(),
                new NoRender(),
                new NoSwing(),
                new Shader(),
                new ShulkerViewer(),
                new SoundHighlight(),
                new StorageESP(),
                new Tracers(),
                new Trajectories(),
                new ViewClip(),
                new ViewModel(),
                new Xray(),

                // Misc
                new Announcer(),
                new AutoEZ(),
                new AutoLog(),
                new AutoWalk(),
                new Blink(),
                new BuildHeight(),
                new ChatModifications(),
                new CustomWorld(),
                new FakePlayer(),
                new FastUse(),
                new LiquidInteract(),
                new MiddleClick(),
                new NoGlobalSounds(),
                new NoRotate(),
                new Notifier(),
                new NoTrace(),
                new OnDeath(),
                new RotationLock(),
                new TeleTofu(),
                new TimerModule(),
                new XCarry(),

                // Client
                new ClientFont(),
                new Colours(),
                new ClickGUI(),
                new DiscordRPC(),

                // HUD
                new Armour(),
                new ArrayListHUD(),
                new CombatInfo(),
                new Coordinates(),
                new Direction(),
                new FPS(),
                new HUD(),
                new HUDEditor(),
                new Inventory(),
                new Notifications(),
                new Ping(),
                new Speed(),
                new Totems(),
                new Watermark()
        );
    }

    /**
     * Gets a list of modules
     *
     * @return The modules
     */
    public List<Module> getModules() {
        return modules;
    }

    /**
     * Gets the modules in a category
     *
     * @param moduleCategory The module category to get modules in
     * @return The modules in the given category
     */
    public List<Module> getModulesInCategory(Category moduleCategory) {
        List<Module> modulesInCategory = new ArrayList<>();

        getModules().forEach(module -> {
            if (module.getCategory() == moduleCategory) {
                modulesInCategory.add(module);
            }
        });

        return modulesInCategory;
    }

    /**
     * Gets all the HUD modules
     *
     * @return The HUD modules
     */
    public List<HUDModule> getHUDModules() {
        List<HUDModule> hudModules = new ArrayList<>();

        getModules().forEach(module -> {
            if (module instanceof HUDModule) {
                hudModules.add((HUDModule) module);
            }
        });

        return hudModules;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        modules.forEach(module -> {
            if (module.getBind().getValue().isPressed() && Minecraft.getMinecraft().currentScreen == null) {
                module.toggle();
            }

            if (module.isEnabled()) {
                module.onTick();
            }
        });
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            modules.forEach(module -> {
                if (module.isEnabled()) {
                    module.onRender2D();
                }
            });
        }
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event) {
        modules.forEach(module -> {
            if (module.isEnabled()) {
                module.onRender3D();
            }
        });
    }

    public Set<Class<?>> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines().filter(line -> line.endsWith(".class")).map(line -> getClass(line, packageName)).collect(Collectors.toSet());
    }

    private Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

}
