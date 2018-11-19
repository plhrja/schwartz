package com.schwartz.matlab.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.schwartz.model.SchwartzModelParameters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author woope
 */
public class SchwartzParameterMapperTest {
    
    private SchwartzParameterMapper mapper;
    
    @BeforeEach
    public void setUp() {
        mapper = new SchwartzParameterMapper();
    }

    @Test
    public void mapThrowsIAEForEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> mapper.map(new double[][]{{}}));
    }

    @Test
    public void mapThrowsIAEForArgsThatDoNotComplyWithParameterSize() {
        assertThrows(
            IllegalArgumentException.class, 
            () -> mapper.map(new double[][]{{1, 2, 3, 4, 5, 6, 7}, {8}})
        );
    }

    @Test
    public void mapThrowsIAEForTooLargeInput() {
        assertThrows(
            IllegalArgumentException.class, 
            () -> mapper.map(new double[][]{{1, 2, 3, 4, 5, 6, 7, 8, 9}})
        );
    }
    
    @Test
    public void mapMapsInputCorrectly() {
        double mu = 0.1;
        double ss = 0.2;
        double kappa = 3;
        double alpha = 0.04;
        double se = 0.55;
        double intrest = 0.6;
        double rho = 0.007;
        double lambda = -0.8;
        double[][] input = new double[][]{{mu, ss, kappa, alpha, se, intrest, rho, lambda}};
        
        SchwartzModelParameters expected = new SchwartzModelParameters(mu, ss, kappa, alpha, se, intrest, rho, lambda);
        SchwartzModelParameters actual = mapper.map(input);
        
        assertEquals(expected, actual);
    }
    
}
