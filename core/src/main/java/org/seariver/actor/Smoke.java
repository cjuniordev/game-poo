package org.seariver.actor;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.seariver.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Smoke extends BaseActor {

    public Smoke(float x, float y, Stage stage) {
        super(x, y, stage);

        String[] animationFileImages = {
            "enemy-death/enemy-death-1.png",
            "enemy-death/enemy-death-2.png",
            "enemy-death/enemy-death-3.png",
            "enemy-death/enemy-death-4.png",
            "enemy-death/enemy-death-5.png",
            "enemy-death/enemy-death-6.png"
        };

        loadAnimationFromFiles(animationFileImages, 0.1f, false);

        addAction(Actions.fadeOut(0.5f));
        addAction(Actions.after(Actions.removeActor()));
    }

}
