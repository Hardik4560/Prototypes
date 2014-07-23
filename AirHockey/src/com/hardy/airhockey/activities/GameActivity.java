
package com.hardy.airhockey.activities;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import com.hardy.airhockey.managers.ResourceManager;
import com.hardy.airhockey.managers.SceneManager;
import com.hardy.airhockey.utils.DeviceUtils;

public class GameActivity extends BaseGameActivity {

    //===============================================
    // VARIABLES
    //===============================================
    private BoundCamera camera;

    private ResourceManager mResourceManager;

    //===============================================
    // OVERRIDE CLASS BEHAVIOUR
    //===============================================    
    @Override
    public EngineOptions onCreateEngineOptions() {
        camera = new BoundCamera(0, 0, DeviceUtils.SCREEN_WIDTH, DeviceUtils.SCREEN_HEIGHT);

        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        return engineOptions;
    }

    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) {
        return new LimitedFPSEngine(pEngineOptions, 60);
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
        ResourceManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
        mResourceManager = ResourceManager.getInstance();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
        SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
