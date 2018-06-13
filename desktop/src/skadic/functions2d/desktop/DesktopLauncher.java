package skadic.functions2d.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import skadic.functions2d.FunctionDrawer;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1900;
		config.height = 1000;
		config.foregroundFPS = 60;
		config.backgroundFPS = 2;
		new LwjglApplication(new FunctionDrawer(), config);
	}
}
