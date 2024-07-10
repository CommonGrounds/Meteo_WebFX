package com.java4now.meteo_webfx.shared;

import dev.webfx.platform.audio.Audio;
import dev.webfx.platform.audio.AudioService;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.resource.Resource;
import dev.webfx.platform.shutdown.Shutdown;
import javafx.scene.image.Image;


/**
 * @author Bruno Salmon
 */
final class WebFXUtil {

    static String toResourceUrl(String resourceName) {
        return Resource.toUrl(resourceName, WebFXUtil.class);
    }

    static Audio newMusic(String resourceName) {
        return AudioService.loadMusic(toResourceUrl(resourceName));
    }

    static Audio newSound(String resourceName) {
        return AudioService.loadSound(toResourceUrl(resourceName));
    }

    static void setLooping(Audio audio, boolean looping) {
        if (audio != null)
            audio.setLooping(looping);
    }

    static void setVolume(Audio audio, double volume) {
        if (audio != null)
            audio.setVolume(volume);
    }

    static void playMusic(Audio music) {
        if (music != null)
            music.play();
    }

    static void pauseMusic(Audio music) {
        if (music != null)
            music.pause();
    }

    static void stopMusic(Audio music) {
        if (music != null)
            music.stop();
    }

    static void playSound(Audio sound) {
        if (sound != null)
            sound.play();
    }

    static Image newImage(String resourceName) {
        return new Image(toResourceUrl(resourceName), true);
    }

    static void onImageLoaded(Image image, Runnable runnable) {
        if (!onImageLoadedIfLoading(image, runnable)){
//            Console.log("runnable.run() - image allReady loaded");
            runnable.run();
        }else{
//            Console.log("runnable.run() in Listener");
        }

    }

    static boolean hasImageFinishedLoading(Image image) {
        return image == null || image.getProgress() >= 1;
    }

    static boolean onImageLoadedIfLoading(Image image, Runnable runnable) {
        if (hasImageFinishedLoading(image)){
//            Console.log("ImageFinishedLoading before Listener");
            return false;
        }
        image.progressProperty().addListener((observableValue, oldProgress, progress) -> {
            if (progress.doubleValue() == 1){
//                Console.log("image_progress: DONE!");
                runnable.run();
            }else{
//                Console.log("image_progress: " + progress.doubleValue());
            }
        });
        return true;
    }

    static void exit(int status) {
        Shutdown.softwareShutdown(true, status);
    }

}
