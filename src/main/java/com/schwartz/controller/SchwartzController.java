package com.schwartz.controller;

import com.schwartz.business.ISchwartzCalculatorService;
import com.schwartz.model.SchwartzModelParameters;
import com.schwartz.model.SchwartzSimulatedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * A controller for the visualizer - page.
 * 
 * @author woope
 */
@RestController
public class SchwartzController {
    private final Logger log = LogManager.getLogger(SchwartzController.class.getName());

    @Autowired
    ISchwartzCalculatorService<SchwartzSimulatedData> schwartzCalculatorService;

    /**
     * TODO: FIXME: Make the interface callable in order not choke up the thread pool. 
     */
    @RequestMapping(method = GET, path = "paths")
    public SchwartzSimulatedData startSamplePathSimulation(
        @ModelAttribute SchwartzModelParameters parameters,
        @RequestParam Double initialSpot, 
        @RequestParam Double initialConvenienceYield
    ) throws Exception {
        try {
            return schwartzCalculatorService.run(initialSpot, initialConvenienceYield, parameters, true).call();
        } catch (Exception ex) {
            log.error("Could not intiate the calculation!");
            throw ex;
        }
    }
}
