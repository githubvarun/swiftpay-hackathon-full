package com.swiftpay.ledger_service.service;

import com.swiftpay.ledger_service.constants.LedgerConstants;
import com.swiftpay.ledger_service.dto.PaymentEvent;
import com.swiftpay.ledger_service.entity.Account;
import com.swiftpay.ledger_service.entity.LedgerEntry;
import com.swiftpay.ledger_service.entity.LedgerStatus;
import com.swiftpay.ledger_service.repository.AccountRepository;
import com.swiftpay.ledger_service.repository.LedgerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.swiftpay.ledger_service.dto.PaymentResultEvent;
import com.swiftpay.ledger_service.producer.PaymentResultProducer;

@Service
@RequiredArgsConstructor
public class LedgerProcessingService {

    private final PaymentResultProducer paymentResultProducer;
    private final AccountRepository accountRepository;
    private final LedgerRepository ledgerRepository;

    @Transactional
    public void processPayment(PaymentEvent event) {



        Account sender =
                accountRepository.findByUserId(event.getSenderId())
                        .orElseThrow();

        Account receiver =
                accountRepository.findByUserId(event.getReceiverId())
                        .orElseThrow();

        if(sender.getBalance().compareTo(event.getAmount()) < 0) {

            LedgerEntry failedEntry =
                    LedgerEntry.builder()
                            .transactionId(event.getTransactionId())
                            .senderId(event.getSenderId())
                            .receiverId(event.getReceiverId())
                            .amount(event.getAmount())
                            .currency(event.getCurrency())
                            .status(LedgerStatus.FAILED)
                            .createdAt(event.getCreatedAt())
                            .build();

            ledgerRepository.save(failedEntry);

            PaymentResultEvent resultEvent =
                    PaymentResultEvent.builder()
                            .transactionId(event.getTransactionId())
                            .status(LedgerConstants.FAILED)
                            .build();

            paymentResultProducer.publish(resultEvent);

            return;
        }

        sender.setBalance(
                sender.getBalance().subtract(event.getAmount())
        );

        receiver.setBalance(
                receiver.getBalance().add(event.getAmount())
        );

        accountRepository.save(sender);
        accountRepository.save(receiver);

        LedgerEntry entry =
                LedgerEntry.builder()
                        .transactionId(event.getTransactionId())
                        .senderId(event.getSenderId())
                        .receiverId(event.getReceiverId())
                        .amount(event.getAmount())
                        .currency(event.getCurrency())
                        .status(LedgerStatus.COMPLETED)
                        .createdAt(event.getCreatedAt())
                        .build();

        ledgerRepository.save(entry);

        PaymentResultEvent resultEvent =
                PaymentResultEvent.builder()
                        .transactionId(event.getTransactionId())
                        .status(LedgerConstants.COMPLETED)
                        .build();

        paymentResultProducer.publish(resultEvent);
    }
}