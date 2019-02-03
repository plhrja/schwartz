package com.schwartz.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

/**
 * @author woope
 */
public class SchwartzSimulatedDataTest {

    @Test
    public void getMatlabFormatReturnsCorrectFormatForModelDataWithNoTermStructure() {
        SchwartzSimulatedData modelData = new SchwartzSimulatedData();
        modelData.put(1, 8d, 0.1);
        modelData.put(2, 9.5, 0.2);
        modelData.put(3, 10d, 0.3);
        
        double[][] expected = new double[][]{{1, 2, 3},
                                             {8, 9.5, 10},
                                             {0.1, 0.2, 0.3},
                                            };
        double[][] actual = modelData.getMatlabFormat();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getMatlabFormatReturnsCorrectFormatForModelDataWithTermStructure() {
        SchwartzSimulatedData modelData = new SchwartzSimulatedData();
        modelData.put(1, 8d, 0.1, new SchwartzSimulatedData.ContractEntry[]{new SchwartzSimulatedData.ContractEntry(3d, 8.3),
                                                                        new SchwartzSimulatedData.ContractEntry(10d, 9d),
                                                                        new SchwartzSimulatedData.ContractEntry(30d, 9.5)
                                                                       });
        modelData.put(2, 9.5, 0.2, new SchwartzSimulatedData.ContractEntry[]{new SchwartzSimulatedData.ContractEntry(2d, 9.2),
                                                                        new SchwartzSimulatedData.ContractEntry(9d, 9.7)
                                                                       });
        modelData.put(3, 10d, 0.3, new SchwartzSimulatedData.ContractEntry[]{new SchwartzSimulatedData.ContractEntry(1d, 9.9),
                                                                        new SchwartzSimulatedData.ContractEntry(8d, 9.8),
                                                                        new SchwartzSimulatedData.ContractEntry(20d, 11d)
                                                                       });
        
        double[][] expected = new double[][]{{1, 2, 3},
                                             {8, 9.5, 10},
                                             {0.1, 0.2, 0.3},
                                             {3, 2, 1},
                                             {8.3, 9.2, 9.9},
                                             {10, 9, 8},
                                             {9, 9.7, 9.8},
                                             {30, Double.NaN, 20},
                                             {9.5, Double.NaN, 11}
                                            };
        double[][] actual = modelData.getMatlabFormat();
        assertArrayEquals(expected, actual);
    }
    
}
