package com.preesa.razorpay.operations.settlement;

import java.util.UUID;

import com.preesa.razorpay.common.dto.SettlementBankDetails;
import com.preesa.razorpay.common.entity.Money;
import com.preesa.razorpay.operations.settlement.dto.BankTransferResult;

public interface BankTransferProcessor {
    BankTransferResult initiate(UUID settlementId, UUID merchantId, Money amount, SettlementBankDetails bankDetails);

}
