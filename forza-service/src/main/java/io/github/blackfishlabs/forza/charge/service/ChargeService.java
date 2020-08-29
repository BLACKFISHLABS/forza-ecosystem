package io.github.blackfishlabs.forza.charge.service;

import com.google.common.base.Strings;
import io.github.blackfishlabs.forza.charge.model.ChargeEntity;
import io.github.blackfishlabs.forza.charge.model.ChargeStatus;
import io.github.blackfishlabs.forza.charge.repository.ChargeRepository;
import io.github.blackfishlabs.forza.charge.repository.ChargeSpecification;
import io.github.blackfishlabs.forza.core.application.search.FilterSearchParam;
import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import io.github.blackfishlabs.forza.core.infra.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@EnableScheduling
public class ChargeService extends GenericService<ChargeEntity, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeService.class);

    @Autowired
    private ChargeRepository chargeRepository;

    public ChargeEntity findOne(Long id) {
        return chargeRepository.getOne(id);
    }

    public List<ChargeEntity> search(FilterSearchParam filter, String cnpj) {
        List<ChargeEntity> response;

        if (!Strings.isNullOrEmpty(filter.getDescription()))
            response = chargeRepository.findAll(ChargeSpecification.findByName(filter.getDescription(), cnpj));
        else
            response = chargeRepository.findAll(ChargeSpecification.findByCompany(cnpj));

        response.sort(Comparator.comparing(BasicEntity::getCreatedAt));
        Collections.reverse(response);

        return response;
    }

    public ChargeEntity searchByCharge(String code, String document, String salesman) {
        List<ChargeEntity> response;

        response = chargeRepository.findAll(ChargeSpecification.findByCodeAndSalesman(code, document, salesman));

        response.sort(Comparator.comparing(BasicEntity::getCreatedAt));
        Collections.reverse(response);

        return response.isEmpty() ? null : response.iterator().next();
    }

    public ChargeEntity searchByChargeCode(String code, String document) {
        List<ChargeEntity> response;

        response = chargeRepository.findAll(ChargeSpecification.findByCodeAndCompany(code, document));

        response.sort(Comparator.comparing(BasicEntity::getCreatedAt));
        Collections.reverse(response);

        return response.isEmpty() ? null : response.iterator().next();
    }

    @Scheduled(fixedDelay = 86400000, initialDelay = 60000)
    public void getOlderResumeSyncAndRemove() {
        for (ChargeEntity charge : chargeRepository.findAll(ChargeSpecification.findByStatus(ChargeStatus.STATUS_SYNCED))) {
            int daysDiff = getDifferenceDays(charge.getIssueDate(), Calendar.getInstance().getTime());
            LOGGER.info("AUTO DELETE OLDER CHARGE STATUS_SYNCED: " + daysDiff);
            if (daysDiff > 30) {
                LOGGER.warn("AUTO DELETE OLDER CHARGE: " + charge.getCode());
                chargeRepository.delete(charge);
            }
        }
    }

    @Scheduled(fixedDelay = 86400000, initialDelay = 60000)
    public void getOlderResumeFinishedAndRemove() {
        for (ChargeEntity charge : chargeRepository.findAll(ChargeSpecification.findByStatus(ChargeStatus.STATUS_FINISHED))) {
            int daysDiff = getDifferenceDays(charge.getIssueDate(), Calendar.getInstance().getTime());
            LOGGER.info("AUTO DELETE OLDER CHARGE STATUS_FINISHED: " + daysDiff);
            if (daysDiff > 7) {
                LOGGER.warn("AUTO DELETE OLDER CHARGE: " + charge.getCode());
                chargeRepository.delete(charge);
            }
        }
    }

    @Scheduled(fixedDelay = 86400000, initialDelay = 60000)
    public void getOlderResumeCreatedAndRemove() {
        for (ChargeEntity charge : chargeRepository.findAll(ChargeSpecification.findByStatus(ChargeStatus.STATUS_CREATED))) {
            int daysDiff = getDifferenceDays(charge.getIssueDate(), Calendar.getInstance().getTime());
            LOGGER.info("AUTO DELETE OLDER CHARGE STATUS_CREATED: " + daysDiff);
            if (daysDiff > 30) {
                LOGGER.warn("AUTO DELETE OLDER CHARGE: " + charge.getCode());
                chargeRepository.delete(charge);
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
