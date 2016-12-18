package goto1134.mathmodels.tube;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Created by Andrew
 * on 18.12.2016.
 */
@Getter(AccessLevel.PACKAGE)
class FunctionParameters {
    private final double a;
    private final double b;
    private final double c;
    private final double d;
    private final double e;

    FunctionParameters(double a, double b, double c, double d, double e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }
}
