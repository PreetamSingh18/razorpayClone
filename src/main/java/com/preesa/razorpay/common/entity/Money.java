package com.preesa.razorpay.common.entity;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;


@Embeddable
public class Money {
    private Long amountUnit;
    private String currency;

    Money (){

    }

    private Money(Long amountUnit, String currency) {
        this.amountUnit=amountUnit;
        this.currency=currency;
    }

    Money of (Long amountUnit, String currency){
        return new Money(this.amountUnit,this.currency);
    }



    public Money add(Money other){
        if(! other.currency.equals(this.currency)){
            throw new IllegalArgumentException("Invalid currency match");

        }
        return new Money(this.amountUnit+other.amountUnit,this.currency);
    }
    public Money subtract(Money other){
        if(! other.currency.equals(this.currency)){
            throw new IllegalArgumentException("Invalid currency match");

        }
        return new Money(this.amountUnit - other.amountUnit,this.currency);
    }
}
