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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.swiftpay.ledger_service.dto.PaymentResultEvent;
import com.swiftpay.ledger_service.producer.PaymentResultProducer;

@Service
@RequiredArgsConstructor
@Slf4j
public class LedgerProcessingService {

    private final PaymentResultProducer paymentResultProducer;
    private final AccountRepository accountRepository;
    private final LedgerRepository ledgerRepository;

    @Transactional
    public void processPayment(PaymentEvent event) {

        try {

            log.info("Starting ledger processing for transaction {}", event.getTransactionId());

            log.info("Payment details: senderId={}, receiverId={}, amount={}, currency={}", event.getSenderId(), event.getReceiverId(), event.getAmount(), event.getCurrency());

            Account sender =
                    accountRepository.findByUserId(event.getSenderId())
                            .orElseThrow();

            Account receiver =
                    accountRepository.findByUserId(event.getReceiverId())
                            .orElseThrow();

            log.debug("Accounts loaded successfully. Sender={}, Receiver={}", sender.getUserId(), receiver.getUserId());

            log.info("Validating sender balance. Sender={}, AvailableBalance={}, RequestedAmount={}", sender.getUserId(), sender.getBalance(), event.getAmount());

            if (sender.getBalance().compareTo(event.getAmount()) < 0) {

                log.warn("Insufficient balance for transaction {}. Sender={}, Balance={}, Requested={}", event.getTransactionId(), sender.getUserId(), sender.getBalance(), event.getAmount());

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

                log.info("Failed ledger entry saved for transaction {}", event.getTransactionId());

                PaymentResultEvent resultEvent =
                        PaymentResultEvent.builder()
                                .transactionId(event.getTransactionId())
                                .status(LedgerConstants.FAILED)
                                .build();

                log.info("Publishing FAILED payment result event for transaction {}", event.getTransactionId());

                paymentResultProducer.publish(resultEvent);

                return;
            }

            sender.setBalance(
                    sender.getBalance().subtract(event.getAmount())
            );

            receiver.setBalance(
                    receiver.getBalance().add(event.getAmount())
            );

            log.info("Balances updated. Sender={} NewBalance={}, Receiver={} NewBalance={}", sender.getUserId(), sender.getBalance(), receiver.getUserId(), receiver.getBalance());

            accountRepository.save(sender);
            accountRepository.save(receiver);

            log.info("Account balances persisted successfully for transaction {}", event.getTransactionId());

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

            log.info("Ledger entry saved successfully for transaction {}", event.getTransactionId());

            PaymentResultEvent resultEvent =
                    PaymentResultEvent.builder()
                            .transactionId(event.getTransactionId())
                            .status(LedgerConstants.COMPLETED)
                            .build();

            log.info("Publishing COMPLETED payment result event for transaction {}", event.getTransactionId());

            paymentResultProducer.publish(resultEvent);

            log.info("Ledger processing completed successfully for transaction {}", event.getTransactionId());

        } catch (Exception ex) {

            log.error("Error while processing transaction {}", event.getTransactionId(), ex);
            throw ex;
        }
    }
}