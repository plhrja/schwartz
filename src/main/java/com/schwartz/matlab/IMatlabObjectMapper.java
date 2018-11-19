package com.schwartz.matlab;

import org.n52.matlab.control.MatlabOperations;
import org.n52.matlab.control.extensions.MatlabNumericArray;
import org.n52.matlab.control.extensions.MatlabTypeConverter;

/**
 * Maps the results from an Matlab function evaluation to the specified target type {@code K}.
 * 
 * Note that the default return format of Matlab function evaluations is an {@link Object} array containing the return objects.
 * These arrays can be further converted to numerical arrays with {@link MatlabTypeConverter#getNumericArray(java.lang.String)}. 
 * The default output format is naturally the 2D array, which can be constructed with {@link MatlabNumericArray#getRealArray2D()}.
 * 
 * @author woope
 * @param <K>
 * 
 * @see MatlabOperations
 * @see MatlabTypeConverter
 * @see MatlabNumericArray
 */
public interface IMatlabObjectMapper<K> {
    
    /**
     * Map the Matlab output data to the specified target type.
     * 
     * @param data The data to be mapped.
     * @return The mapped data converted to the specified format.
     * 
     * @throws IllegalArgumentException if the supplied data does not comply with the expected format.
     */
    public K map(double[][] data) throws IllegalArgumentException;
    
}
