package io.cognitionbox.petra.google;

/**
 * Implementation of an {@link Optional} not containing a reference.
 */
final public class Absent<T> extends Optional<T> {

    public static <T extends Object> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static <T extends Object> T checkNotNull(
            T reference, Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    static final Absent<Object> INSTANCE = new Absent<>();

    @SuppressWarnings("unchecked") // implementation is "fully variant"
    public static <T> Optional<T> withType() {
        return (Optional<T>) INSTANCE;
    }

    private Absent() {
    }

    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public T get() {
        throw new IllegalStateException("Optional.get() cannot be called on an absent value");
    }

    @Override
    public boolean equals(Object object) {
        return object == this;
    }

    @Override
    public int hashCode() {
        return 0x79a31aac;
    }

    @Override
    public String toString() {
        return "Optional.absent()";
    }

    private Object readResolve() {
        return INSTANCE;
    }

    private static final long serialVersionUID = 0;
}
