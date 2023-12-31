package com.birunthaban.inventoryservice.service;

import com.birunthaban.inventoryservice.dto.InventoryResponse;
import com.birunthaban.inventoryservice.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j

public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCode)  {
        log.info("Wait started");
        Thread.sleep(10000);
        log.info("Wait Ended");
        return inventoryRepository.findBySkuCodeIn(skuCode).stream().map(inventory ->

            InventoryResponse.builder()
                    .skuCode(inventory.getSkuCode())
                    .isInStock(inventory.getQuantity() > 0)
                    .build()
        ).toList();

    }
}