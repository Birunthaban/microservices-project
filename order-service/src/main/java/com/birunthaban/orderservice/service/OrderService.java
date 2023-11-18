package com.birunthaban.orderservice.service;

import com.birunthaban.orderservice.dto.OrderLineItemsDto;
import com.birunthaban.orderservice.dto.OrderRequest;
import com.birunthaban.orderservice.model.Order;
import com.birunthaban.orderservice.model.OrderLineItems;
import com.birunthaban.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository ;
    public void placeOrder( OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList= orderRequest.getOrderLineItemsDtoList()
                .stream().map(this::mapToDto)
                .toList();


        order.setOrderLineItemsList(orderLineItemsList);
        orderRepository.save(order);

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemDto) {
       OrderLineItems orderLineItem =new OrderLineItems();
       orderLineItem.setPrice(orderLineItemDto.getPrice());
       orderLineItem.setQuantity(orderLineItemDto.getQuantity());
       orderLineItem.setSkuCode(orderLineItemDto.getSkuCode());
       return orderLineItem;
    }
}
