package org.seariver.actor;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.seariver.BaseActor;

public class Key extends BaseActor {

    public Key(float x, float y, Stage stage) {
        super(x, y, stage);

        loadAnimationFromSheet("items/keys.png", 1, 24, 0.1f, true);
    }
}
