package com.example.cryptotracker.portfolio;

import com.example.cryptotracker.security.SecurityUtilis;
import com.example.cryptotracker.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public void save(Transaction transaction) {
        transaction.setUser(SecurityUtilis.getUserFromSecurityContext());

        transactionRepository.save(transaction);
    }

    public List<Transaction> findAllByRequestUser() {
        User requestUser = SecurityUtilis.getUserFromSecurityContext();
        return transactionRepository.findAllByUser(requestUser);
    }

    public void deleteById(Integer transactionId) {
        transactionRepository.deleteById(transactionId);

    }
}
