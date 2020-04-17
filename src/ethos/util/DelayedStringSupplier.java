package ethos.util;

import java.util.function.Supplier;

public class DelayedStringSupplier implements Supplier<String> {

    private long time = 0;
    private final long delay;
    private final String message;

    public DelayedStringSupplier(String message, long delay) {
        this.message = message;
        this.delay = delay;
    }

    @Override
    public String get() {
        if (System.currentTimeMillis() - time >= delay) {
            time = System.currentTimeMillis();
            return message;
        } else {
            return null;
        }
    }
}
