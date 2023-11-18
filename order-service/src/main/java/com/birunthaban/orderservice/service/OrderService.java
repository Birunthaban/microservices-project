package com.birunthaban.orderservice.service;

import com.birunthaban.orderservice.dto.InventoryResponse;
import com.birunthaban.orderservice.dto.OrderLineItemsDto;
import com.birunthaban.orderservice.dto.OrderRequest;
import com.birunthaban.orderservice.model.Order;
import com.birunthaban.orderservice.model.OrderLineItems;
import com.birunthaban.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.databind.cfg.CoercionInputShape.Array;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository ;
    private final WebClient webClient ;
    public void placeOrder( OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList= orderRequest.getOrderLineItemsDtoList()
                .stream().map(this::mapToDto)
                .toList();


        order.setOrderLineItemsList(orderLineItemsList);

        List<String> skuCodes =order.getOrderLineItemsList().stream().map(orderLineItems -> orderLineItems.getSkuCode()).toList();

        //  check whether products are available
        InventoryResponse[] inventoryResponses = webClient.get().uri("http://localhost:8091/api/inventory",
                uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build()).
                retrieve().bodyToMono(InventoryResponse[].class).block();
   boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(inventoryResponse -> inventoryResponse.isInStock());
        if(allProductsInStock){
            orderRepository.save(order);
        }
        else{
            throw new IllegalArgumentException("Product is not in stock , try again latter ");
        }


    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemDto) {
       OrderLineItems orderLineItem =new OrderLineItems();
       orderLineItem.setPrice(orderLineItemDto.getPrice());
       orderLineItem.setQuantity(orderLineItemDto.getQuantity());
       orderLineItem.setSkuCode(orderLineItemDto.getSkuCode());
       return orderLineItem;
    }
}
