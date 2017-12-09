package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.graphics.ParticleEmitterBox2D;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by julienvillegas on 07/12/2017.
 */

public class Ball extends Image {
    private Body body;
    private World world;
    private boolean delete;
    static private final Texture texture = new Texture("bubble.png");
    ParticleEffect explosionEffect;
    private boolean exploding;

    public Ball(World aWorld, float pos_x, float pos_y) {
        super(texture);
        this.setSize(0.5f,0.5f);
        this.setPosition(pos_x, pos_y);

        world = aWorld;
        BodyDef bd = new BodyDef();
        bd.position.set(this.getX(),this.getY());
        bd.type = BodyDef.BodyType.DynamicBody;


        body = world.createBody(bd);
        CircleShape circle = new CircleShape();
        circle.setRadius(this.getWidth()/2);
        exploding = false;


        // 2. Create a FixtureDef, as usual.
        FixtureDef fd = new FixtureDef();
        fd.density = 10;
        fd.friction = 0.5f;
        fd.restitution = 0.6f;
        fd.shape = circle;

        Fixture fixture = body.createFixture(fd);
        body.setUserData(this);

        this.setOrigin(this.getWidth()/2,this.getHeight()/2);
        circle.dispose();

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(exploding) {
            explosionEffect.draw(batch);
        }

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(exploding) {
            explosionEffect.update(delta);

            if (explosionEffect.isComplete()) {
                explosionEffect.dispose();
                world.destroyBody(body);
                this.delete = true;
            }
        }
        this.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        this.setPosition(body.getPosition().x-this.getWidth()/2 , body.getPosition().y -this.getHeight()/2);
        if(delete) {
            this.remove();
        }
    }

    public void explode(ParticleEffect effect){

        explosionEffect = effect;
        explosionEffect.getEmitters().add(new ParticleEmitterBox2D(world,explosionEffect.getEmitters().first()));
        explosionEffect.getEmitters().removeIndex(0);
        explosionEffect.setPosition(this.getX()+this.getWidth()/2, this.getY());
        explosionEffect.scaleEffect(0.01f);

        explosionEffect.start();


        exploding = true;

    }

}
