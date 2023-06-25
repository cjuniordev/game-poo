package org.seariver.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.seariver.BaseGame;
import org.seariver.BaseActor;
import org.seariver.BaseScreen;
import org.seariver.TilemapActor;
import org.seariver.actor.*;
import org.seariver.actor.entity.Directions;
import org.seariver.actor.entity.Enemy;
import org.seariver.actor.entity.Player;

import java.util.ArrayList;

import static com.badlogic.gdx.Input.Keys;

public class LevelScreen extends BaseScreen {

    Player jack;
    Sword sword;

    float facingAngle = 0;

    boolean gameOver;
    float time;

    ArrayList<Color> keyList;
    Label timeLabel;
    Label messageLabel;
    Table keyTable;
    Label lifeLabel;

    TilemapActor tma;

    protected void loadSolids() {
        for (MapObject obj : this.tma.getRectangleList("Solid")) {

            MapProperties props = obj.getProperties();

            new Solid((float) props.get("x"), (float) props.get("y"), (float) props.get("width"), (float) props.get("height"), this.mainStage);
        }
    }

    protected void loadEnemySolids() {
        for (MapObject obj : this.tma.getRectangleList("EnemySolid")) {
            MapProperties props = obj.getProperties();
            new EnemySolid((float) props.get("x"), (float) props.get("y"), (float) props.get("width"), (float) props.get("height"), mainStage);
        }
    }

    protected void loadSpringboards() {
        for (MapObject obj : tma.getTileList("Springboard")) {
            MapProperties props = obj.getProperties();
            new Springboard((float) props.get("x"), (float) props.get("y"), mainStage);
        }
    }

    protected void loadTimers() {
        for (MapObject obj : tma.getTileList("Timer")) {
            MapProperties props = obj.getProperties();
            new Timer((float) props.get("x"), (float) props.get("y"), mainStage);
        }
    }

    protected void loadFlags() {
        for (MapObject obj : tma.getTileList("Flag")) {
            MapProperties props = obj.getProperties();
            new Flag((float) props.get("x"), (float) props.get("y"), mainStage);
        }
    }

    protected void loadKeys() {
        for (MapObject obj : tma.getTileList("Key")) {
            MapProperties props = obj.getProperties();
            Key key = new Key((float) props.get("x"), (float) props.get("y"), mainStage);

            String color = (String) props.get("color");
            if (color.equals("red")) {
                key.setColor(Color.RED);
            } else if (color.equals("green")) {
                key.setColor(Color.GREEN);
            } else {
                key.setColor(Color.WHITE);
            }
        }
    }

    protected void loadLocks() {
        for (MapObject obj : tma.getTileList("Lock")) {
            MapProperties props = obj.getProperties();
            Lock lock = new Lock((float) props.get("x"), (float) props.get("y"), mainStage);

            String color = (String) props.get("color");
            if (color.equals("red")) {
                lock.setColor(Color.RED);
            } else if (color.equals("green")) {
                lock.setColor(Color.GREEN);
            } else {
                lock.setColor(Color.WHITE);
            }
        }
    }

    protected void loadEnemies() {
        for (MapObject obj : tma.getRectangleList("Enemy")) {
            MapProperties props = obj.getProperties();
            new Enemy((float) props.get("x"), (float) props.get("y"), mainStage);
        }
    }

    protected void gameOver(String text) {
        this.messageLabel.setText(text);
        this.messageLabel.setColor(Color.RED);
        this.messageLabel.setVisible(true);
        this.jack.remove();
        this.gameOver = true;
    }


    public void initialize() {

        this.keyList = new ArrayList<>();

        this.tma = new TilemapActor("map.tmx", mainStage);

        this.loadSolids();
        this.loadEnemySolids();
        this.loadSpringboards();
        this.loadTimers();
        this.loadFlags();
        this.loadKeys();
        this.loadLocks();
        this.loadEnemies();

        // Create Player
        MapObject startPoint = tma.getRectangleList("start").get(0);
        MapProperties startProps = startPoint.getProperties();
        jack = new Player((float) startProps.get("x"), (float) startProps.get("y"), mainStage);
        jack.toFront();

        // Create Sword
        sword = new Sword(0, 0, mainStage);
        sword.setVisible(false);

        // Setup
        this.gameOver = false;
        this.time = 60;
        this.timeLabel = new Label("Time: " + (int) time, BaseGame.labelStyle);
        this.timeLabel.setColor(Color.LIGHT_GRAY);
        this.lifeLabel = new Label("Vidas: " + jack.lifes, BaseGame.labelStyle);
        this.lifeLabel.setColor(Color.RED);
        this.messageLabel = new Label("Message", BaseGame.labelStyle);
        this.messageLabel.setVisible(false);
        this.keyTable = new Table();

        this.uiTable.pad(20);
        this.uiTable.add(lifeLabel);
        this.uiTable.add(keyTable).expandX();
        this.uiTable.add(timeLabel);
        this.uiTable.row();
        this.uiTable.add(messageLabel).colspan(3).expandY();
    }

    protected void updateTimer() {
        for (BaseActor timer : BaseActor.getList(mainStage, "org.seariver.actor.Timer")) {
            if (jack.overlaps(timer)) {
                time += 20;
                timer.remove();
            }
        }
    }

    protected void lockCollision(Solid solid) {
        if (solid instanceof Lock && jack.overlaps(solid)) {
            Color lockColor = solid.getColor();

            if (keyList.contains(lockColor)) {
                solid.setEnabled(false);
                solid.addAction(Actions.fadeOut(0.5f));
                solid.addAction(Actions.after(Actions.removeActor()));
            }
        }
    }

    protected void solidCollision(Solid solid) {
        boolean isEnemySolid = solid instanceof EnemySolid;

        if (jack.overlaps(solid) && solid.isEnabled() && !isEnemySolid) {
            Vector2 offset = jack.preventOverlap(solid);
            if (offset != null) {
                // collided in X direction
                if (Math.abs(offset.x) > Math.abs(offset.y)) {
                    jack.velocityVec.x = 0;
                    // collided in Y direction
                } else {
                    jack.velocityVec.y = 0;
                }
            }
        }
    }

    protected void enemyCollisions(Solid solid) {
        for (BaseActor actor : BaseActor.getList(mainStage, "org.seariver.actor.entity.Enemy")) {
            Enemy enemy = (Enemy) actor;

            if (enemy.overlaps(solid) && solid.isEnabled()) {
                Vector2 offset = enemy.preventOverlap(solid);
                if (offset != null) {
                    if (Math.abs(offset.x) > Math.abs(offset.y)) {
                        float vx = enemy.getVelocityX();

                        if (vx > 0) {
                            enemy.velocityVec.x = 0;
                            enemy.walkTo(Directions.LEFT);
                        } else {
                            enemy.velocityVec.x = 0;
                            enemy.walkTo(Directions.RIGHT);
                        }
                    } else {
                        enemy.velocityVec.y = 0;
                    }
                }
            }
        }
    }

    protected void updateCollisions() {
        for (BaseActor actor : BaseActor.getList(mainStage, "org.seariver.actor.Solid")) {
            Solid solid = (Solid) actor;

            this.lockCollision(solid);
            this.solidCollision(solid);
            this.enemyCollisions(solid);
//            this.updateEnemies(solid);
        }
    }

    protected void updateSpringboard() {
        for (BaseActor springboard : BaseActor.getList(mainStage, "org.seariver.actor.Springboard")) {
            if (jack.belowOverlaps(springboard) && jack.isFalling()) {
                jack.spring();
            }
        }
    }

    protected void updateKey() {
        for (BaseActor key : BaseActor.getList(mainStage, "org.seariver.actor.Key")) {
            if (jack.overlaps(key)) {
                Color keyColor = key.getColor();
                key.remove();
                BaseActor keyIcon = new BaseActor(0, 0, uiStage);
                keyIcon.loadTexture("assets/key-icon.png");
                keyIcon.setColor(keyColor);
                keyTable.add(keyIcon);
                keyList.add(keyColor);
            }
        }
    }

    protected void updateFlag() {
        for (BaseActor flag : BaseActor.getList(mainStage, "org.seariver.actor.Flag")) {
            if (jack.overlaps(flag)) {
                messageLabel.setText("Você ganhou!");
                messageLabel.setColor(Color.LIME);
                messageLabel.setVisible(true);
                jack.remove();
                gameOver = true;
            }
        }
    }

    protected void updateEnemies(Solid solid) {
        for (BaseActor actor : BaseActor.getList(mainStage, "org.seariver.actor.Enemy")) {
            Enemy enemy = (Enemy) actor;

            if (jack.overlaps(enemy) && enemy.canTakeHit()) {
                jack.takeHit();
            }

            // Código repetido
//            if (enemy.overlaps(solid) && solid.isEnabled()) {
//                Vector2 offset = enemy.preventOverlap(solid);
//                if (offset != null) {
//                    if (Math.abs(offset.x) > Math.abs(offset.y)) {
//                        float vx = enemy.getVelocityX();
//
//                        if (vx > 0) {
//                            enemy.velocityVec.x = 0;
//                            enemy.walkTo(Directions.LEFT);
//                        } else {
//                            enemy.velocityVec.x = 0;
//                            enemy.walkTo(Directions.RIGHT);
//                        }
//                    } else {
//                        enemy.velocityVec.y = 0;
//                    }
//                }
//            }
        }
    }

    protected void updateCombat() {

    }


    public void update(float deltaTime) {

        if (jack.isDead()) {
            this.gameOver("Você morreu!");
        }

//        if (enemy.isDead()) {
//            enemy.remove();
//        }

        if (gameOver) return;

        time -= deltaTime;
        timeLabel.setText("Time: " + (int) time);

        this.updateTimer();

        if (time <= 0) {
            this.gameOver("O seu tempo acabou!");
        }

        this.updateCollisions();
        this.updateSpringboard();
        this.updateKey();
        this.updateFlag();

//        if (sword.overlaps(enemy) && sword.isVisible() && enemy.canTakeHit()) {
//
//            if (jack.isWalkingTo(Directions.LEFT))
//            {
//                enemy.walkTo(Directions.LEFT);
//            } else {
//                enemy.walkTo(Directions.RIGHT);
//            }
//
//            enemy.takeDamage();
//            enemy.takeHit();
//        }

        // Enemy collision
//        if (jack.overlaps(enemy)) {
//            Vector2 offset = jack.preventOverlap(enemy);
//            if (offset != null && jack.canTakeHit()) {
//
//                float vx = enemy.getVelocityX();
//
//                if (vx > 0) {
//                    enemy.velocityVec.x = 0;
//                    enemy.walkTo(Directions.LEFT);
//                } else {
//                    enemy.velocityVec.x = 0;
//                    enemy.walkTo(Directions.RIGHT);
//                }
//
//                jack.takeDamage();
//                lifeLabel.setText("Vidas: " + jack.lifes);
//                jack.takeHit();
//            }
//        }


    }

    public void swingSword()
    {
        // visibility determines if sword is currently swinging
        if (sword.isVisible()) return;

        this.jack.setSpeed(0);

        // RIGHT - 0
        // LEFT - 180

        Vector2 offset = new Vector2();

        if (facingAngle == 0) {
            offset.set(0.50f, 0.20f);
        } else {
            offset.set(0.40f, 0.20f);
        }

        this.sword.setPosition(jack.getX(), jack.getY());
        this.sword.moveBy(offset.x * jack.getWidth(), offset.y * jack.getHeight());
        float swordArc = 90;
        this.sword.setRotation(facingAngle - swordArc / 2);
        this.sword.setOriginX(0);
        this.sword.setVisible(true);
        this.sword.addAction(Actions.rotateBy(swordArc, 0.25f));
        this.sword.addAction(Actions.after(Actions.visible(false)));
        this.sword.toFront();
    }

    public boolean keyDown(int keyCode) {

        if (gameOver) return false;

        if (keyCode == Keys.LEFT) {
            facingAngle = 180;
        }

        if (keyCode == Keys.RIGHT) {
            facingAngle = 0;
        }

        if (keyCode == Keys.S) {
            swingSword();
        }

        if (keyCode == Keys.SPACE && this.jack.isOnSolid()) {
            jack.jump();
        }

        return false;
    }
}
