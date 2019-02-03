/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schwartz.matlab.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.schwartz.matlab.AbstractMatlabProxyHandler;

import org.n52.matlab.control.MatlabConnectionException;
import org.n52.matlab.control.MatlabInvocationException;
import org.n52.matlab.control.MatlabProxy;
import org.n52.matlab.control.MatlabProxyFactory;

/**
 * @author woope
 */
public class MatlabProxyHandlerTest {
    
    private MatlabProxyHandler handler;
    private MatlabProxy proxy;
    private MatlabProxyFactory proxyFactory;
    
    @BeforeEach
    public void setUpClass() throws MatlabConnectionException {
        proxy = mock(MatlabProxy.class);
        proxyFactory = mock(MatlabProxyFactory.class);
        
        when(proxyFactory.getProxy()).thenReturn(proxy);
        
        handler = new MatlabProxyHandler();
        handler.setProxyFactory(proxyFactory);
    }
    
    @Test
    public void getProxyCreatesANewProxy() throws MatlabConnectionException {
        MatlabProxy matlabProxy = handler.getMatlabProxy();
        
        verify(proxyFactory, times(1)).getProxy();
        verifyNoMoreInteractions(proxyFactory);        
        assertNotNull(matlabProxy);
    }
    
    public void disconnectProxyReturnsTrueForNullArgs() {
        boolean actual = AbstractMatlabProxyHandler.disconnectMatlabProxy(null);
        assertTrue(actual);
    }
    
    @Test
    public void disconnectProxyDisconnectsThePoxyAndReturnsOriginalAnswer() {
        boolean expected = false;
        when(proxy.disconnect()).thenReturn(expected);
        boolean actual = AbstractMatlabProxyHandler.disconnectMatlabProxy(proxy);
        assertEquals(expected, actual);

        expected = true;
        when(proxy.disconnect()).thenReturn(expected);
        actual = AbstractMatlabProxyHandler.disconnectMatlabProxy(proxy);
        verify(proxy, times(2)).disconnect();
        assertEquals(expected, actual);
    }
    
    @Test
    public void terminateProxyTerminatesADisconnectedProxy() throws MatlabInvocationException {
        when(proxy.disconnect()).thenReturn(false);
        AbstractMatlabProxyHandler.terminateMatlabProxy(proxy);
        
        verify(proxy, times(1)).isConnected();
        verify(proxy, times(1)).exit();
        verifyNoMoreInteractions(proxy);
    }
    
    @Test
    public void terminateProxyTerminatesAndDisconnectsAConnectedProxy() throws MatlabInvocationException {
        when(proxy.isConnected()).thenReturn(true);
        AbstractMatlabProxyHandler.terminateMatlabProxy(proxy);
        
        verify(proxy, times(1)).isConnected();
        verify(proxy, times(1)).disconnect();
        verify(proxy, times(1)).exit();
        verifyNoMoreInteractions(proxy);
    }
    
}
