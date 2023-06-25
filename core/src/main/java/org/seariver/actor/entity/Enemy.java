package org.seariver.actor.entity;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.seariver.BaseActor;
import org.seariver.actor.Solid;

import static com.badlogic.gdx.Gdx.input;

public class Enemy extends BaseActor {

    protected BaseActor belowSensor;
    public Directions direction;
    private Animation stand;
    public int lifes;
    public int delayHit = 0;
    protected float walkAcceleration;
    protected float maxHorizontalSpeed;
    protected float gravity;

    public Enemy(float x, float y, Stage stage) {
        super(x, y, stage);

        String[] animationFileNames = {
                "opossum/opossum-1.png",
                "opossum/opossum-2.png",
                "opossum/opossum-3.png",
                "opossum/opossum-4.png",
                "opossum/opossum-5.png",
                "opossum/opossum-6.png"
        };
        this.stand = loadAnimationFromFiles(animationFileNames, 0.2f, true);

        setSpeed(MathUtils.random(50, 80));
        setBoundaryPolygon(6);

        this.maxHorizontalSpeed = 100;
        this.walkAcceleration = 100;
        this.gravity = 700;
        this.direction = Directions.LEFT;
        this.lifes = 2;

        setBoundaryPolygon(8);

        // set up the below sensor
        this.belowSensor = new BaseActor(0, 0, stage);
        this.belowSensor.loadTexture("white.png");
        this.belowSensor.setSize(this.getWidth() - 8, 8);
        this.belowSensor.setBoundaryRectangle();
        this.belowSensor.setVisible(false);
    }


    protected void setFace() {
        if (this.velocityVec.x > 0){
            setScaleX(Directions.LEFT.getDirection());
        }

        if (this.velocityVec.x < 0) {
            setScaleX(Directions.RIGHT.getDirection());
        }
    }

    protected void accelerate(Directions direction) {

        float acc = 0;

        if (direction == Directions.LEFT) {
            acc = -walkAcceleration;
        } else {
            acc = walkAcceleration;
        }

        accelerationVec.add(acc, 0);
        this.direction = direction;
    }

    protected void move(float deltaTime) {

        this.velocityVec.add(
            this.accelerationVec.x * deltaTime,
            this.accelerationVec.y * deltaTime
        );

        this.velocityVec.x = MathUtils.clamp(velocityVec.x, -this.maxHorizontalSpeed, this.maxHorizontalSpeed);

        this.moveBy(velocityVec.x * deltaTime, velocityVec.y * deltaTime);

        // reset acceleration
        accelerationVec.set(0, 0);
    }

    @Override
    public void act(float deltaTime) {
        super.act(deltaTime);

        if (this.direction == Directions.LEFT) {
            accelerate(Directions.LEFT);
        }

        if (this.direction == Directions.RIGHT) {
            accelerate(Directions.RIGHT);
        }

        // apply gravity
        accelerationVec.add(0, -gravity);

        this.move(deltaTime);

        // move the below sensor below the koala
        belowSensor.setPosition(getX() + 4, getY() - 8);

        setFace();

        boundToWorld();

        delayHit--;
    }

    public boolean belowOverlaps(BaseActor actor) {
        return this.belowSensor.overlaps(actor);
    }

    public boolean isOnSolid() {

        for (BaseActor actor : BaseActor.getList(getStage(), "org.seariver.actor.Solid")) {
            Solid solid = (Solid) actor;
            if (this.belowOverlaps(solid) && solid.isEnabled())
                return true;
        }

        return false;
    }

    public void takeDamage()
    {
        this.lifes--;
    }
    public boolean isDead()
    {
        return this.lifes <= 0;
    }

    public void takeHit()
    {
        this.delayHit = 50;
    }

    public boolean canTakeHit()
    {
        return this.delayHit <= 0;
    }

    public void walkTo(Directions direction) {
        this.direction = direction;
    }

    public boolean isWalkingTo(Directions direction) {
        return this.direction == direction;
    }

    public float getVelocityX() {
        return this.velocityVec.x;
    }
}
