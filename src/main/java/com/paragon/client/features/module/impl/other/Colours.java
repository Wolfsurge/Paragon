package com.paragon.client.features.module.impl.other;

import com.paragon.client.Paragon;
import com.paragon.client.features.module.Category;
import com.paragon.client.features.module.Module;
import com.paragon.client.features.module.settings.impl.ColourSetting;
import java.awt.*;

public class Colours extends Module {

    public static ColourSetting mainColour = new ColourSetting("Main Colour", "The main colour of the client", new Color(185, 19, 211));

    public Colours() {
        super("Colours", "Customise the client's main colour", Category.OTHER);
        this.addSettings(mainColour);
        Paragon.EVENT_BUS.subscribe(this);
        if(!this.isEnabled())
            toggle();
    }

    @Override
    public void onDisable() {
        toggle();
    }
}
