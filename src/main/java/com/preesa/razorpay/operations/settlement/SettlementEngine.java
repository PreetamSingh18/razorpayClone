package com.preesa.razorpay.operations.settlement;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.preesa.razorpay.merchant.api.MerchantLookupService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Component 
@RequiredArgsConstructor 
public class SettlementEngine {

    private final MerchantLookupService merchantLookupService;
    private final SettlementTransactionExecutor settlementTransactionExecutor;

    @Scheduled(cron = "0 0 23 * * *")
    public void runScheduled() {
        log.info("Nightly settlement Scheduler running");
        run();
    }

    private void run() {

        List<UUID> merchantIds = merchantLookupService.getAllActiveMerchantIds();
        log.info("Processing the settlement for active merchants : {}", merchantIds.size());

        try (ExecutorService virtualExecutorService = Executors.newVirtualThreadPerTaskExecutor()) {

            List<Future<?>> futures = new ArrayList<>();

            for (UUID id : merchantIds) {
                virtualExecutorService.submit(() -> {
                  settlementTransactionExecutor.processForMerchant(id, Instant.now());
                });
            }

            for (Future future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {

                    log.error("settlement batch future failed");
                    throw new RuntimeException(e);
                }
            }

        }
        log.info("Settlement batch completed");

    }

}
