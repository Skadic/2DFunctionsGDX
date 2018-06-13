package skadic.functions2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import skadic.functions2d.utils.*;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static skadic.functions2d.utils.DrawUtils.RENDERER;
import static skadic.functions2d.utils.DrawUtils.drawLine;

public class FunctionDisplayer {

    private final int xOffset;
    private final int yOffset;
    private final int size;
    private double scale;
    private SpriteBatch batch;
    private final Function2D func;
    private BitmapFont font;
    private GlyphLayout layout;
    private BitmapFont roboto;
    private Number2D num;
    private boolean rootFound;

    //Points for loop Polygon for winding number calculation
    private List<Number2D> points;

    private boolean render;


    public FunctionDisplayer(int xOffset, int yOffset, int size, double scale, SpriteBatch batch, Function2D func) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.size = size;
        this.scale = scale;
        this.batch = batch;
        this.func = func;
        font = new BitmapFont();
        layout = new GlyphLayout();
        points = new LinkedList<>();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.absolute("C:/Programming/2DFunctionsGDX/core/assets/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 10;
        roboto = generator.generateFont(parameter);
        generator.dispose();

        num = new Number2D(2, -1);
        render = true;
    }

    public void update() {
        if(Gdx.input.isButtonPressed(Buttons.LEFT) && !render){
            float x = Gdx.input.getX() - xOffset - size / 2;
            float y = (Gdx.graphics.getHeight() - Gdx.input.getY()) - yOffset - size / 2;

            if(inRange(Gdx.input.getX(), xOffset, xOffset + size) && inRange((Gdx.graphics.getHeight() - Gdx.input.getY()), yOffset, yOffset + size)) {

                num.setRe(x / scale);
                num.setIm(y / scale);
            }
        }

        if(Gdx.input.isButtonPressed(Buttons.RIGHT)){
            if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)){
                points.clear();
                rootFound = false;
            } else {
                float x = Gdx.input.getX() - xOffset - size / 2;
                float y = (Gdx.graphics.getHeight() - Gdx.input.getY()) - yOffset - size / 2;

                if(inRange(Gdx.input.getX(), xOffset, xOffset + size) && inRange((Gdx.graphics.getHeight() - Gdx.input.getY()), yOffset, yOffset + size)) {
                    points.add(new Number2D(x / scale, y / scale));
                }
            }
        }

        if(Gdx.input.isKeyJustPressed(Keys.ENTER)){
            rootFound = WindingCalculator.calcWindingNumber(points.stream().distinct().collect(Collectors.toList()), func) != 0;
            points.clear();
        }

        if(Gdx.input.isKeyJustPressed(Keys.R)){
            render = !render;
        }
        if(Gdx.input.isKeyJustPressed(Keys.PLUS)){
            scale += 10F;
        }
        if(Gdx.input.isKeyJustPressed(Keys.MINUS) && scale > 10){
            scale -= 10F;
        }
    }

    private boolean inRange(double n, double min, double max){
        return n >= min && n <= max;
    }


    /**
     * draws everything to the screen
     */
    public void render(){
        //if render is true, the Functions should be drawn as normal
        if(render) {
            drawFunc(xOffset, yOffset, size, scale, func);
            drawFunc((int) (xOffset + size * 1.01F), yOffset, size, scale, Function2D.IDENTITY);
        }

        //draw Coordinates
        drawCoords(xOffset, yOffset, size, size, scale);
        drawCoords((int) (xOffset + size * 1.01F), yOffset, size, size, scale);

        //if render is false, you should click on a point and it should draw a line to the corresponding point in the output space.
        if(!render) {
            Number2D result = func.apply(num).multiply(scale);
            Number2D numScaled = num.multiply(scale);

            float inPosX = (float) numScaled.re() + xOffset + size / 2;
            float inPosY = (float) numScaled.im() + yOffset + size / 2;

            float outPosX, outPosY;

            if (!inRange((float) result.re() + xOffset + size * 1.01F + size / 2, xOffset + size * 1.01F, xOffset + size * 2.01F) ||
                    !inRange((float) (result.im() + yOffset + size / 2), yOffset, yOffset + size)) {
                Number2D n = new Number2D(result.re(), result.im());

                n = n.division(n.abs()).multiply(Math.sqrt(2) * size / 2);

                outPosX = clamp((float) n.re() + xOffset + size * 1.01F + size / 2, xOffset + size * 1.01F, xOffset + size * 2.01F);
                outPosY = clamp((float) n.im() + yOffset + size / 2, yOffset, yOffset + size);
            } else {
                outPosX = clamp((float) result.re() + xOffset + size * 1.01F + size / 2, xOffset + size * 1.01F, xOffset + size * 1.01F + size);
                outPosY = clamp((float) (result.im() + yOffset + size / 2), yOffset, yOffset + size);
            }

            Color color = CalcUtils.getColorForFunctionAndNumber(num, func);

            /*DrawUtils.drawLine(
                    inPosX,
                    inPosY,
                    outPosX,
                    outPosY,
                    3,
                    color);*/

            DrawUtils.drawCircle(inPosX, inPosY, 4, color);
            DrawUtils.drawLine(xOffset + size * 1.01F + size / 2, yOffset + size / 2, outPosX, outPosY, 2, color);
            DrawUtils.drawCircle(outPosX, outPosY, 4, color);

            List<Number2D> funcPoints = CalcUtils.calculatePathVerteces(points);
            for (int i = 0; i < funcPoints.size() - 1; i++) {
                Number2D n1 = func.apply(funcPoints.get(i));
                Number2D n2 = func.apply(funcPoints.get(i + 1));
                float x1 = (float) (xOffset + size / 2 + size * 1.01 + n1.multiply(scale).re());
                float y1 = (float) (yOffset + size / 2 +  n1.multiply(scale).im());
                float x2 = (float) (xOffset + size / 2 + size * 1.01 + n2.multiply(scale).re());
                float y2 = (float) (yOffset + size / 2 +  n2.multiply(scale).im());
                Color c = CalcUtils.getColorForFunctionAndNumber(funcPoints.get(i), func);
                DrawUtils.drawLine(x1, y1, x2, y2, 3, c);
            }
            if(!funcPoints.isEmpty()) {
                Number2D n1 = func.apply(funcPoints.get(funcPoints.size() - 1));
                Number2D n2 = func.apply(funcPoints.get(0));
                float x1 = (float) (xOffset + size / 2 + size * 1.01 + n1.multiply(scale).re());
                float y1 = (float) (yOffset + size / 2 + n1.multiply(scale).im());
                float x2 = (float) (xOffset + size / 2 + size * 1.01 + n2.multiply(scale).re());
                float y2 = (float) (yOffset + size / 2 + n2.multiply(scale).im());
                Color c = CalcUtils.getColorForFunctionAndNumber(funcPoints.get(funcPoints.size() - 1), func);
                DrawUtils.drawLine(x1, y1, x2, y2, 3, c);
            }
        }



        for (int i = 0; i < points.size() - 1; i++) {
            float x1 = (float) (xOffset + size / 2 + points.get(i).multiply(scale).re()), y1 = (float) (yOffset + size / 2 + points.get(i).multiply(scale).im());
            float x2 = (float) (xOffset + size / 2 + points.get(i + 1).multiply(scale).re()), y2 = (float) (yOffset + size / 2 + points.get(i + 1).multiply(scale).im());
            DrawUtils.drawLine(x1, y1, x2, y2, 3, Color.WHITE);
        }
        if(points.size() > 1) {
            float x1 = (float) (xOffset + size / 2 + points.get(points.size() - 1).multiply(scale).re()), y1 = (float) (yOffset + size / 2 + points.get(points.size() - 1).multiply(scale).im());
            float x2 = (float) (xOffset + size / 2 + points.get(0).multiply(scale).re()), y2 = (float) (yOffset + size / 2 + points.get(0).multiply(scale).im());
            DrawUtils.drawLine(x1, y1, x2, y2, 3, Color.WHITE);
        }

        boolean shouldEnd = false;
        if(!batch.isDrawing()){
            batch.begin();
            shouldEnd = true;
        }

        font.draw(batch,"Input Space", xOffset, yOffset + size + font.getCapHeight() + font.getXHeight());
        font.draw(batch,"Output Space", xOffset + size * 1.01F, yOffset + size + font.getCapHeight() + font.getXHeight());

        font.draw(batch, "Root found: " + (rootFound ? "Yes" : "No"), xOffset + 60 * font.getSpaceWidth(), yOffset + size + font.getCapHeight() + font.getXHeight());

        if(shouldEnd)
            batch.end();
    }

    public void drawFunc(int xPos, int yPos, int size, double scale, Function<Number2D, Number2D> func){
        RENDERER.setProjectionMatrix(batch.getProjectionMatrix());
        RENDERER.begin(ShapeRenderer.ShapeType.Point);
        for (int y = yPos; y < yPos + size; y++) {
            for (int x = xPos; x < xPos + size; x++) {
                Number2D n = new Number2D(x - xPos - size / 2, y - yPos - size / 2);
                RENDERER.setColor(CalcUtils.getColorForFunctionAndNumber(n, func, scale));
                RENDERER.point(x, y, 0);
            }
        }
        RENDERER.end();
    }

    private void drawCoords(int x, int y, int w, int h, double scale){
        if(scale <= 0) throw new IllegalArgumentException("scale <= 0: " + scale);

        final int tickSize = 3;

        int mX = x + w / 2;
        int mY = y + h / 2;

        drawLine(mX, y, mX, y + h);
        drawLine(mX, y + h, mX - 5, y + h - 5);
        drawLine(mX, y + h, mX + 5, y + h - 5);

        drawLine(x, mY, x + w, mY);
        drawLine(x + w, mY, x + w - 5, mY - 5);
        drawLine(x + w, mY, x + w - 5, mY + 5);

        int step = 50;

        for (int i = step; i < w / 2; i += step) {
            drawLine(mX + i, mY, mX + i, mY - tickSize);
        }
        for (int i = -step; i > -w / 2; i -= step) {
            drawLine(mX + i, mY, mX + i, mY - tickSize);
        }

        for (int i = step; i < h / 2; i += step) {
            drawLine(mX, mY + i, mX - tickSize, mY + i);
        }

        for (int i = -step; i > -h / 2; i -= step) {
            drawLine(mX, mY + i, mX - tickSize, mY + i);
        }

        boolean shouldEnd = false;
        if(!batch.isDrawing()){
            batch.begin();
            shouldEnd = true;
        }

        layout.setText(font, "Re(z)");
        font.draw(batch, "Re(z)", x + w - layout.width, mY + font.getCapHeight() + font.getXHeight());
        font.draw(batch, "Im(z)", mX + font.getCapHeight(), y + h + font.getCapHeight());

        layout.setText(roboto, "0");
        roboto.draw(batch, "0", (float) (mX - roboto.getCapHeight() * 1.5), mY - roboto.getCapHeight());

        for (int i = step; i < w / 2; i += step) {
            String num = String.valueOf(i / scale).endsWith(".0") ? String.valueOf(i / scale).replace(".0", "") : String.valueOf(round(i / scale, 2));
            layout.setText(roboto, num);
            roboto.draw(batch, num, mX + i - layout.width / 2, mY - roboto.getCapHeight());
        }
        for (int i = -step; i > -w / 2; i -= step) {
            String num = String.valueOf(i / scale).endsWith(".0") ? String.valueOf(i / scale).replace(".0", "") : String.valueOf(round(i / scale, 2));
            layout.setText(roboto, num);
            roboto.draw(batch, num, mX + i - layout.width / 2, mY - roboto.getCapHeight());
        }

        for (int i = step; i < h / 2; i += step) {
            String num = String.valueOf(i / scale).endsWith(".0") ? String.valueOf(-i / scale).replace(".0", "") : String.valueOf(round(i / scale, 2));
            layout.setText(roboto, num);
            roboto.draw(batch, num, mX - layout.width - tickSize, mY - i + roboto.getXHeight() / 2);
        }

        for (int i = -step; i > -h / 2; i -= step) {
            String num = String.valueOf(i / scale).endsWith(".0") ? String.valueOf(-i / scale).replace(".0", "") : String.valueOf(round(i / scale, 2));
            layout.setText(roboto, num);
            roboto.draw(batch, num, mX - layout.width - tickSize, mY - i + roboto.getXHeight() / 2);
        }

        if(shouldEnd)
            batch.end();
    }

    private float clamp(float x, float min, float max){
        if(x > max) return max;
        if(x < min) return min;
        else        return x;
    }

    private double round(double x, int digits){
        if(digits < 0) throw new IllegalArgumentException("digits < 0: " + digits);

        x = Math.round(x * Math.pow(10, digits)) / Math.pow(10, digits);
        return x;
    }
}
