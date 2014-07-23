
package com.hardy.airhockey.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.hardy.airhockey.activities.BaseScene;
import com.hardy.airhockey.managers.SceneManager.SceneType;
import com.hardy.airhockey.utils.DeviceUtils;

public class SplashScene extends BaseScene {

    private Sprite splash;

    @Override
    public void createScene() {
        splash = new Sprite(DeviceUtils.SCREEN_WIDTH_CENTER, DeviceUtils.SCREEN_HEIGHT_CENTER, resourcesManager.splashRegion, vbom) {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera) {
                super.preDraw(pGLState, pCamera);
                pGLState.setDitherEnabled(true);
            }
        };

        splash.setScale(1.5f);
        attachChild(splash);
    }

    @Override
    public void onBackKeyPressed() {
        System.exit(1);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_SPLASH;
    }

    @Override
    public void disposeScene() {
        splash.detachSelf();
        splash.dispose();

        this.detachSelf();
        this.dispose();
    }

}
