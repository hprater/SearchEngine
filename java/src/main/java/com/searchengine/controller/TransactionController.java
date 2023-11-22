package com.searchengine.controller;

import com.searchengine.dao.TransactionDao;
import com.searchengine.model.Transaction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;


@RestController
@PreAuthorize("isAuthenticated()")
@CrossOrigin
public class TransactionController {
    private TransactionDao botTransactionDao;

    public TransactionController(TransactionDao botTransactionDao) {
        this.botTransactionDao = botTransactionDao;
    }

    @RequestMapping(path = "/question", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_USER')")
    public String createTransaction(@Valid @RequestBody Transaction transaction) throws IOException {
        Transaction botMethod = new Transaction();
        return botMethod.getResponse(transaction.getQuestionOrResponse(), botTransactionDao.getQuestionAnswerMap());
    }
}

