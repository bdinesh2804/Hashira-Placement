import java.nio.file.*;
import java.util.*;
import java.math.*;
import org.json.*;

public class FindC {

    static BigDecimal computeConstant(List<BigDecimal> xs, List<BigDecimal> ys, int k) {
        BigDecimal constant = BigDecimal.ZERO;

        for (int i = 0; i < k; i++) {
            BigDecimal xi = xs.get(i);
            BigDecimal yi = ys.get(i);
            BigDecimal term = yi;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigDecimal xj = xs.get(j);
                    // (0 - xj) / (xi - xj)
                    BigDecimal numerator = xj.negate();
                    BigDecimal denominator = xi.subtract(xj);
                    term = term.multiply(numerator.divide(denominator, 50, RoundingMode.HALF_UP));
                }
            }

            constant = constant.add(term);
        }

        // Round to nearest integer
        return constant.setScale(0, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: java FindC <input.json>");
            return;
        }

        String jsonContent = Files.readString(Path.of(args[0]));
        JSONObject data = new JSONObject(jsonContent);

        JSONObject keys = data.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        List<BigDecimal> xs = new ArrayList<>();
        List<BigDecimal> ys = new ArrayList<>();

        for (String key : data.keySet()) {
            if (key.equals("keys")) continue;
            JSONObject entry = data.getJSONObject(key);
            int base = Integer.parseInt(entry.getString("base"));
            BigInteger value = new BigInteger(entry.getString("value"), base);
            xs.add(BigDecimal.valueOf(xs.size() + 1));
            ys.add(new BigDecimal(value));
        }

        List<BigDecimal> subX = xs.subList(0, k);
        List<BigDecimal> subY = ys.subList(0, k);

        BigDecimal c = computeConstant(subX, subY, k);
        System.out.println(c.toBigInteger());
    }
}
