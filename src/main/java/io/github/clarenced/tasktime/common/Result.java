package io.github.clarenced.tasktime.common;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A class that represents the result of an operation that can either succeed or fail.
 * @param <S> The type of the success value
 * @param <E> The type of the error value
 */
public class Result<S, E> {
    private final S successValue;
    private final E errorValue;
    private final boolean isSuccess;

    private Result(S successValue, E errorValue, boolean isSuccess) {
        this.successValue = successValue;
        this.errorValue = errorValue;
        this.isSuccess = isSuccess;
    }

    /**
     * Creates a successful result with the given value.
     * @param value The success value
     * @param <S> The type of the success value
     * @param <E> The type of the error value
     * @return A successful result
     */
    public static <S, E> Result<S, E> success(S value) {
        return new Result<>(value, null, true);
    }

    /**
     * Creates a successful result with no value.
     * @param <S> The type of the success value
     * @param <E> The type of the error value
     * @return A successful result with no value
     */
    public static <S, E> Result<S, E> success() {
        return new Result<>(null, null, true);
    }

    /**
     * Creates an error result with the given error value.
     * @param error The error value
     * @param <S> The type of the success value
     * @param <E> The type of the error value
     * @return An error result
     */
    public static <S, E> Result<S, E> error(E error) {
        return new Result<>(null, error, false);
    }

    /**
     * Returns whether this result is a success.
     * @return true if this result is a success, false otherwise
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Returns whether this result is an error.
     * @return true if this result is an error, false otherwise
     */
    public boolean isError() {
        return !isSuccess;
    }

    /**
     * Returns the success value if this result is a success.
     * @return The success value
     * @throws IllegalStateException if this result is an error
     */
    public S getSuccess() {
        if (!isSuccess) {
            throw new IllegalStateException("Cannot get success value from an error result");
        }
        return successValue;
    }

    /**
     * Returns the error value if this result is an error.
     * @return The error value
     * @throws IllegalStateException if this result is a success
     */
    public E getError() {
        if (isSuccess) {
            throw new IllegalStateException("Cannot get error value from a success result");
        }
        return errorValue;
    }

    /**
     * Maps the success value of this result to a new value.
     * @param mapper The function to map the success value
     * @param <T> The type of the new success value
     * @return A new result with the mapped success value, or the same error if this result is an error
     */
    public <T> Result<T, E> map(Function<S, T> mapper) {
        if (isSuccess) {
            return Result.success(mapper.apply(successValue));
        }
        return Result.error(errorValue);
    }

    /* If this result is an error, the error is propagated.
    * If this result is a success, the mapper function is applied to create a new Result.
    *
         * @param mapper The function to map the success value to a new Result
    * @param <T> The type of the new success value
    * @return A new Result from the mapper function, or the same error if this result is an error
    */
    public <T> Result<T, E> flatMap(Function<S, Result<T, E>> mapper) {
        if (isSuccess) {
            return mapper.apply(successValue);
        }
        return Result.error(errorValue);
    }


    /**
     * Executes the given consumer if this result is a success.
     * @param consumer The consumer to execute
     * @return This result
     */
    public Result<S, E> onSuccess(Consumer<S> consumer) {
        if (isSuccess) {
            consumer.accept(successValue);
        }
        return this;
    }

    /**
     * Executes the given consumer if this result is an error.
     * @param consumer The consumer to execute
     * @return This result
     */
    public Result<S, E> onError(Consumer<E> consumer) {
        if (!isSuccess) {
            consumer.accept(errorValue);
        }
        return this;
    }
}