package com.preesa.razorpay.common.entity;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Embeddable
@Getter
@Setter
@ToString 
public class Money {
    private Long amountUnits;
    private String currency;

    Money (){

    }

    private Money(Long amountUnits, String currency) {
        this.amountUnits=amountUnits;
        this.currency=currency;
    }

    public static Money of (Long amountUnits, String currency){
        return new Money(amountUnits,currency);
    }



    public Money add(Money other){
        if(! other.currency.equals(this.currency)){
            throw new IllegalArgumentException("Invalid currency match");

        }
        return new Money(this.amountUnits+other.amountUnits,this.currency);
    }
    public Money subtract(Money other){
        if(! other.currency.equals(this.currency)){
            throw new IllegalArgumentException("Invalid currency match");

        }
        return new Money(this.amountUnits - other.amountUnits,this.currency);
    }
}
