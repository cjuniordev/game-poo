package org.seariver;

import org.seariver.screen.MenuScreen;

public class PlatformGamePoo extends BaseGame {

    public void create() {
        super.create();
        setActiveScreen(new MenuScreen());
//        setActiveScreen(new LevelScreen());
    }
}
