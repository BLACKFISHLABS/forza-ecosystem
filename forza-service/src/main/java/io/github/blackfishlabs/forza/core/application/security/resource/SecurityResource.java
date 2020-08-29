package io.github.blackfishlabs.forza.core.application.security.resource;

import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@ApiIgnore
@RestController
@RequestMapping(ResourcePaths.LOGIN_PATH)
public class SecurityResource implements APIMap {

    @GetMapping
    public Principal user(Principal user) {
        return user;
    }
}
