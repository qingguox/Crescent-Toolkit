package io.github.qingguox.id.sequence.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import io.github.qingguox.id.sequence.IdRule;
import io.github.qingguox.id.sequence.IdSequenceClientService;
import io.github.qingguox.id.sequence.IdSequenceService;
import io.github.qingguox.id.sequence.dao.IdBizDAO;
import io.github.qingguox.id.sequence.model.IdBiz;

/**
 * @author wangqingwei
 * Created on 2022-08-18
 */
@Service
public class IdSequenceServiceImpl implements IdSequenceService {
    private static final Logger logger = LoggerFactory.getLogger(IdSequenceServiceImpl.class);

    @Autowired
    private IdSequenceServiceFactory idSequenceServiceFactory;

    @Autowired
    private IdBizDAO idBizDAO;

    @Override
    public long getId(String idBizType) {
        final IdBiz idBiz = idBizDAO.geByBizType(idBizType);
        Assert.notNull(idBiz, "idBiz is null! idBizType : " + idBizType);
        IdSequenceClientService idSequenceClientByRule =
                idSequenceServiceFactory.getIdSequenceClientByRule(IdRule.fromValue(idBiz.getRule()));
        return idSequenceClientByRule.getId(idBiz.getId());
    }
}
