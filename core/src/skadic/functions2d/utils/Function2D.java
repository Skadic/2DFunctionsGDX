package skadic.functions2d.utils;

import java.util.function.Function;

@FunctionalInterface
public interface Function2D extends Function<Number2D, Number2D> {

    Function2D IDENTITY = (n) -> n;

    @Override
    default Number2D apply(Number2D number2D){
        return calc(new Number2D(number2D));
    }

    Number2D calc(Number2D number2D);
}
