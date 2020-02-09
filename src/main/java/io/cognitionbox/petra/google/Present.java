package io.cognitionbox.petra.google;

final public class Present<T> extends Optional<T> {

  public static <T extends Object> T checkNotNull(
          T reference, Object errorMessage) {
    if (reference == null) {
      throw new NullPointerException(String.valueOf(errorMessage));
    }
    return reference;
  }

  private final T reference;

  Present(T reference) {
    this.reference = reference;
  }

  @Override
  public boolean isPresent() {
    return true;
  }

  @Override
  public T get() {
    return reference;
  }

  @Override
  public boolean equals( Object object) {
    if (object instanceof Present) {
      Present<?> other = (Present<?>) object;
      return reference.equals(other.reference);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return 0x598df91c + reference.hashCode();
  }

  @Override
  public String toString() {
    return "Optional.of(" + reference + ")";
  }

  private static final long serialVersionUID = 0;
}
