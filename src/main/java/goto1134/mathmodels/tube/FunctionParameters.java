package goto1134.mathmodels.tube;

import lombok.Getter;

/**
 * Created by Andrew
 * on 18.12.2016.
 */
@Getter
class FunctionParameters {
    @Getter
    private final double a;
    @Getter
    private final double b;
    @Getter
    private final double c;
    @Getter
    private final double d;
    @Getter
    private final double e;

    public FunctionParameters(double a, double b, double c, double d, double e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }
}
