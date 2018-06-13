package skadic.functions2d.utils;

import java.util.Objects;

public class Number2D{

    double re, im;

    public Number2D() {
        re = 0;
        im = 0;
    }

    public Number2D(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Number2D(Number2D z){
        re = z.re;
        im = z.im;
    }

    public Number2D add(Number2D z){
        return new Number2D(re + z.re, im + z.im);
    }

    public Number2D subtract(Number2D z){
        return new Number2D(re - z.re, im - z.im);
    }

    public Number2D multiply(Number2D z){
        return new Number2D(re * z.re - im * z.im, re * z.im + im * z.re);
    }

    public Number2D division(Number2D z){
        return new Number2D((re * z.re + im * z.im)/(z.re * z.re + z.im * z.im), (im * z.re - re * z.im)/(z.re * z.re + z.im * z.im));
    }

    public Number2D pow(int exp){
        if(exp == 0){
            return new Number2D(1, 0);
        } else {
            Number2D old = new Number2D(re, im);
            Number2D newNum = new Number2D(re, im);
            for (int i = 0; i < exp - 1; i++) {
                newNum = newNum.multiply(old);
            }
            return newNum;
        }
    }

    public Number2D add(double num){
        return new Number2D(re + num, im);
    }

    public Number2D subtract(double num){
        return new Number2D(re - num, im);
    }

    public Number2D multiply(double num){
        return new Number2D(re * num, im * num);
    }

    public Number2D division(double num){
        return new Number2D(re / num, im / num);
    }


    public double abs(){
        return Math.sqrt(re * re + im * im);
    }

    public Number2D conjugate(){
        return new Number2D(re, -im);
    }

    public double re(){
        return re;
    }

    public double im(){
        return im;
    }

    public void setRe(double re) {
        this.re = re;
    }

    public void setIm(double im) {
        this.im = im;
    }


    public double getPolarAngle(){
        return Math.atan2(im, re);
    }

    public static Number2D sin(Number2D z) {
        double x = Math.exp(z.im);
        double x_inv = 1/x;
        double r = Math.sin(z.re) * (x + x_inv)/2;
        double i = Math.cos(z.re) * (x - x_inv)/2;
        return new Number2D(r,i);
    }

    public static Number2D cos(Number2D z)
    {
        double x = Math.exp(z.im);
        double x_inv = 1/x;
        double r = Math.cos(z.re) * (x + x_inv)/2;
        double i = -Math.sin(z.re) * (x - x_inv)/2;
        return new Number2D(r,i);
    }

    public static Number2D fromPolar(double angle, double abs){
        return new Number2D(Math.cos(angle) * abs, Math.sin(angle) * abs);
    }

    @Override
    public String toString() {
        return String.valueOf(round(re, 3)) + " " + (im < 0 ? "-" : "+") + " i * " + Math.abs(round(im, 3));
    }

    private double round(double x, int digits){
        digits = Math.max(0, digits);
        x *= Math.pow(10, digits);
        x = Math.floor(x);
        return x / Math.pow(10, digits);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj instanceof Number2D){
            Number2D num = (Number2D) obj;
            return re == num.re && im == num.im;
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(re, im);
    }
}
