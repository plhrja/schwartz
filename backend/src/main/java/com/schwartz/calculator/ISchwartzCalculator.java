package com.schwartz.calculator;

/**
 * An Interface for implementations of the Schwartz two factor model calculators.<br>
 * 
 * The implementations need to handle able to handle both parameters estimation and sample path simulations.
 * 
 * @author woope
 * @param <K> The return-type of the results of the calculation.
 */
public interface ISchwartzCalculator<K> {
    
    /**
     * Simulates sample paths of the spot price and the convenience yield that correspond to the {@link SchwartzModelParameters modelParameters}.
     * 
     * @return The simulated {@link SchwartzSimulatedData modelData}.
     */
    public K calculate();
}
