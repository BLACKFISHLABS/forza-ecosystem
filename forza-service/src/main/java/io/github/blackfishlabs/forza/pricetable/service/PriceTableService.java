package io.github.blackfishlabs.forza.pricetable.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.application.search.FilterSearchParam;
import io.github.blackfishlabs.forza.core.helper.FormattingUtils;
import io.github.blackfishlabs.forza.core.infra.service.GenericService;
import io.github.blackfishlabs.forza.pricetable.model.PriceTableEntity;
import io.github.blackfishlabs.forza.pricetable.repository.PriceTableRepository;
import io.github.blackfishlabs.forza.pricetable.repository.PriceTableSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@EnableScheduling
public class PriceTableService extends GenericService<PriceTableEntity, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceTableService.class);

    @Autowired
    private PriceTableRepository priceTableRepository;

    public List<PriceTableEntity> findByCompany(String cnpj) {
        List<PriceTableEntity> response = Lists.newArrayList();

        response.addAll(priceTableRepository.findAll(PriceTableSpecification.findByCompany(cnpj)));
        response.sort(Comparator.comparing(PriceTableEntity::getCreatedAt));
        Collections.reverse(response);

        return response;
    }

    public List<PriceTableEntity> findByCompanyAndCharge(String cnpj, String code) {
        List<PriceTableEntity> response = Lists.newArrayList();

        response.addAll(priceTableRepository.findByCompanyAndCharge(FormattingUtils.formatCpforCnpj(cnpj), code));
        response.sort(Comparator.comparing(PriceTableEntity::getCreatedAt));
        Collections.reverse(response);

        return response;
    }

    public List<PriceTableEntity> findByCompanyAndLastUpdate(String cnpj, String lastUpdateTime) {
        List<PriceTableEntity> response = Lists.newArrayList();

        response.addAll(priceTableRepository.findAll(PriceTableSpecification.findByCompanyAndLastUpdate(cnpj, lastUpdateTime)));
        response.sort(Comparator.comparing(PriceTableEntity::getCreatedAt));
        Collections.reverse(response);

        return response;
    }

    public List<PriceTableEntity> search(FilterSearchParam filter, String cnpj) {
        List<PriceTableEntity> response = Lists.newArrayList();

        if (!Strings.isNullOrEmpty(filter.getDescription()))
            response.addAll(findByParamAndCompany(filter.getDescription(), cnpj));
        else
            response.addAll(priceTableRepository.findAll(PriceTableSpecification.findByCompany(cnpj)));

        response.sort(Comparator.comparing(PriceTableEntity::getCreatedAt));
        Collections.reverse(response);

        return response;
    }

    public List<PriceTableEntity> findByParamAndCompany(String param, String cnpj) {
        List<PriceTableEntity> response = Lists.newArrayList();

        response.addAll(priceTableRepository.findAll(PriceTableSpecification.findByParamAndCompany(param, cnpj)));
        response.sort(Comparator.comparing(PriceTableEntity::getCreatedAt));
        Collections.reverse(response);

        return response;
    }

    public PriceTableEntity findOne(Long id) {
        return priceTableRepository.getOne(id);
    }

    @Scheduled(fixedDelay = 86400000, initialDelay = 60000)
    public void getOlderPriceTableAndRemove() {
        for (PriceTableEntity priceTable : priceTableRepository.findAll()) {
            int daysDiff = getDifferenceDays(priceTable.getLastChangeTime(), Calendar.getInstance().getTime());
            LOGGER.info("AUTO DELETE OLDER PRICE TABLE DAYS: " + daysDiff);
            if (daysDiff > 30) {
                LOGGER.warn("AUTO DELETE OLDER PRICE TABLE: " + priceTable.getCode());
                priceTableRepository.delete(priceTable);
            }
        }
    }

    private static int getDifferenceDays(Date date1, Date date2) {
        int daysDiff;
        long diff = date2.getTime() - date1.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
        daysDiff = (int) diffDays;
        return daysDiff;
    }
}
