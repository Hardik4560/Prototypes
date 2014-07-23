
package com.hardy.airhockey.managers;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import com.hardy.airhockey.activities.BaseScene;
import com.hardy.airhockey.scenes.SplashScene;

public class SceneManager {

    //===========================================
    // VARIABLES
    //===========================================
    private final static SceneManager INSTANCE = new SceneManager();

    private SceneType currentSceneType = SceneType.SCENE_SPLASH;
    private BaseScene currentScene;

    private Engine engine = ResourceManager.getInstance().engine;

    //---------------------------------------------
    // SCENES
    //---------------------------------------------

    private BaseScene splashScene;
    private BaseScene menuScene;
    private BaseScene loadingScene;
    private BaseScene gameScene;

    public enum SceneType {
        SCENE_SPLASH,
        SCENE_MENU,
        SCENE_LOADING,
        SCENE_GAME,
    }

    //===========================================
    // CONSTRUCTORS
    //===========================================
    private SceneManager() {}

    //===========================================
    // CREATE METHODS
    //===========================================
    public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
        ResourceManager.getInstance().loadSplashScreen();

        splashScene = new SplashScene();
        currentScene = splashScene;

        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }

    private void disposeSplashScene() {
        ResourceManager.getInstance().unLoadSplashScreen();
        splashScene.disposeScene();
        splashScene = null;
    }

    //===========================================
    // CLASS LOGIC
    //===========================================

    public void setScene(BaseScene pScene) {
        engine.setScene(pScene);
        currentScene = pScene;
        currentSceneType = pScene.getSceneType();
    }

    public void setScene(SceneType sceneType) {
        switch (sceneType) {
            case SCENE_SPLASH:
                setScene(splashScene);
                break;
            case SCENE_MENU:
                setScene(menuScene);
                break;
            case SCENE_LOADING:
                setScene(loadingScene);
                break;
            case SCENE_GAME:
                setScene(gameScene);
                break;
            default:
                break;
        }
    }

    //===========================================
    // GETTERS/SETTERS
    //===========================================
    public static SceneManager getInstance() {
        return INSTANCE;
    }
}
