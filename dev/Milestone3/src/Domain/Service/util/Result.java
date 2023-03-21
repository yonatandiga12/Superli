package Domain.Service.util;

import java.util.Optional;

public class Result<T> {
    private Optional<T> value;
    private Optional<String> error;

    /**
     * private Constructor for result
     *
     * @param value The value of the result
     * @param error The Error of the result
     */
    private Result(T value, String error) {
        this.value = Optional.ofNullable(value);
        this.error = Optional.ofNullable(error);
    }

    /**
     * Factory pattern for Ok type Result
     *
     * @param value Value of the Ok
     * @return Ok encapsulating the value
     * @param <U>
     */
    public static <U> Result<U> makeOk(U value) {
        return new Result<>(value, null);
    }

    /**
     * Factory for Error type Result
     *
     * @param error Error message of the Error
     * @return Error encapsulating the error message
     * @param <U>
     */
    public static <U> Result<U> makeError(String error) {
        return new Result<>(null, error);
    }

    /**
     * Predicate for Error type
     *
     * @return true if the Result is an Error
     */
    public boolean isError() {
        return error.isPresent();
    }

    /**
     * Predicate for Ok type
     *
     * @return true if the Result is an Ok
     */
    public boolean isOk() {
        return !isError();
    }

    /**
     * Getter for Result's value
     *
     * @return Value encapsulated, if any
     */
    public T getValue() {
        return value.get();
    }

    /**
     * Getter for error message
     *
     * @return Error message encapsulated, if any
     */
    public String getError() {
        return error.get();
    }
}