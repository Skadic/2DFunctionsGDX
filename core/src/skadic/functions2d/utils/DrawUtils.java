package skadic.functions2d.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;


public class DrawUtils {

    public static final ShapeRenderer RENDERER = new ShapeRenderer();
    private static Matrix4 projectionMatrix;

    public static void drawLine(float x1, float y1, float x2, float y2, int lineWidth, Color color){
        Gdx.gl.glLineWidth(lineWidth);
        drawLine(x1, y1, x2, y2, color);
        Gdx.gl.glLineWidth(1);
    }

    public static void drawLine(float x1, float y1, float x2, float y2, Color color){
        RENDERER.setProjectionMatrix(projectionMatrix);
        RENDERER.begin(ShapeRenderer.ShapeType.Line);
        RENDERER.setColor(color);
        RENDERER.line(new Vector2(x1, y1), new Vector2(x2, y2));
        RENDERER.end();
    }

    public static void drawLine(int x1, int y1, int x2, int y2){
        drawLine(x1, y1, x2, y2, Color.WHITE);
    }

    public static void drawPixel(int x, int y, Color color){
        RENDERER.setProjectionMatrix(projectionMatrix);
        RENDERER.begin(ShapeRenderer.ShapeType.Point);
        RENDERER.setColor(color);
        RENDERER.point(x, y, 0);
        RENDERER.end();
    }

    public static void drawCircle(float x, float y, int size, Color color){
        RENDERER.setProjectionMatrix(projectionMatrix);
        RENDERER.begin(ShapeRenderer.ShapeType.Filled);
        RENDERER.setColor(color);
        RENDERER.circle(x, y, size);
        RENDERER.end();
    }

    public static void drawRect(float x, float y, int sizeX, int sizeY, Color color){
        RENDERER.setProjectionMatrix(projectionMatrix);
        RENDERER.begin(ShapeRenderer.ShapeType.Line);
        RENDERER.setColor(color);
        RENDERER.rect(x, y, sizeX, sizeY);
        RENDERER.end();
    }

    public static void setProjectionMatrix(Matrix4 projectionMatrix) {
        DrawUtils.projectionMatrix = projectionMatrix;
    }
}
