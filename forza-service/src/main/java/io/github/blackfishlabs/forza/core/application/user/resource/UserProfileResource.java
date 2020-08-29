package io.github.blackfishlabs.forza.core.application.user.resource;

import io.github.blackfishlabs.forza.core.application.user.converter.UserProfileConverter;
import io.github.blackfishlabs.forza.core.application.user.json.UserProfileJson;
import io.github.blackfishlabs.forza.core.application.user.service.UserProfileService;
import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@ApiIgnore
@RestController
@RequestMapping(ResourcePaths.PERMISSION_PATH)
public class UserProfileResource implements APIMap {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserProfileConverter userProfileConverter;

    @GetMapping
    public List<UserProfileJson> findAll() {
        return userProfileConverter.convertListModelFrom(userProfileService.findAll().stream().filter(p -> !p.getRole().equals("ROLE_ADMIN")).collect(Collectors.toList()));
    }

    @PostMapping
    public UserProfileJson insert(@RequestBody UserProfileJson userProfileJson) {
        return userProfileConverter.convertFrom(userProfileService.insert(userProfileConverter.convertFrom(userProfileJson)));
    }

    @PutMapping
    public void update(@RequestBody UserProfileJson userProfileJson) {
        userProfileService.update(userProfileConverter.convertFrom(userProfileJson));
    }

    @DeleteMapping
    public void delete(@RequestBody UserProfileJson userProfileJson) {
        userProfileService.delete(userProfileConverter.convertFrom(userProfileJson));
    }
}
