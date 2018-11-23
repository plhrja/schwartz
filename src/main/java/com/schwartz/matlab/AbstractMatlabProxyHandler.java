package com.schwartz.matlab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.n52.matlab.control.MatlabInvocationException;
import org.n52.matlab.control.MatlabProxy;

/**
 * Implementations are able to create, disconnect and terminate Matlab-sessions. The Matlab-sessions are controlled through
 * {@link MatlabProxy MatlabProxies}.
 *
 * @author woope
 * @see MatlabProxy
 */
public abstract class AbstractMatlabProxyHandler {

    private static final Logger log = LogManager.getLogger(AbstractMatlabProxyHandler.class.getName());

    /**
     * Initializes and returns a {@link MatlabProxy}.
     *
     * If a Matlab-session has aleady been initialized alive, the {@link MatlabProxy} to be returned will be connected to the existing Matlab -
     * session.
     *
     * @return A initialized {@link MatlabProxy}.
     */
    public abstract MatlabProxy getMatlabProxy();

    /**
     * Disconnect's the supplied {@link MatlabProxy proxy}.
     *
     * @param proxy The {@link MatlabProxy} to be disconnected.
     * @return {@code true} if the supplied {@link MatlabProxy} was disconnected, {@code false} otherwise.
     */
    public static boolean disconnectMatlabProxy(MatlabProxy proxy) {
        if (proxy != null) {
            return proxy.disconnect();
        }
        log.warn("The supplied proxy was null!");
        return true;
    }

    /**
     * Terminates the supplied {@link MatlabProxy proxy} by terminating the (possibly connected) Matlab - session, if one exists.
     *
     * @param proxy The {@link MatlabProxy} to be terminated.
     */
    public static void terminateMatlabProxy(MatlabProxy proxy) {
        if (proxy != null) {
            if (proxy.isConnected()) {
                disconnectMatlabProxy(proxy);
            }
            try {
                proxy.exit();
            } catch (MatlabInvocationException ex) {
                log.error("Could not terminate the supplied proxy!", ex);
            }
        }
    }
}
