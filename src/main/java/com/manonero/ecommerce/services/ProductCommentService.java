package com.manonero.ecommerce.services;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.manonero.ecommerce.entities.ProductComment;
import com.manonero.ecommerce.entities.UserAccount;
import com.manonero.ecommerce.models.ProductCommentRequest;
import com.manonero.ecommerce.repositories.IProductCommentRepository;
import com.manonero.ecommerce.repositories.IUserAccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductCommentService implements IProductCommentService {

    @Autowired
    private IProductCommentRepository commentRepository;
    @Autowired
    private IUserAccountRepository accountRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private int numberComment;

    @Override
    @Transactional
    public List<ProductComment> filter(Integer offset, Integer limit, String productId, Integer userId) {
        List<ProductComment> list = null;
        if (userId != null) {
            list = commentRepository.selectLevel1(productId, userId, true);
            List<ProductComment> list2 = commentRepository.selectLevel1(productId, userId, false);
            list.addAll(list2);
        } else {
            list = commentRepository.selectLevel1(productId, userId, null);
        }
        if (list != null) {
            this.numberComment = list.size();
            if (offset != null && limit != null) {
                int ix1 = offset - 1;
                int ix2 = offset + limit - 1;
                ix1 = ix1 >= 0 ? ix1 : 0;
                ix2 = ix2 >= 1 ? ix2 : 1;
                ix1 = this.numberComment > ix1 ? ix1 : 0;
                ix2 = this.numberComment > ix2 ? ix2 : this.numberComment;
                List<ProductComment> subList = list.subList(ix1, ix2);
                for (ProductComment cmt : subList) {
                    List<ProductComment> childList = commentRepository.selectByParentId(cmt.getId());
                    cmt.setChildren(childList);
                }
                return subList;
            }
        }
        return list;
    }

    @Override
    @Transactional
    public ProductComment add(ProductCommentRequest request) {
        UserAccount account = accountRepository.selectById(request.getUserId());
        if (account != null) {
            Date dateNow = new Date();
            ProductComment productComment = new ProductComment();
            productComment.setCreatedAt(dateNow);
            productComment.setParentId(request.getParentId());
            productComment.setContent(request.getContent());
            productComment.setProductId(request.getProductId());
            productComment.setUserAccount(account);
            ProductComment cmt = commentRepository.insert(productComment);
            messagingTemplate.convertAndSend("/topic/productcomment/" + request.getProductId(), cmt);
            return cmt;
        }
        return null;
    }

    @Override
    @Transactional
    public ProductComment edit(ProductCommentRequest request) {
        ProductComment cmt = new ProductComment();
        cmt.setId(request.getId());
        cmt.setContent(request.getContent());
        ProductComment comment = commentRepository.update(cmt);
        messagingTemplate.convertAndSend("/topic/productcomment/" + request.getProductId(), comment);
        return comment;
    }

    @Override
    @Transactional
    public ProductComment remove(int id) {
        ProductComment comment = commentRepository.delete(id);
        ProductComment cmt = new ProductComment();
        cmt.setCreatedAt(null);
        cmt.setParentId(comment.getParentId());
        cmt.setProductId(comment.getProductId());
        cmt.setId(comment.getId());
        cmt.setUserAccount(comment.getUserAccount());
        messagingTemplate.convertAndSend("/topic/productcomment/" + cmt.getProductId(), cmt);
        return comment;
    }

    @Override
    public int getNumberComment() {
        return numberComment;
    }

    @Override
    @Transactional
    public ProductComment getById(int id) {
        return commentRepository.selectById(id);
    }
    
}
