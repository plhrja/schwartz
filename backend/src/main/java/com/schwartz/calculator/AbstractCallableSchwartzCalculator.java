package com.schwartz.calculator;

import java.util.concurrent.Callable;

/**
 * An abstraction of the calculator that implements the {@link Callable} interface.
 * 
 * The @{@link AbstractCallableSchwartzCalculator#call() AbstractCallableSchwartzCalculator.call} is just a simple follow through of the calculation 
 * function. Implementing an extension of this class will result in concurrent calculators.
 * 
 * @author woope
 * @param <K> The return-type of the calculation results.
 * @see ISchwartzCalculator
 */
public abstract class AbstractCallableSchwartzCalculator<K> implements ISchwartzCalculator<K>, Callable<K>{

    @Override
    public K call() {
        return calculate();
    }

    @Override
    public abstract K calculate();
}
