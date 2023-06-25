package org.seariver.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.seariver.BaseGame;
import org.seariver.BaseScreen;
import org.seariver.PlatformGamePoo;
import org.seariver.TilemapActor;

public class MenuScreen extends BaseScreen {

    public void initialize() {

        TilemapActor tma = new TilemapActor("map-menu.tmx", mainStage);

        Table titleTable = new Table();

        Label title = new Label("Game POO", BaseGame.labelStyle);
        title.setColor(Color.GREEN);

        Label pressEnter = new Label("Pressione ENTER para começar.", BaseGame.labelStyle);
        pressEnter.setColor(Color.GREEN);
        pressEnter.setFontScale(0.5f);

        this.uiTable.pad(20);
        this.uiTable.row();
        this.uiTable.add(title).colspan(3);
        this.uiTable.row();
        this.uiTable.add(pressEnter).colspan(3);
    }

    public void update(float deltaTime) {
        //
    }

    public boolean keyDown(int keyCode) {

         if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
             PlatformGamePoo.setActiveScreen(new LevelScreen());
         }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        return false;
    }
}
