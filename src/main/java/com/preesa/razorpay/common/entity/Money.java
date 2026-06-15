package com.preesa.razorpay.common.entity;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;


@Embeddable
public class Money {
    private Long amountUnits;
    private String currency;

    Money (){

    }

    private Money(Long amountUnits, String currency) {
        this.amountUnits=amountUnits;
        this.currency=currency;
    }

    Money of (Long amountUnits, String currency){
        return new Money(this.amountUnits,this.currency);
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
