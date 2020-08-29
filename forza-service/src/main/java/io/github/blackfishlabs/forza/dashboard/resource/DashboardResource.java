package io.github.blackfishlabs.forza.dashboard.resource;

import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import io.github.blackfishlabs.forza.dashboard.json.DashboardJson;
import io.github.blackfishlabs.forza.dashboard.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ResourcePaths.DASHBOARD_PATH)
public class DashboardResource implements APIMap {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardResource.class);

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public DashboardJson calculateDashboard(@RequestParam("cnpj") String cnpj) {
        LOGGER.info("call method calculateDashboard()");
        return dashboardService.calculate(cnpj);
    }
}
