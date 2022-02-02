package com.paragon.client.features.module.impl.render;

import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.ModeSetting;

public class ESP extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "How to render the entities", "Outline", new String[]{"Outline", "Glow", "Box"});

    public ESP() {
        super("ESP", "Highlights entities in the world", Category.RENDER);
    }

}
