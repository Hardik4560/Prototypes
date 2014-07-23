
package com.hardy.learningandengine;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

/**
 * It will be a really important class in our game, responsible for switching between scenes and 
 * keeping track of the currently displayed scene. 
 * We will use SINGLETON HOLDER, which means we will be able to use this manger from the global level. 
 * It will also have an enum, containing our scene types. 
 * We also will create 4 BaseScene objects, for our scenes (splash, loading, menu and game scenes)
 * @author Hardik
 */
public class SceneManager {

    //---------------------------------------------
    // SCENES
    //---------------------------------------------

    private BaseScene splashScene;
    private BaseScene menuScene;
    private BaseScene gameScene;
    private BaseScene loadingScene;

    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------

    private static final SceneManager INSTANCE = new SceneManager();

    public enum SceneType {
        SCENE_SPLASH,
        SCENE_MENU,
        SCENE_GAME,
        SCENE_LOADING,
    }

    private SceneType currentSceneType = SceneType.SCENE_SPLASH;

    private BaseScene currentScene;

    private Engine engine = ResourcesManager.getInstance().engine;

    //---------------------------------------------
    // CONSTRUCTORS
    //---------------------------------------------

    private SceneManager() {}

    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------
    public void setScene(BaseScene scene) {
        engine.setScene(scene);
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }

    public void setScene(SceneType sceneType)
    {
        switch (sceneType)
        {
            case SCENE_MENU:
                setScene(menuScene);
                break;
            case SCENE_GAME:
                setScene(gameScene);
                break;
            case SCENE_SPLASH:
                setScene(splashScene);
                break;
            case SCENE_LOADING:
                setScene(loadingScene);
                break;
            default:
                break;
        }
    }

    //---------------------------------------------
    // SCENE CREATOR METHOD
    //---------------------------------------------
    public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
        ResourcesManager.getInstance().loadSplashScreen();

        splashScene = new SplashScene();
        currentScene = splashScene;
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
    }

    public void createMenuScene() {
        ResourcesManager.getInstance().loadMenuResources();
        menuScene = new MainMenuScene();
        loadingScene = new LoadingScene();

        setScene(menuScene);
        disposeSplashScene();
    }

    public void loadGameScene(final Engine mEngine) {
        setScene(loadingScene);

        ResourcesManager.getInstance().unloadMenuTextures();

        mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mEngine.unregisterUpdateHandler(pTimerHandler);

                ResourcesManager.getInstance().loadGameResources();
                gameScene = new GameScene();
                setScene(gameScene);
            }
        }));
    }

    //---------------------------------------------
    // SCENE DISPOSER METHOD
    //---------------------------------------------
    private void disposeSplashScene() {
        ResourcesManager.getInstance().unloadSplashScreen();
        splashScene.disposeScene();
        splashScene = null;
    }

    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    public static SceneManager getInstance() {
        return INSTANCE;
    }

    public SceneType getCurrentSceneType() {
        return currentSceneType;
    }

    public BaseScene getCurrentScene() {
        return currentScene;
    }
}
