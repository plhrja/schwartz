package com.schwartz.matlab.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.schwartz.model.SchwartzSimulatedData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author woope
 */
public class SchwartzModelDataMapperTest {
    
    private SchwartzModelDataMapper mapper;
    
    @BeforeEach
    public void setUp() {
        mapper = new SchwartzModelDataMapper();
    }

    @Test
    public void mapThrowsIAEForEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> mapper.map(new double[][]{{}}));
    }

    @Test
    public void mapThrowsIAEForNullInput() {
        assertThrows(IllegalArgumentException.class, () -> mapper.map(null));
    }

    @Test
    public void mapThrowsIAEForTooFewDatasets() {
        assertThrows(
            IllegalArgumentException.class, 
            () -> mapper.map(new double[][]{
                {1, 2, 3}, 
                {8, 8.5, 9},
            })
        );
    }
        
    @Test
    public void mapThrowsIAEForInequalColumnLengths() {
        assertThrows(
            IllegalArgumentException.class, 
            () -> mapper.map(new double[][]{
                {1, 2, 3}, 
                {8, 8.5, 9},
                {0.1, 0.2, 0, -0.1},
                {0.1, 0.2, 0},
                {0.1, 0.2, 3},
            })
        );
    }
    
    @Test()
    public void mapThrowsIAEForInequalColumnLengthsAsLast() {
        assertThrows(
            IllegalArgumentException.class, () ->
            mapper.map(new double[][]{
                {1, 2, 3}, 
                {8, 8.5, 9},
                {0.1, 0.2, 0, -0.1}
            })
        );
    }

    @Test
    public void mapThrowsIAEForEvenNumberOfDatasets() {
        assertThrows(
            IllegalArgumentException.class, () ->
            mapper.map(new double[][]{
                {1, 2, 3}, 
                {8, 8.5, 9},
                {0.1, 0.2, 0},
                {1, 2, 3}
            })
        );
    }

    @Test
    public void mapMapsCorrectlyDataWithoutTermStructure() {
        SchwartzSimulatedData expected = new SchwartzSimulatedData();
        expected.put(1, 8d, 0.1);
        expected.put(2, 8.5, 0.2);
        expected.put(3, 9d, 0d);
        SchwartzSimulatedData actual = mapper.map(new double[][]{{1, 2, 3}, 
                                                             {8, 8.5, 9},
                                                             {0.1, 0.2, 0},
                                                            });
        assertEquals(expected, actual);
    }

    @Test
    public void mapMapsCorrectlyDataWithTermStructure() {
        SchwartzSimulatedData expected = new SchwartzSimulatedData();
        expected.put(1, 8d, 0.1, new SchwartzSimulatedData.ContractEntry[]{new SchwartzSimulatedData.ContractEntry(3d, 8.3),
                                                                        new SchwartzSimulatedData.ContractEntry(30d, 9.5)
                                                                       });
        expected.put(2, 9.5, 0.2, new SchwartzSimulatedData.ContractEntry[]{new SchwartzSimulatedData.ContractEntry(2d, 9.2),
                                                                         new SchwartzSimulatedData.ContractEntry(25d, 10.2)
                                                                        });
        expected.put(3, 10d, 0.3, new SchwartzSimulatedData.ContractEntry[]{new SchwartzSimulatedData.ContractEntry(1d, 9.9),
                                                                         new SchwartzSimulatedData.ContractEntry(20d, 11d)
                                                                        });
        SchwartzSimulatedData actual = mapper.map(new double[][]{{1, 2, 3},
                                                             {8, 9.5, 10},
                                                             {0.1, 0.2, 0.3},
                                                             {3, 2, 1},
                                                             {8.3, 9.2, 9.9},
                                                             {30, 25d, 20},
                                                             {9.5, 10.2, 11}
                                                            });
        assertEquals(expected, actual);
    }

    @Test
    public void mapMapsCorrectlyDataWithTermStructureWithMissingData() {
        SchwartzSimulatedData expected = new SchwartzSimulatedData();
        expected.put(1, 8d, 0.1, new SchwartzSimulatedData.ContractEntry[]{new SchwartzSimulatedData.ContractEntry(3d, 8.3),
                                                                        new SchwartzSimulatedData.ContractEntry(30d, 9.5)
                                                                       });
        expected.put(2, 9.5, 0.2, new SchwartzSimulatedData.ContractEntry[]{new SchwartzSimulatedData.ContractEntry(2d, 9.2),
                                                                        });
        expected.put(3, 10d, 0.3, new SchwartzSimulatedData.ContractEntry[]{new SchwartzSimulatedData.ContractEntry(1d, 9.9),
                                                                         new SchwartzSimulatedData.ContractEntry(20d, 11d)
                                                                        });
        SchwartzSimulatedData actual = mapper.map(new double[][]{{1, 2, 3},
                                                             {8, 9.5, 10},
                                                             {0.1, 0.2, 0.3},
                                                             {3, 2, 1},
                                                             {8.3, 9.2, 9.9},
                                                             {30, Double.NaN, 20},
                                                             {9.5, Double.NaN, 11}
                                                            });
        assertEquals(expected, actual);
    }
    
}
