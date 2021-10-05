package com.manonero.ecommerce.services;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.manonero.ecommerce.entities.ProductEvaluation;
import com.manonero.ecommerce.entities.ProductPurchased;
import com.manonero.ecommerce.entities.UserAccount;
import com.manonero.ecommerce.models.ProductEvaluationRequest;
import com.manonero.ecommerce.repositories.IProductEvaluationRepository;
import com.manonero.ecommerce.repositories.IProductPurchasedRepository;
import com.manonero.ecommerce.repositories.IUserAccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductEvaluationService implements IProductEvaluationService {

    @Autowired
    private IProductEvaluationRepository evaluationRepository;
    @Autowired
    private IUserAccountRepository accountRepository;
    @Autowired
    private IProductPurchasedRepository purchasedRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private int numberEvaluation;

    @Override
    @Transactional
    public List<ProductEvaluation> filter(Integer offset, Integer limit, String productId, Integer userId) {
        List<ProductEvaluation> list = null;
        if (userId != null) {
            list = evaluationRepository.selectLevel1(productId, userId, true);
            List<ProductEvaluation> list2 = evaluationRepository.selectLevel1(productId, userId, false);
            list.addAll(list2);
        } else {
            list = evaluationRepository.selectLevel1(productId, userId, null);
        }
        if (list != null) {
            this.numberEvaluation = list.size();
            if (offset != null && limit != null) {
                int ix1 = offset - 1;
                int ix2 = offset + limit - 1;
                ix1 = ix1 >= 0 ? ix1 : 0;
                ix2 = ix2 >= 1 ? ix2 : 1;
                ix1 = this.numberEvaluation > ix1 ? ix1 : 0;
                ix2 = this.numberEvaluation > ix2 ? ix2 : this.numberEvaluation;
                List<ProductEvaluation> subList = list.subList(ix1, ix2);
                for (ProductEvaluation eval : subList) {
                    List<ProductEvaluation> childList = evaluationRepository.selectByParentId(eval.getId());
                    eval.setChildren(childList);
                }
                return subList;
            }
        }
        return list;
    }

    @Override
    @Transactional
    public ProductEvaluation add(ProductEvaluationRequest request) {
        UserAccount account = accountRepository.selectById(request.getUserId());
        if (account != null) {
            Date dateNow = new Date();
            ProductEvaluation productEvaluation = new ProductEvaluation();
            productEvaluation.setCreatedAt(dateNow);
            productEvaluation.setParentId(request.getParentId());
            productEvaluation.setContent(request.getContent());
            productEvaluation.setProductId(request.getProductId());
            productEvaluation.setStar(request.getStar());
            productEvaluation.setUserAccount(account);
            if (request.getParentId() == 0) {
                ProductPurchased purchased = new ProductPurchased();
                purchased.setUserId(request.getUserId());
                purchased.setProductId(request.getProductId());
                purchased.setHasEvaluated(true);
                purchasedRepository.save(purchased);
            }
            ProductEvaluation pEval = evaluationRepository.insert(productEvaluation);
            messagingTemplate.convertAndSend("/topic/productcomment/" + request.getProductId(), pEval);
            return pEval;
        }
        return null;
    }

    @Override
    public int getNumberEvaluation() {
        return numberEvaluation;
    }

    @Override
    @Transactional
    public ProductEvaluation edit(ProductEvaluationRequest request) {
        ProductEvaluation evaluation = new ProductEvaluation();
        evaluation.setId(request.getId());
        evaluation.setContent(request.getContent());
        evaluation.setStar(request.getStar());
        ProductEvaluation pEval = evaluationRepository.update(evaluation);
        messagingTemplate.convertAndSend("/topic/productcomment/" + request.getProductId(), pEval);
        return pEval;
    }

    @Override
    @Transactional
    public ProductEvaluation remove(int id) {
        ProductEvaluation evaluation = evaluationRepository.delete(id);
        if(evaluation != null) {
            if(evaluation.getParentId() == 0) {
                ProductPurchased purchased = new ProductPurchased();
                purchased.setUserId(evaluation.getUserAccount().getId());
                purchased.setProductId(evaluation.getProductId());
                purchased.setHasEvaluated(false);
                purchasedRepository.save(purchased);
            }
        }
        ProductEvaluation eval = new ProductEvaluation();
        eval.setCreatedAt(null);
        eval.setStar(evaluation.getStar());
        eval.setParentId(evaluation.getParentId());
        eval.setProductId(evaluation.getProductId());
        eval.setId(evaluation.getId());
        eval.setUserAccount(evaluation.getUserAccount());
        messagingTemplate.convertAndSend("/topic/productcomment/" + eval.getProductId(), eval);
        return evaluation;
    }

    @Override
    @Transactional
    public ProductEvaluation getById(int id) {
        return evaluationRepository.selectById(id);
    }

}
