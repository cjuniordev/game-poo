package org.seariver;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import org.seariver.screen.MenuScreen;

public class PlatformGamePoo extends BaseGame {

    public void create() {
        super.create();
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        setActiveScreen(new MenuScreen());
//        setActiveScreen(new LevelScreen());
    }
}
