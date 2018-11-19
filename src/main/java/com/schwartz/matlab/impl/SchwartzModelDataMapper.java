package com.schwartz.matlab.impl;

import com.schwartz.model.SchwartzSimulatedData;
import com.schwartz.matlab.IMatlabObjectMapper;
import static com.schwartz.matlab.IMatlabConvertable.*;
import java.util.ArrayList;

/**
 *
 * @author woope
 */
public class SchwartzModelDataMapper implements IMatlabObjectMapper<SchwartzSimulatedData> {

    @Override
    public SchwartzSimulatedData map(double[][] data) {
        validateInput(data);
        SchwartzSimulatedData modelData = new SchwartzSimulatedData();
        
        for (int j = 0; j < data[0].length; j++) {
            Integer time = (int) Math.floor(data[MATLAB_TIMELINE_INDEX][j]);
            Double spotPrice = data[MATLAB_SPOT_PRICE_INDEX][j];
            Double convenienceYield = data[MATLAB_CONVENIENCE_YIELD_INDEX][j];
            
            ArrayList<SchwartzSimulatedData.ContractEntry> contractEntries = new ArrayList<>();
            for (int i = MATLAB_FIRST_INDEX_FOR_TERM_STRUCTURE; i < data.length; i += 2) {
                if (Double.isNaN(data[i][j]) || Double.isNaN(data[i + 1][j])) {
                    continue;
                }
                
                contractEntries.add(new SchwartzSimulatedData.ContractEntry(data[i][j], data[i + 1][j]));
            }
            
            modelData.put(time, spotPrice, convenienceYield, contractEntries);
        }
        
        return modelData;
    }

    private void validateInput(double[][] data) throws IllegalArgumentException {
        if (data == null) {
            throw new IllegalArgumentException("Expected non-null data!");
        }
        
        int rowLength = data.length;
        if (rowLength < 3) {
            throw new IllegalArgumentException("The Data needs to supply at least the timeline, spot price and convenience yield!");
        }
        if (rowLength % 2 == 0) {
            throw new IllegalArgumentException("Expected an odd number of datasets! The number of datasets: " + rowLength);
        }
        if (!equalColumnLengths(data)) {
            throw new IllegalArgumentException("Datasets of inequal length");
        }
    }

    private boolean equalColumnLengths(double[][] data) {
        for (int i = 0; i < data.length - 1; i++) {
            if (data[i].length != data[i + 1].length) {
                return false;
            }
        }
        return true;
    }

}
