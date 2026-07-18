package com.preesa.razorpay.payment.statemachine;

import com.preesa.razorpay.common.enums.PaymentEvent;
import com.preesa.razorpay.common.enums.PaymentStatus;
import com.preesa.razorpay.common.exceptions.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentStateMachine {

    public static final Map<Transition, PaymentStatus> Transition = Map.ofEntries(
            Map.entry(new Transition(PaymentStatus.CREATED, PaymentEvent.AUTHORIZE_ATTEMPT), PaymentStatus.AUTHORIZING),
            Map.entry(new Transition(PaymentStatus.AUTHORIZING, PaymentEvent.AUTHORIZE_SUCCESS), PaymentStatus.AUTHORIZED),
            Map.entry(new Transition(PaymentStatus.AUTHORIZING, PaymentEvent.AUTHORIZE_FAIL), PaymentStatus.FAILED),
            Map.entry(new Transition(PaymentStatus.AUTHORIZED, PaymentEvent.CAPTURE_REQUEST), PaymentStatus.CAPTURING),
            Map.entry(new Transition(PaymentStatus.CAPTURING, PaymentEvent.CAPTURE_FAIL), PaymentStatus.AUTHORIZED),
            Map.entry(new Transition(PaymentStatus.CAPTURING, PaymentEvent.CAPTURE_SUCCESS), PaymentStatus.CAPTURED),
            Map.entry(new Transition(PaymentStatus.CAPTURED, PaymentEvent.REFUND_INIT), PaymentStatus.PARTIALLY_REFUNDED),
            Map.entry(new Transition(PaymentStatus.CAPTURED, PaymentEvent.REFUND_COMPLETE), PaymentStatus.REFUNDED),
            Map.entry(new Transition(PaymentStatus.CAPTURED, PaymentEvent.SETTLE), PaymentStatus.SETTLED),
            Map.entry(new Transition(PaymentStatus.SETTLED, PaymentEvent.REFUND_INIT), PaymentStatus.PARTIALLY_REFUNDED),
            Map.entry(new Transition(PaymentStatus.SETTLED, PaymentEvent.REFUND_COMPLETE), PaymentStatus.REFUNDED),
            Map.entry(new Transition(PaymentStatus.PARTIALLY_REFUNDED, PaymentEvent.REFUND_COMPLETE), PaymentStatus.REFUNDED),
            Map.entry(new Transition(PaymentStatus.CREATED, PaymentEvent.CANCEL), PaymentStatus.CANCELLED),
            Map.entry(new Transition(PaymentStatus.AUTHORIZING, PaymentEvent.CANCEL), PaymentStatus.CANCELLED),
            Map.entry(new Transition(PaymentStatus.AUTHORIZED, PaymentEvent.CAPTURE_TIMEOUT), PaymentStatus.AUTH_EXPIRED)

    );

    public record Transition(PaymentStatus from, PaymentEvent event) {
    }

    public PaymentStatus transition (PaymentStatus current, PaymentEvent event){
        if(Transition.get( new Transition(current,event))!= null){
            return Transition.get( new Transition(current,event));
        }
        else{
            throw new InvalidStateTransitionException(current.name(),event.name());
        }
    }


}
