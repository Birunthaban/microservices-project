package com.birunthaban.inventoryservice.service;

import com.birunthaban.inventoryservice.dto.InventoryResponse;
import com.birunthaban.inventoryservice.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor

public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional
    public List<InventoryResponse> isInStock(List<String> skuCode) {

        return inventoryRepository.findBySkuCodeIn(skuCode).stream().map(inventory ->

            InventoryResponse.builder()
                    .skuCode(inventory.getSkuCode())
                    .isInStock(inventory.getQuantity() > 0)
                    .build()
        ).toList();

    }
}