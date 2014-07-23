
package com.hardy.airhockey.managers;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.hardy.airhockey.activities.GameActivity;

public class ResourceManager {

    public Engine engine;
    public GameActivity activity;
    public Camera camera;
    public VertexBufferObjectManager vbom;

    //=========================================
    // VARIABLES
    //=========================================
    private final static ResourceManager INSTANCE = new ResourceManager();

    //=========================================
    // TEXTURES / REGIONS
    //=========================================

    public ITextureRegion splashRegion;
    private BitmapTextureAtlas splashTextureAlas;

    //=========================================
    // CONSTRUCTOR
    //=========================================
    private ResourceManager() {}

    //=========================================
    // CLASS LOGIC
    //=========================================
    public void loadMenuResources() {
        loadMenuGraphics();
    }

    public void loadGameResources() {
        loadGameGraphics();
    }

    private void loadMenuGraphics() {

    }

    private void loadGameGraphics() {

    }

    public void loadSplashScreen() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        splashTextureAlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 154, TextureOptions.BILINEAR);

        splashRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAlas, activity, "splash.png", 0, 0);
        splashTextureAlas.load();
    }

    public void unLoadSplashScreen() {
        splashTextureAlas.unload();
        splashRegion = null;
    }

    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     */
    public static void prepareManager(Engine engine, GameActivity activity, Camera camera, VertexBufferObjectManager vbom)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }

    //=========================================
    // GETTER/SETTER
    //=========================================
    public static ResourceManager getInstance() {
        return INSTANCE;
    }
}
