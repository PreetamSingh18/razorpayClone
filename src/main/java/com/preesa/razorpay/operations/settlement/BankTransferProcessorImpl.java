package com.preesa.razorpay.operations.settlement;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.preesa.razorpay.common.dto.SettlementBankDetails;
import com.preesa.razorpay.common.entity.Money;
import com.preesa.razorpay.common.util.RandomizerUtil;
import com.preesa.razorpay.operations.settlement.dto.BankTransferResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankTransferProcessorImpl implements BankTransferProcessor {

    @Override
    public BankTransferResult initiate(UUID settlementId, UUID merchantId, Money amount,
            SettlementBankDetails bankDetails) {
        // Call the Bank API

        String registrationRef = "TXN_" + RandomizerUtil.randomBase64(12);

        log.debug("Bank Transfer call completed for settlementId: {}, registrationRef: {}",
                settlementId, registrationRef);

        return new BankTransferResult(registrationRef);

    }

}
