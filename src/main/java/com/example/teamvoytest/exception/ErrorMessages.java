package com.example.teamvoytest.exception;

public class ErrorMessages {

  private ErrorMessages() {
    throw new IllegalStateException("Utility class");
  }

  public static final String PRODUCT_NOT_FOUND = "Product with id: %d not found";
  public static final String PRODUCTS_NOT_FOUND = "Products with id: %s not found";
  public static final String INSUFFICIENT_AMOUNT_OF_PRODUCTS = "Insufficient amount of products with id: %s in stock";
  public static final String PRODUCTS_ARE_NOT_AVAILABLE = "Products with ids: %s are either not AVAILABLE or out of stock.";
  public static final String PRODUCT_IDS_ARE_NOT_FOUND = "The following product IDs are not found: %s";
  public static final String ORDER_NOT_FOUND = "Order with id: %d not found";
  public static final String ORDER_IS_COMPLETED = "Order with id: %d is already completed";
  public static final String ORDER_IS_FAILED = "Order with id: %d is failed";
  public static final String DATA_CONTAINS_DUPLICATE_IDS = "The provided data contains duplicate IDs: %s";

}
