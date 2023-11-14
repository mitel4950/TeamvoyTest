package com.example.teamvoytest.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.teamvoytest.exception.InvalidDataException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class CommonValidationUtilsTest {

  @Test
  void validateUniqueIds_WithNoDuplicates_ShouldNotThrowException() {
    List<Long> ids = Arrays.asList(1L, 2L, 3L);
    assertDoesNotThrow(() -> CommonValidationUtils.validateUniqueIds(ids));
  }

  @Test
  void validateUniqueIds_WithDuplicates_ShouldThrowException() {
    List<Long> ids = Stream.of(1L, 2L, 2L, 3L).toList();
    assertThrows(InvalidDataException.class, () -> CommonValidationUtils.validateUniqueIds(ids));
  }
}
