package testTaskOvsiy;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of a hook that allows to listen when user presses buttons
 * and react when user pressed ESC button
 */
public class EscapeButtonHook {

    private static final Logger logger = Logger.getLogger(EscapeButtonHook.class.getName());

    /**
     * Registers keys listener
     *
     * @param shouldInterrupt flag to finish counting
     */
    public void registerHook(AtomicBoolean shouldInterrupt) {
        try {
            GlobalScreen.registerNativeHook();
            NativeKeyListener nativeKeyListener = getNativeKeyListener(shouldInterrupt);
            GlobalScreen.addNativeKeyListener(nativeKeyListener);
        } catch (NativeHookException e) {
            logger.severe(String.format("Could not register hook: %s", e.getMessage()));
        }
    }

    /**
     * Unregisters keys listener
     */
    public void unregisterHook() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            logger.severe(String.format("Could not unregister hook: %s", e.getMessage()));
        }
    }

    /**
     * Disabling native logging
     */
    public void disableLogging() {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
    }

    /**
     * Implementation of ESC button handler
     *
     * @param shouldInterrupt flag to finish counting
     * @return implementation of NativeKeyListener
     */
    private NativeKeyListener getNativeKeyListener(AtomicBoolean shouldInterrupt) {
        return new NativeKeyListener() {
            @Override
            public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
                // intentionally empty
            }

            @Override
            public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
                if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
                    shouldInterrupt.set(true);
                    try {
                        GlobalScreen.unregisterNativeHook();
                    } catch (NativeHookException e) {
                        logger.severe(String.format(
                                "Error during key pressing: %s", e.getMessage()));
                    }
                }
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
                // intentionally empty
            }
        };
    }
}
