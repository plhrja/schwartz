package com.schwartz.model;

import com.schwartz.matlab.IMatlabConvertable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.lang3.Validate;

/**
 * Spot price and convenience yield data (for a single commodity).
 *
 * @author woope
 */
public class SchwartzSimulatedData implements IMatlabConvertable {

    private static final String NULL_ARGUMENT_ERROR_MESSAGE = "The supplied argument was null!";

    private final Map<Integer, Integer> time2TermStructureLengths;
    private final Map<Integer, Entry> time2entryMap;

    public SchwartzSimulatedData() {
        this.time2entryMap = new TreeMap<>();
        this.time2TermStructureLengths = new HashMap<>();
    }

    public void put(Integer time, Double spotPrice, Double convenienceYield) {
        time2entryMap.put(time, new Entry(spotPrice, convenienceYield));
    }

    public void put(Integer time, Double spotPrice, Double convenienceYield, ContractEntry[] contractEntries) {
        Entry entry2put = new Entry(spotPrice, convenienceYield, Arrays.asList(contractEntries));
        addNonzeroTermStructureLength(time, entry2put);
        time2entryMap.put(time, entry2put);
    }

    public void put(Integer time, Double spotPrice, Double convenienceYield, List<ContractEntry> contractEntries) {
        Entry entry2put = new Entry(spotPrice, convenienceYield, contractEntries);
        addNonzeroTermStructureLength(time, entry2put);
        time2entryMap.put(time, entry2put);
    }

    public Collection<Entry> getEntries() {
        return time2entryMap.values();
    }

    public Set<Integer> getTimeline() {
        return time2entryMap.keySet();
    }

    public boolean hasContractEntries() {
        return !time2TermStructureLengths.isEmpty();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.time2entryMap);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SchwartzSimulatedData other = (SchwartzSimulatedData) obj;
        if (!Objects.equals(this.time2entryMap, other.time2entryMap)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SchwartzModelData{" + "time2TermStructureLengths=" + time2TermStructureLengths + "\n, time2entryMap=" + time2entryMap + '}';
    }

    @Override
    public double[][] getMatlabFormat() {
        Integer maxTermStructureLength = this.hasContractEntries() ? Collections.max(time2TermStructureLengths.values()) : 0;
        int rowLength = 3 + (maxTermStructureLength * 2);
        int columnLength = time2entryMap.size();
        double[][] data2matlab = new double[rowLength][columnLength];

        Set<Integer> timeline = getTimeline();
        Collection<Entry> entries = getEntries();

        // First, construct the timeline row.
        // Note that this stupid iterative method has to be used as no primitive casting for arrays is possible.
        Iterator<Integer> timelineIterator = timeline.iterator();
        for (int j = 0; j < timeline.size(); j++) {
            data2matlab[MATLAB_TIMELINE_INDEX][j] = (double) timelineIterator.next();
        }

        // Next, construct the rest of the matrix.
        Iterator<Entry> entriesIterator = entries.iterator();
        for (int j = 0; j < time2entryMap.size(); j++) {
            Entry entry = entriesIterator.next();
            data2matlab[MATLAB_SPOT_PRICE_INDEX][j] = entry.getSpotPrice();
            data2matlab[MATLAB_CONVENIENCE_YIELD_INDEX][j] = entry.getConvenienceYield();

            if (this.hasContractEntries()) {
                List<ContractEntry> contractEntries = entry.getContractEntriesList();
                
                for (int i = 0; i < maxTermStructureLength; i++) {
                    int contractEntryIndex = MATLAB_FIRST_INDEX_FOR_TERM_STRUCTURE + (i * 2);
                    double time2maturityValue;
                    double futuresPriceValue;

                    if (i < contractEntries.size()) {
                        // Add the existing contract entries.
                        ContractEntry contractEntry = contractEntries.get(i);
                        time2maturityValue = contractEntry.time2maturity;
                        futuresPriceValue = contractEntry.futuresPrice;
                    } else {
                        // If there are no more contract entres, the values need to be replaced with -Inf.
                        time2maturityValue = Double.NaN;
                        futuresPriceValue = Double.NaN;
                    }

                    data2matlab[contractEntryIndex][j] = time2maturityValue;
                    data2matlab[contractEntryIndex + 1][j] = futuresPriceValue;
                }
            }
        }
        return data2matlab;
    }

    private void addNonzeroTermStructureLength(Integer time, Entry entry2put) {
        if (time2TermStructureLengths.containsKey(time) && !entry2put.hasContractData()) {
            time2TermStructureLengths.remove(time);
        } else if (entry2put.hasContractData()) {
            time2TermStructureLengths.put(time, entry2put.getContractEntriesList().size());
        }
    }

    public static class Entry {

        private final Double spotPrice;
        private final Double convenienceYield;
        private final List<ContractEntry> contractEntriesList;

        public Entry(Double spotPrice, Double convenienceYield) {
            Validate.notNull(spotPrice, NULL_ARGUMENT_ERROR_MESSAGE);
            Validate.notNull(convenienceYield, NULL_ARGUMENT_ERROR_MESSAGE);

            this.spotPrice = spotPrice;
            this.convenienceYield = convenienceYield;
            this.contractEntriesList = new ArrayList<>();
        }

        public Entry(Double spotPrice, Double convenienceYield, List<ContractEntry> contractEntriesList) {
            Validate.notNull(spotPrice, NULL_ARGUMENT_ERROR_MESSAGE);
            Validate.notNull(convenienceYield, NULL_ARGUMENT_ERROR_MESSAGE);
            Validate.notNull(contractEntriesList, NULL_ARGUMENT_ERROR_MESSAGE);

            this.spotPrice = spotPrice;
            this.convenienceYield = convenienceYield;
            this.contractEntriesList = contractEntriesList;
        }

        public Double getSpotPrice() {
            return spotPrice;
        }

        public Double getConvenienceYield() {
            return convenienceYield;
        }

        public List<ContractEntry> getContractEntriesList() {
            return contractEntriesList;
        }

        public boolean hasContractData() {
            return contractEntriesList != null && !contractEntriesList.isEmpty();
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 59 * hash + Objects.hashCode(this.spotPrice);
            hash = 59 * hash + Objects.hashCode(this.convenienceYield);
            hash = 59 * hash + Objects.hashCode(this.contractEntriesList);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Entry other = (Entry) obj;
            if (!Objects.equals(this.spotPrice, other.spotPrice)) {
                return false;
            }
            if (!Objects.equals(this.convenienceYield, other.convenienceYield)) {
                return false;
            }
            if (!Objects.equals(this.contractEntriesList, other.contractEntriesList)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "Entry{" + "spotPrice=" + spotPrice + ", convenienceYield=" + convenienceYield + ", contractEntriesList=" + contractEntriesList + "}\n";
        }

    }

    public static class ContractEntry {

        private final Double time2maturity;
        private final Double futuresPrice;

        public ContractEntry(Double time2maturity, Double futuresPrice) {
            Validate.notNull(time2maturity, NULL_ARGUMENT_ERROR_MESSAGE);
            Validate.notNull(futuresPrice, NULL_ARGUMENT_ERROR_MESSAGE);

            this.time2maturity = time2maturity;
            this.futuresPrice = futuresPrice;
        }

        public Double getTime2maturity() {
            return time2maturity;
        }

        public Double getFuturesPrice() {
            return futuresPrice;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + Objects.hashCode(this.time2maturity);
            hash = 83 * hash + Objects.hashCode(this.futuresPrice);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ContractEntry other = (ContractEntry) obj;
            if (!Objects.equals(this.time2maturity, other.time2maturity)) {
                return false;
            }
            if (!Objects.equals(this.futuresPrice, other.futuresPrice)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "ContractEntry{" + "time2maturity=" + time2maturity + ", futuresPrice=" + futuresPrice + '}';
        }

    }
}
