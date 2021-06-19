package black.bracken.verdirectrenewed.util.functional;

import java.util.function.Consumer;
import java.util.function.Function;

public interface Either<L, R> {

    <T> Either<L, T> map(Function<R, T> mapping);

    <T> Either<T, R> mapLeft(Function<L, T> mapping);

    <T> Either<L, T> flatMap(Function<R, Either<L, T>> mapping);

    boolean isRight();

    boolean isLeft();

    R orElse(R alternative);

    L toLeft(L alternative);

    void consume(Consumer<R> onRight, Consumer<L> onLeft);

    static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }

    static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }

    class Right<L, R> implements Either<L, R> {
        private final R value;

        private Right(R value) {
            this.value = value;
        }

        @Override
        public <T> Either<L, T> map(Function<R, T> mapping) {
            return new Right<>(mapping.apply(this.value));
        }

        @Override
        public <T> Either<T, R> mapLeft(Function<L, T> mapping) {
            return new Right<>(this.value);
        }

        @Override
        public <T> Either<L, T> flatMap(Function<R, Either<L, T>> mapping) {
            return mapping.apply(this.value);
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public R orElse(R alternative) {
            return this.value;
        }

        @Override
        public L toLeft(L alternative) {
            return alternative;
        }

        @Override
        public void consume(Consumer<R> onRight, Consumer<L> onLeft) {
            onRight.accept(this.value);
        }
    }

    class Left<L, R> implements Either<L, R> {
        private final L value;

        private Left(L value) {
            this.value = value;
        }

        @Override
        public <T> Either<L, T> map(Function<R, T> mapping) {
            return new Left<>(this.value);
        }

        @Override
        public <T> Either<T, R> mapLeft(Function<L, T> mapping) {
            return new Left<>(mapping.apply(this.value));
        }

        @Override
        public <T> Either<L, T> flatMap(Function<R, Either<L, T>> mapping) {
            return new Left<>(this.value);
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public R orElse(R alternative) {
            return alternative;
        }

        @Override
        public L toLeft(L alternative) {
            return this.value;
        }

        @Override
        public void consume(Consumer<R> onRight, Consumer<L> onLeft) {
            onLeft.accept(this.value);
        }
    }

}
