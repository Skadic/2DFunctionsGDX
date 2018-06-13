package skadic.functions2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import skadic.functions2d.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FunctionDrawer extends ApplicationAdapter {
	private SpriteBatch batch;
	private FunctionDisplayer displayer;

	private Function2D
			func1 = (z) -> z.add(1).division(new Number2D(2, 0).subtract(new Number2D(z).multiply(5)).add(new Number2D(z).pow(2).multiply(2))),
			func2 = (z) -> z.pow(3),
			funcP10_B = (z) -> new Number2D(Math.sin(z.re() * z.im()), Math.sin(z.re() * z.im() - Math.PI / 2)),
			funcA36 = (z) -> new Number2D(Math.sqrt(z.re() * z.re() + Math.pow(z.im(), 3)), 0),
			funcA37 = (z) -> {
				double x = z.re();
				double y = z.im();

				return new Number2D((x * x + y * y - 2) * (x * x - y * y - 1), 0);
			};
	@Override
	public void create () {
		batch = new SpriteBatch();
		displayer = new FunctionDisplayer(10, 10, 900, 100D, batch, func1);
		DrawUtils.setProjectionMatrix(batch.getProjectionMatrix());

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		batch.setColor(1F, 1F, 1F, 1F);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		displayer.update();
		displayer.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
