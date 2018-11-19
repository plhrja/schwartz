package com.schwartz.matlab.impl;

import com.schwartz.model.SchwartzModelParameters;
import com.schwartz.matlab.IMatlabObjectMapper;

/**
 *
 * @author woope
 */
public class SchwartzParameterMapper implements IMatlabObjectMapper<SchwartzModelParameters>{
    
    @Override
    public SchwartzModelParameters map(double[][] data) {
        int rowlen = data.length;
        if (data.length != 1 || data[0].length != 8) {
            throw new IllegalArgumentException("The input data is of invalid dimensions! Expected 1x8, got " 
                    + rowlen + "x" + (rowlen > 0 ? data[0].length : 0) + ".");
        }
        
        double[] val = data[0];
        return new SchwartzModelParameters(val[0], val[1], val[2], val[3], val[4], val[5], val[6], val[7]);
    }
    
}
