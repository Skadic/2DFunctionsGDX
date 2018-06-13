package skadic.functions2d.utils;


import com.badlogic.gdx.graphics.Color;

import java.util.List;
import java.util.stream.Collectors;

public class WindingCalculator {


    public static long calcWindingNumber(List<Number2D> verteces, Function2D function){
        if(verteces.size() <= 2) return 0;

        double epsilon = -(Math.PI / 2) * 0.9;

        List<Color> pathColors = CalcUtils.calculatePath(verteces, function).stream().distinct().collect(Collectors.toList());


        double winding = 0;

        for (int i = 0; i < pathColors.size() - 1; i++) {
            double a1 = getPolarAngleByColor(pathColors.get(i)), a2 = getPolarAngleByColor(pathColors.get(i + 1));
            if(a2 > 3 * Math.PI / 2 + epsilon && a1 < Math.PI / 2 - epsilon)
                a2 -= 2 * Math.PI;
            else if(a1 > 3 * Math.PI / 2 + epsilon && a2 < Math.PI / 2 - epsilon)
                a1 -= 2 * Math.PI;
            winding += a2 - a1;
        }

        double a1 = getPolarAngleByColor(pathColors.get(pathColors.size() - 1)), a2 = getPolarAngleByColor(pathColors.get(0));
        winding += a2 - a1;

        System.out.println(winding / (2 * Math.PI));
        return -Math.round(winding / (2 * Math.PI));
    }

    private static double getPolarAngleByColor(Color color){
        double h = java.awt.Color.RGBtoHSB((int) (color.r * 255), (int) (color.g * 255), (int) (color.b * 255), null)[0];
        return h * 2 * Math.PI;
    }
}
