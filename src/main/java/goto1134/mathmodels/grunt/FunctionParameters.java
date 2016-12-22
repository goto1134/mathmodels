package goto1134.mathmodels.grunt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Created by Andrew
 * on 21.12.2016.
 */
@Value
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class FunctionParameters {
    int max_t;
    int C;
    int Q;
    int x;
    int y;
}
