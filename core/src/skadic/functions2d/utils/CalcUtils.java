package skadic.functions2d.utils;


import com.badlogic.gdx.graphics.Color;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class CalcUtils {

    private static final Function<Double, Double> LOGARITHMIC = Math::log,
                                LINEAR = x -> x,
                                SQUARE = x -> x * x,
                                SPECIAL = x -> {
                                    if (x >= 0 && x <= Math.PI / 8)
                                        return -Math.cos(4 * x) + 1;
                                    else if(x < 0)
                                        return 0D;
                                    else if(x > 0)
                                        return 1D;
                                    return -1D;
                                };

    private static Function<Double, Double> CHOSEN = (x) -> 1 * LINEAR.apply(x);

    public static Color getColorForNumber(Number2D num){
        return new com.badlogic.gdx.graphics.Color(((java.awt.Color.getHSBColor(
                (float) (num.getPolarAngle() / (2 * Math.PI)),
                1F,
                (float) clamp(CHOSEN.apply(num.abs()), 0, 1)
        ).getRGB() & 0xFFFFFF) << 8) + 0xFF);
    }

    public static Color getColorForFunctionAndNumber(Number2D num, Function<Number2D, Number2D> func){
        return getColorForNumber(func.apply(num));
    }

    public static Color getColorForFunctionAndNumber(Number2D num, Function<Number2D, Number2D> func, double scale){
        return getColorForNumber(func.apply(num.multiply(1D / scale)));
    }

    public static List<Color> calculatePath(List<Number2D> verteces, Function2D function){
        if(verteces.isEmpty()) return new LinkedList<>();

        List<Color> points = new LinkedList<>();

        for (int i = 0; i < verteces.size() - 1; i++) {
            Function<Double, Number2D> line = getLineFunction(verteces.get(i), verteces.get(i + 1));
            double abs = verteces.get(i + 1).subtract(verteces.get(i)).abs();
            for (int j = 0; j < 10 * abs; j++) {
                double interval = 1 / (10 * abs);
                points.add(CalcUtils.getColorForFunctionAndNumber(line.apply(j * interval), function));
            }
        }

        Function<Double, Number2D> line = getLineFunction(verteces.get(verteces.size() - 1), verteces.get(0));
        double abs = verteces.get(verteces.size() - 1).subtract(verteces.get(0)).abs();
        for (int j = 0; j < abs; j++) {
            double interval = 1 / (abs);
            points.add(CalcUtils.getColorForFunctionAndNumber(line.apply(j * interval), function));
        }

        return points;
    }

    public static List<Number2D> calculatePathVerteces(List<Number2D> verteces){
        if(verteces.isEmpty()) return new LinkedList<>();

        List<Number2D> points = new LinkedList<>();

        for (int i = 0; i < verteces.size() - 1; i++) {
            Function<Double, Number2D> line = getLineFunction(verteces.get(i), verteces.get(i + 1));
            double abs = verteces.get(i + 1).subtract(verteces.get(i)).abs();
            for (int j = 0; j < 10 * abs; j++) {
                double interval = 1 / (10 * abs);
                points.add(line.apply(j * interval));
            }
        }

        Function<Double, Number2D> line = getLineFunction(verteces.get(verteces.size() - 1), verteces.get(0));
        double abs = verteces.get(verteces.size() - 1).subtract(verteces.get(0)).abs();
        for (int j = 0; j < abs; j++) {
            double interval = 1 / (abs);
            points.add(line.apply(j * interval));
        }

        return points;
    }

    private static double clamp(double x, double min, double max) {
        if(x < min) return min;
        if(x > max) return max;
        return x;
    }

    public static Function<Double, Number2D> getLineFunction(Number2D a, Number2D b){
        Number2D distanceVec = b.subtract(a);
        return (t) -> a.add(distanceVec.multiply(t));
    }
}
