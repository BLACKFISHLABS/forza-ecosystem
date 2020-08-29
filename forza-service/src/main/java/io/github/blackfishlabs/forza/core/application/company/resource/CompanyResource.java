package io.github.blackfishlabs.forza.core.application.company.resource;

import io.github.blackfishlabs.forza.core.application.address.converter.CityConverter;
import io.github.blackfishlabs.forza.core.application.address.json.CityJson;
import io.github.blackfishlabs.forza.core.application.address.service.AddressService;
import io.github.blackfishlabs.forza.core.application.address.service.CityService;
import io.github.blackfishlabs.forza.core.application.company.converter.CompanyConverter;
import io.github.blackfishlabs.forza.core.application.company.json.CompanyJson;
import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.core.application.company.service.CompanyService;
import io.github.blackfishlabs.forza.core.helper.EmailHelper;
import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@ApiIgnore
@RestController
@RequestMapping(ResourcePaths.COMPANY_PATH)
public class CompanyResource implements APIMap {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyConverter companyConverter;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CityService cityService;
    @Autowired
    private CityConverter cityConverter;

    @PostMapping
    public CompanyJson save(@RequestBody CompanyJson json) {
        String code = json.getAddressJson().getCityJson().getCode();
        CityJson cityJson = cityConverter.convertFrom(cityService.findByCode(Integer.parseInt(code)));
        json.getAddressJson().setCityJson(cityJson);

        CompanyEntity entity = companyService.insert(companyConverter.convertFrom(json));

        sendNotificationEmail(entity);
        return companyConverter.convertFrom(entity);
    }

    private void sendNotificationEmail(CompanyEntity entity) {
        String emailFrom = "noreply@mail.com";
        String emailTo = "dev.blackfishlabs@gmail.com";
        String subject = "FORZA SERVICE - NOVO CLIENTE!";

        String message = "<html>" +
                "<body>" +
                "Forza Service - Novo Cliente!" +
                "<br>" +
                "CNPJ: ".concat(entity.getCnpj()) +
                "<br>" +
                "Nome Empresarial: ".concat(entity.getCompanyName()) +
                "<br>" +
                "Nome Fantasia: ".concat(entity.getFantasyName()) +
                "<br>" +
                "<br>" +
                "<hr>" +
                "Não responda este e-mail." +
                "<br>" +
                "BLACKFISH LABS - http://www.blackfishlabs.com.br" +
                "<br>" +
                "</body>" +
                "</html>";
        EmailHelper.sendEmailSendGrid(emailFrom, emailTo, subject, message);
    }

    @DeleteMapping
    public void delete(@RequestBody CompanyJson json) {
        CompanyEntity companyEntity = companyService.findOne(json.getId());
        companyService.delete(companyEntity);
    }

    @PutMapping
    public void update(@RequestBody CompanyJson json) {
        String code = json.getAddressJson().getCityJson().getCode();
        CityJson cityJson = cityConverter.convertFrom(cityService.findByCode(Integer.parseInt(code)));
        json.getAddressJson().setCityJson(cityJson);

        companyService.update(companyConverter.convertFrom(json));
    }

    @GetMapping
    public List<CompanyJson> getAll() {
        return companyConverter.convertListModelFrom(companyService.findAll());
    }

    @GetMapping("/{id}")
    public CompanyJson findOne(@PathVariable Long id) {
        return companyConverter.convertFrom(companyService.findOne(id));
    }
}
