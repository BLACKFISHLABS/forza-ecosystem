package io.github.blackfishlabs.forza.core.application.user.resource;

import io.github.blackfishlabs.forza.core.application.user.converter.UserConverter;
import io.github.blackfishlabs.forza.core.application.user.json.UserJson;
import io.github.blackfishlabs.forza.core.application.user.service.UserService;
import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Base64;
import java.util.List;

@ApiIgnore
@RestController
@RequestMapping(ResourcePaths.USER_PATH)
public class UserResource implements APIMap {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/{ssoId}", method = RequestMethod.GET)
    public UserJson findOne(@PathVariable String ssoId) {
        return userConverter.convertFrom(userService.findOne(ssoId));
    }

    @GetMapping
    public List<UserJson> findAll(@RequestParam("cnpj") String cnpj) {
        return userConverter.convertListModelFrom(userService.findUserByCompany(cnpj));
    }

    @PostMapping
    public UserJson insert(@RequestBody UserJson userJson) {
        String encodeStr = userJson.getSsoId().concat(":").concat(userJson.getPassword());
        byte[] encode = Base64.getEncoder().encode(encodeStr.getBytes());

        userJson.setApiKey(new String(encode));
        userJson.setPassword(passwordEncoder.encode(userJson.getPassword()));
        return userConverter.convertFrom(userService.insert(userConverter.convertFrom(userJson)));
    }

    @PutMapping
    public void update(@RequestBody UserJson userJson) {
        String encodeStr = userJson.getSsoId().concat(":").concat(userJson.getPassword());
        byte[] encode = Base64.getEncoder().encode(encodeStr.getBytes());

        userJson.setApiKey(new String(encode));
        userJson.setPassword(passwordEncoder.encode(userJson.getPassword()));
        userService.update(userConverter.convertFrom(userJson));
    }

    @DeleteMapping
    public void delete(@RequestBody UserJson userJson) {
        userService.delete(userConverter.convertFrom(userJson));
    }
}
