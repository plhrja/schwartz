package com.schwartz.matlab.impl;

import com.schwartz.matlab.AbstractMatlabProxyHandler;
import java.io.File;
import org.apache.log4j.Logger;
import org.n52.matlab.control.MatlabConnectionException;
import org.n52.matlab.control.MatlabProxy;
import org.n52.matlab.control.MatlabProxyFactory;
import org.n52.matlab.control.MatlabProxyFactoryOptions;

/**
 * The default implementation of {@link AbstractMatlabProxyHandler}.
 * 
 * The handler will create {@link MatlabProxy MatlabProxies} with the following options:
 * <ul>
 *      <li>Re-use existing Matlab-sessions if possible.</li>
 *      <li>No splash</li>
 *      <li>Start Matlab at '/var/matlab_startdir'.</li>
 * </ul>
 * The options above are wrapped in a {@link MatlabProxyFactoryOptions} object.
 * 
 * @author woope
 * @see AbstractMatlabProxyHandler
 */
public class MatlabProxyHandler extends AbstractMatlabProxyHandler {

    private static final Logger log = Logger.getLogger(MatlabProxyHandler.class.getName());
    
    private static final String MATLAB_STARTDIR = "/var/matlab_startdir/JavaInterface";
    private static final boolean MATLAB_SPLASH_HIDDEN = true;
    
    private MatlabProxyFactory proxyFactory;
    private final MatlabProxyFactoryOptions defaultOptions;
    private final MatlabProxyFactoryOptions fallbackOption;
    
    public MatlabProxyHandler() {
        this.defaultOptions = 
            new MatlabProxyFactoryOptions.Builder()
                .setUsePreviouslyControlledSession(true)
                .setHidden(MATLAB_SPLASH_HIDDEN)
                .setMatlabStartingDirectory(new File(MATLAB_STARTDIR))
                .build();
        this.fallbackOption = 
            new MatlabProxyFactoryOptions.Builder()
                .setUsePreviouslyControlledSession(false)
                .setHidden(MATLAB_SPLASH_HIDDEN)
                .setMatlabStartingDirectory(new File(MATLAB_STARTDIR))
                .build();

        this.proxyFactory = new MatlabProxyFactory(defaultOptions);
    }
    
    @Override
    public MatlabProxy getMatlabProxy() {
        try {
            return proxyFactory.getProxy();
        } catch (MatlabConnectionException ex) {
            log.error("Could not initialize the calculator session using previous proxy, trying with fallback parameters!", ex);
        }
        try {
            MatlabProxy proxy = new MatlabProxyFactory(fallbackOption).getProxy();
            log.info("Calculator session initialized succesfully with fallback parameters!");
            return proxy;
        } catch (MatlabConnectionException ex) {
            log.error("Could not initialize the calculator even with the fallback parameters, returning null!", ex);
        }
        return null;
    }

    public MatlabProxyFactory getProxyFactory() {
        return proxyFactory;
    }

    public void setProxyFactory(MatlabProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    /**
     * Returns the {@link MatlabProxyFactoryOptions default options} that are used to create the {@link MatlabProxy proxies}.
     * @return The {@link MatlabProxyFactoryOptions default options} that are used to create the {@link MatlabProxy proxies}
     */
    public MatlabProxyFactoryOptions getDefaultOptions() {
        return defaultOptions;
    }
    
}
