package com.schwartz.calculator.impl;

import com.schwartz.calculator.AbstractCallableSchwartzCalculator;
import com.schwartz.calculator.ISchwartzCalculator;
import com.schwartz.model.SchwartzSimulatedData;
import com.schwartz.model.SchwartzModelParameters;
import com.schwartz.matlab.IMatlabObjectMapper;
import com.schwartz.matlab.impl.MatlabProxyHandler;
import com.schwartz.matlab.impl.SchwartzModelDataMapper;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.n52.matlab.control.MatlabConnectionException;
import org.n52.matlab.control.MatlabInvocationException;
import org.n52.matlab.control.MatlabProxy;
import org.n52.matlab.control.extensions.MatlabNumericArray;
import org.n52.matlab.control.extensions.MatlabTypeConverter;

/**
 * A {@link AbstractCallableSchwartzCalculator} implementation that handles the sample path generation.
 * 
 * @author woope
 * @see ISchwartzCalculator
 * @see AbstractCallableSchwartzCalculator
 */
public class SchwartzMatlabPathSimulator extends AbstractCallableSchwartzCalculator<SchwartzSimulatedData> {
    
    private static final Logger log = Logger.getLogger(SchwartzMatlabPathSimulator.class.getName());
    
    private IMatlabObjectMapper<SchwartzSimulatedData> modelDataMapper;
    private MatlabTypeConverter converter;
    private MatlabProxy proxy;
    private double initialSpot;
    private double initialConvenienceYield;
    private SchwartzModelParameters modelParameters;
    private boolean simulateTermStructure;

    public SchwartzMatlabPathSimulator(MatlabProxy proxy) {
        Validate.notNull(proxy, "The supplied MatlabProxy was null!");
        
        this.proxy = proxy;
        this.converter = new MatlabTypeConverter(proxy);
        this.modelDataMapper = new SchwartzModelDataMapper();
    }

    public SchwartzMatlabPathSimulator(MatlabProxy proxy, double initialSpot, double initialConvenienceYield, 
            SchwartzModelParameters modelParameters, boolean simulateTermStructure) {
        this(proxy);
        this.initialSpot = initialSpot;
        this.initialConvenienceYield = initialConvenienceYield;
        this.modelParameters = modelParameters;
        this.simulateTermStructure = simulateTermStructure;
    }
    
    @Override
    public SchwartzSimulatedData calculate() {
        try {
            proxy.eval("rng('shuffle')");
            proxy.eval("eval('init_consts')");
            converter.setNumericArray("parray", new MatlabNumericArray(modelParameters.getMatlabFormat(), null));
            proxy.eval("carray = num2cell(parray);");
            proxy.eval("pstruct = paramstruct(carray{:});");
            proxy.eval(new StringBuffer("[dates price cy ttm] = feval('gensynthdata',").append(initialSpot)
                                                                                       .append(", ")
                                                                                       .append(initialConvenienceYield)
                                                                                       .append(",pstruct, synth_years, dt, ncontracts, false);").toString());
            proxy.eval("data = feval('price2array', dates, price, cy, ttm,"  + simulateTermStructure +" );");
            
            return modelDataMapper.map(converter.getNumericArray("data").getRealArray2D());
        } catch (MatlabInvocationException ex) {
            log.error("Unable to simulate the spot price dynamics for the parameterset " + modelParameters + "! Returning null.", ex);
        }
        
        return null;
    }

    public MatlabProxy getProxy() {
        return proxy;
    }

    public void setProxy(MatlabProxy proxy) {
        this.proxy = proxy;
        this.converter = new MatlabTypeConverter(proxy);
    }

    public void setModelDataMapper(IMatlabObjectMapper<SchwartzSimulatedData> modelDataMapper) {
        this.modelDataMapper = modelDataMapper;
    }

    public void setInitialSpot(double initialSpot) {
        this.initialSpot = initialSpot;
    }

    public void setInitialConvenienceYield(double initialConvenienceYield) {
        this.initialConvenienceYield = initialConvenienceYield;
    }

    public void setModelParameters(SchwartzModelParameters modelParameters) {
        this.modelParameters = modelParameters;
    }

    public void setSimulateTermStructure(boolean simulateTermStructure) {
        this.simulateTermStructure = simulateTermStructure;
    }
    
    // Main-method testing.
    public static void main(String[] args) throws MatlabConnectionException {
        MatlabProxy proxy = new MatlabProxyHandler().getMatlabProxy();
        SchwartzMatlabPathSimulator calc = new SchwartzMatlabPathSimulator(proxy);
        calc.setInitialSpot(100);
        calc.setInitialConvenienceYield(0.6);
        calc.setModelParameters(new SchwartzModelParameters(0, 0.3, 2, -0.1, 0.6, 0.3, 0.6, -0.5));
        calc.setSimulateTermStructure(true);
        SchwartzSimulatedData data = calc.calculate();
        log.info(data);
        proxy.disconnect();
    }
}
