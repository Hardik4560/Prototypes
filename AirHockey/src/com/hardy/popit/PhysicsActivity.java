/*
package com.hardy.popit;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;

public class PhysicsActivity extends SimpleBaseGameActivity {

    // ===========================================================
    // Constants
    // ===========================================================
    protected static final int CAMERA_WIDTH = 720;
    protected static final int CAMERA_HEIGHT = 480;

    private PhysicsWorld mPhysicsWorld;
    private Scene mScene;

    private ITextureRegion bgRegion;

    // ===========================================================
    // Constructors
    // ===========================================================

    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
    }

    @Override
    protected void onCreateResources() {
        onLoadGraphics();
        onLoadSounds();
        onLoadFonts();
    }

    private void onLoadFonts() {

    }

    private void onLoadSounds() {

    }

    private void onLoadGraphics() {

    }

    @Override
    protected Scene onCreateScene() {
        mScene = new Scene();

        Sprite backgroundSprite = new Sprite(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, bgRegion, getVertexBufferObjectManager());
        mScene.setBackground(new SpriteBackground(backgroundSprite));
        return mScene;
    }

    private void initPhysics() {
        mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
        yourScene.registerUpdateHandler(physicsWorld);
    }
}
*/
