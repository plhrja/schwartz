package com.schwartz.business;

import java.util.concurrent.Callable;

import com.schwartz.model.SchwartzModelParameters;

/**
 * An interface for different {@link ISchwartzCalculatorService ISchwartzCalculatorServices} that provides all the necessary methods for carrying out
 * calculation-tasks.
 *
 * @author woope
 * @param <K> the return-type of the calculation results.
 */
public interface ISchwartzCalculatorService<K> {

    public Callable<K> run(
        double initialSpot, 
        double initialConvenienceYield, 
        SchwartzModelParameters modelParameters,
        boolean simulateTermStructure
    );


}
