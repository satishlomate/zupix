package io.zupix;

/** Executes the next middleware in the chain. */
@FunctionalInterface
public interface MiddlewareChain {
    void next();
}
