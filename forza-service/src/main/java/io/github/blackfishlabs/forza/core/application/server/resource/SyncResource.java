package io.github.blackfishlabs.forza.core.application.server.resource;


import io.github.blackfishlabs.forza.core.application.server.json.ServerJson;
import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(ResourcePaths.SERVER_PATH)
public class SyncResource implements APIMap {

    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    @GetMapping
    public ServerJson serverStatus() {
        ServerJson json = new ServerJson();
        json.setCurrentTime(new SimpleDateFormat(FORMAT).format(new Date()));

        return json;
    }
}
