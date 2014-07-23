
package com.hardy.popit;

import java.util.Vector;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class HomeScreen extends SimpleBaseGameActivity {

    private Camera camera;
    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;

    private ITextureRegion bgRegion;
    private ITextureRegion puckRegion;
    private ITextureRegion bluePaddleRegion;
    private ITextureRegion greenPaddleRegion;

    private Font font;
    private BitmapTextureAtlas bgTexture;
    private BitmapTextureAtlas puckTexture;

    private BitmapTextureAtlas bluePaddleTexture;
    private BitmapTextureAtlas greenPaddleTexture;

    protected PhysicsWorld mPhysicsWorld;

    @Override
    public EngineOptions onCreateEngineOptions() {
        camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
        engineOptions.getRenderOptions().setDithering(true);
        return engineOptions;
    }

    @Override
    protected void onCreateResources() {
        onLoadGraphics();
        onLoadSounds();
        onLoadFonts();
    }

    private void onLoadFonts() {
        /* FontFactory.setAssetBasePath("font/");
         final ITexture fontTexture = new BitmapTextureAtlas(getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

         font = FontFactory.createFromAsset(getFontManager(), fontTexture, getAssets(), "font.ttf", 40, true, Color.BLACK);
         font.load();*/
    }

    private void onLoadSounds() {

    }

    private void onLoadGraphics() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        bgTexture = new BitmapTextureAtlas(getTextureManager(), CAMERA_WIDTH, CAMERA_HEIGHT, TextureOptions.DEFAULT);
        puckTexture = new BitmapTextureAtlas(getTextureManager(), 150, 150, TextureOptions.DEFAULT);
        bluePaddleTexture = new BitmapTextureAtlas(getTextureManager(), 90, 90, TextureOptions.DEFAULT);
        greenPaddleTexture = new BitmapTextureAtlas(getTextureManager(), 90, 90, TextureOptions.DEFAULT);

        bgRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bgTexture, this, "table.jpg", 0, 0);
        puckRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(puckTexture, this, "puck.png", 0, 0);
        bluePaddleRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bluePaddleTexture, this, "blue_paddle.png", 0, 0);
        greenPaddleRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(greenPaddleTexture, this, "green_paddle.png", 0, 0);

        bgTexture.load();
        puckTexture.load();
        bluePaddleTexture.load();
        greenPaddleTexture.load();
    }

    @Override
    protected Scene onCreateScene() {
        final float centerX = CAMERA_WIDTH / 2;
        final float centerY = CAMERA_HEIGHT / 2;

        this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_MOON), false);
        Scene scene = new Scene();

        final Sprite backgroundSprite = new Sprite(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, bgRegion, getVertexBufferObjectManager());
        final Sprite puckSprite = new Sprite(getCenterX(puckRegion), getCenterY(puckRegion), puckRegion, getVertexBufferObjectManager());

        final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
        final Body puckBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, puckSprite, BodyType.DynamicBody, objectFixtureDef);

        Sprite bluePaddleSprite = new Sprite(30, getCenterY(bluePaddleRegion), bluePaddleRegion, getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionMove()) {
                    if (!((pSceneTouchEvent.getX() + bluePaddleRegion.getWidth() / 2) > centerX)) {
                        this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight());
                    }
                }
                return true;
            }

            @Override
            protected void onManagedUpdate(float pSecondsElapsed)
            {
                if (puckSprite.collidesWith(this)) {

                }
            };
        };

        Sprite greenPaddleSprite = new Sprite(CAMERA_WIDTH - 150, getCenterY(greenPaddleRegion), greenPaddleRegion, getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionMove()) {
                    //See that paddle is not going beyond the center.
                    if (!((pSceneTouchEvent.getX() - greenPaddleRegion.getWidth() / 2) < centerX)) {
                        this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight());
                    }
                }
                return true;
            }
        };

        scene.setBackground(new SpriteBackground(backgroundSprite));
        scene.attachChild(puckSprite);

        scene.registerTouchArea(bluePaddleSprite);
        scene.registerTouchArea(greenPaddleSprite);
        scene.setTouchAreaBindingOnActionDownEnabled(true);
        scene.attachChild(bluePaddleSprite);
        scene.attachChild(greenPaddleSprite);

        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(puckSprite, puckBody, true, true) {
            
            @Override
            public void onUpdate(final float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);                
            }
            
        });
        
        return scene;
    }

    private float getCenterX(ITextureRegion iTextureRegion) {
        return CAMERA_WIDTH / 2 - iTextureRegion.getWidth() / 2;
    }

    private float getCenterY(ITextureRegion iTextureRegion) {
        return CAMERA_HEIGHT / 2 - iTextureRegion.getWidth() / 2;
    }
}
