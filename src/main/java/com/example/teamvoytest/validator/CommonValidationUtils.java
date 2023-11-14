package com.example.teamvoytest.validator;

import static com.example.teamvoytest.exception.ErrorMessages.DATA_CONTAINS_DUPLICATE_IDS;

import com.example.teamvoytest.exception.InvalidDataException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommonValidationUtils {

  private CommonValidationUtils() {
    throw new IllegalStateException("Utility class");
  }

  public static void validateUniqueIds(List<Long> ids){
    Set<Long> uniqueIds = new HashSet<>(ids);
    if (uniqueIds.size() != ids.size()) {
      String repeatingIdsString = ids.stream()
          .filter(ids::contains)
          .distinct()
          .map(String::valueOf)
          .collect(Collectors.joining(", "));
      throw new InvalidDataException(DATA_CONTAINS_DUPLICATE_IDS.formatted(repeatingIdsString));
    }
  }

}
