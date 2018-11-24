package com.schwartz.business.impl;

import com.schwartz.business.ISchwartzCalculatorService;
import com.schwartz.calculator.impl.SchwartzMatlabPathSimulator;
import com.schwartz.matlab.AbstractMatlabProxyHandler;
import com.schwartz.matlab.impl.MatlabProxyHandler;
import com.schwartz.model.SchwartzModelParameters;
import com.schwartz.model.SchwartzSimulatedData;
import java.util.concurrent.Callable;
import org.n52.matlab.control.MatlabProxy;
import org.springframework.stereotype.Service;


/**
 * A service that handles the initiating of {@link MatlabProxy MatlabProxies} to a {@link SchwartzMatlabPathSimulator} instance, and also handles the
 * calculation process utilizing an instance of {@link IConcurrentCalculatorHandler}.
 *
 * @author woope
 * @see ISchwartzCalculatorService
 * @see schwartzController
 */
@Service
public class SchwartzSpotDynamicsCalculatorService implements ISchwartzCalculatorService<SchwartzSimulatedData> {

    private final AbstractMatlabProxyHandler proxyHandler;
    private MatlabProxy proxy;

    public SchwartzSpotDynamicsCalculatorService() {
        this.proxyHandler = new MatlabProxyHandler();
    }

	@Override
	public Callable<SchwartzSimulatedData> run(
        double initialSpot, 
        double initialConvenienceYield,
        SchwartzModelParameters modelParameters, 
        boolean simulateTermStructure
    ) {
        if (proxy == null || !proxy.isConnected()) {
            proxy = proxyHandler.getMatlabProxy();
        }

		return new SchwartzMatlabPathSimulator(
            proxy, 
            initialSpot, 
            initialConvenienceYield,
            modelParameters, 
            simulateTermStructure
        );
	}

}
