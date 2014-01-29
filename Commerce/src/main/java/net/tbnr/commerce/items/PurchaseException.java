package net.tbnr.commerce.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public class PurchaseException extends Exception {
    @Getter @NonNull private final String message;
}
