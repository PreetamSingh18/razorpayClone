package com.preesa.razorpay.vault.serviceImpl;

import com.preesa.razorpay.common.entity.Money;
import com.preesa.razorpay.common.enums.CardBrand;
import com.preesa.razorpay.common.exceptions.ResourceNotFoundException;
import com.preesa.razorpay.common.util.RandomizerUtil;
import com.preesa.razorpay.payment.processor.PaymentProcessorRouter;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.preesa.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.preesa.razorpay.vault.config.VaultEncryptionConfig;
import com.preesa.razorpay.vault.dto.request.TokenizerRequest;
import com.preesa.razorpay.vault.dto.response.TokenizerResponse;
import com.preesa.razorpay.vault.entity.CardToken;
import com.preesa.razorpay.vault.entity.VaultCard;
import com.preesa.razorpay.vault.repository.CardTokenRepository;
import com.preesa.razorpay.vault.repository.VaultCardRepository;
import com.preesa.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class VaultServiceImpl implements VaultService {

    private final CardTokenRepository cardTokenRepository;
    private final VaultCardRepository vaultCardRepository;
    private final BytesEncryptor dekEncryptor;
    private final PaymentProcessorRouter paymentProcessorRouter;

    /**
     * @param request
     * @param merchantId
     * @return
     */
    @Override
    public TokenizerResponse tokenizer(TokenizerRequest request, UUID merchantId) {
        String lastFour = request.pan().substring(request.pan().length() - 4);
        String bin = request.pan().substring(0, 6);
        CardBrand cardBrand = detectBrand(request.pan());

        byte[] dek= KeyGenerators.secureRandom(32).generateKey();
        byte[] encryptedPan = VaultEncryptionConfig.panEncryptor(dek)
                .encrypt(request.pan().getBytes(StandardCharsets.UTF_8));

        byte[] encryptedDek = dekEncryptor.encrypt(dek);
        VaultCard card = VaultCard.builder()
                .lastFour(lastFour)
                .bin(bin)
                .encryptedDek(encryptedDek)
                .encryptedPan(encryptedPan)
                .brand(cardBrand)
                .cardHolderName(request.cardHolderName())
                .expiryMonth(request.expiryMonth().toString())
                .expiryYear(request.expiryYear().toString())
                .build();
        vaultCardRepository.save(card);

        String token = "tok_"+ RandomizerUtil.randomBase64(32);

        CardToken cardToken= CardToken.builder()
                        .vaultCard(card)
                        .token(token)
                        .merchantId(merchantId)
                        .customerId(request.customerId())
                       .build();

        cardTokenRepository.save(cardToken);


        return new TokenizerResponse(token,cardBrand,lastFour,Integer.parseInt(request.expiryMonth()),request.expiryYear());
    }

    /**
     * @param paymentId
     * @param token
     * @param amount
     * @param methodDetails
     * @return
     */
    @Override
    public PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails) {
    CardToken cardToken =  cardTokenRepository.findByTokenAndRevokedAtNull(token)
            .orElseThrow(()-> new ResourceNotFoundException("CardToken",token));

    VaultCard vaultCard= cardToken.getVaultCard();

    byte[] panByte= null;

    try {
        byte[] dek = dekEncryptor.decrypt(vaultCard.getEncryptedDek());
        panByte = VaultEncryptionConfig.panEncryptor(dek).decrypt(vaultCard.getEncryptedPan());

        String pan = new String(panByte, StandardCharsets.UTF_8);
        String expiry = vaultCard.getExpiryMonth() + "/" + vaultCard.getExpiryYear();

        PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest.card(paymentId, pan, expiry, amount, methodDetails);
      PaymentProcessorResponse response =  paymentProcessorRouter.charge(paymentProcessorRequest);
        log.info("vault change registered with token {}***",token.substring(0,4));
        pan=null;
      return response;
    }
    catch (Exception e){
        log.warn("Error occured at Vault charge");
         return   new PaymentProcessorResponse.Failure("VAULT_CHARGE_FAILED",e.getMessage());
    }
    finally {
        if (panByte!= null) Arrays.fill(panByte, (byte) 0);

    }

    }

    private CardBrand detectBrand(String pan) {
        if (pan.startsWith("4")) return CardBrand.VISA;
        if (pan.startsWith("5") || pan.startsWith("2")) return CardBrand.MASTERCARD;
        if (pan.startsWith("37") || pan.startsWith("34")) return CardBrand.AMEX;
        return CardBrand.RUPAY;
    }
}
