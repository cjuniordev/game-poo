package org.seariver.actor;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.seariver.BaseActor;

public class Timer extends BaseActor {

    public Timer(float x, float y, Stage stage) {
        super(x, y, stage);

        String[] animationFileImages = {
            "items/clock/1.png",
            "items/clock/2.png",
            "items/clock/3.png",
            "items/clock/4.png",
            "items/clock/5.png",
        };
        loadAnimationFromFiles(animationFileImages, 1.0f, true);

        setSize(32, 32);
    }
}
