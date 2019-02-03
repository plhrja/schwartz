package com.schwartz.matlab;

/**
 * {@link IMatlabConvertable} objects can be converted to a Matlab-readable format by invoking the 
 * {@link IMatlabConvertable#getMatlabFormat() getMatlabFormat()}-method.
 * 
 * @author woope
 */
public interface IMatlabConvertable {
    
    public static final int MATLAB_TIMELINE_INDEX = 0;
    public static final int MATLAB_SPOT_PRICE_INDEX = 1;
    public static final int MATLAB_CONVENIENCE_YIELD_INDEX = 2;
    public static final int MATLAB_FIRST_INDEX_FOR_TERM_STRUCTURE = 3;
    
    public double[][] getMatlabFormat();
    
}
