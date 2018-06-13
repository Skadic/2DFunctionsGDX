package skadic.functions2d.utils;

public class Math2D {


    public static Number2D add(Number2D n1, Number2D n2){
        return new Number2D(n1.re + n2.re, n1.im + n2.im);
    }

    public static Number2D add(Number2D n1, Number n2){
        return new Number2D(n1.re + n2.doubleValue(), n1.im);
    }

    public static Number2D subtract(Number2D n1, Number2D n2){
        return new Number2D(n1.re - n2.re, n1.im - n2.im);
    }

    public static Number2D subtract(Number2D n1, Number n2){
        return new Number2D(n1.re - n2.doubleValue(), n1.im);
    }

    public static Number2D multiply(Number2D n1, Number2D n2){
        return new Number2D(n1.re * n2.re - n1.im * n2.im, n1.im * n2.re + n1.re * n2.im);
    }

    public static Number2D multiply(Number2D n1, Number n2){
        return new Number2D(n1.re * n2.doubleValue(), n1.im * n2.doubleValue());
    }

    public static Number2D division(Number2D n1, Number2D n2){
        return new Number2D((n1.re * n2.re + n1.im * n2.im)/(n2.re * n2.re + n2.im * n2.im), (n1.im * n2.re - n1.re * n2.im)/(n2.re * n2.re + n2.im * n2.im));
    }

    public static Number2D division(Number2D n1, Number n2){
        return new Number2D(n1.re / n2.doubleValue(), n1.im / n2.doubleValue());
    }

    public static Number2D pow(Number2D num, int exp){
        if(exp == 0){
            return new Number2D(1, 0);
        }
        Number2D old = new Number2D(num.re, num.im);
        Number2D newNum = new Number2D(num.re, num.im);

        for (int i = 0; i < exp - 1; i++) {
            newNum = multiply(old, newNum);
        }
        return newNum;
    }

}
