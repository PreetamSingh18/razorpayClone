package com.preesa.razorpay.payment.mapper;

import com.preesa.razorpay.payment.dto.response.OrderResponse;
import com.preesa.razorpay.payment.entity.OrderRecord;
import org.hibernate.query.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    @Mapping( target = "orderId", source = "id")
    OrderResponse toResponse(OrderRecord order);
}
