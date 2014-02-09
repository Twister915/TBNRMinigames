package net.tbnr.commerce.items;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PurchaseResult {
    private final CommerceItemAPI.PurchaseMethod purchaseMethod;
    private final Integer amount;
    private final boolean successful;
}
