package io.github.clarenced.tasktime.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class ResultTest {

    @Test
    @DisplayName("Should create a success result with a value")
    void should_create_success_result_with_value() {
        Result<String, String> result = Result.success("Success");
        
        assertTrue(result.isSuccess());
        assertFalse(result.isError());
        assertEquals("Success", result.getSuccess());
        assertThrows(IllegalStateException.class, result::getError);
    }

    @Test
    @DisplayName("Should create a success result with no value")
    void should_create_success_result_with_no_value() {
        Result<String, String> result = Result.success();
        
        assertTrue(result.isSuccess());
        assertFalse(result.isError());
        assertNull(result.getSuccess());
        assertThrows(IllegalStateException.class, result::getError);
    }

    @Test
    @DisplayName("Should create an error result")
    void should_create_error_result() {
        Result<String, String> result = Result.error("Error");
        
        assertFalse(result.isSuccess());
        assertTrue(result.isError());
        assertEquals("Error", result.getError());
        assertThrows(IllegalStateException.class, result::getSuccess);
    }

    @Test
    @DisplayName("Should map success value")
    void should_map_success_value() {
        Result<String, String> result = Result.success("Success");
        Result<Integer, String> mappedResult = result.map(String::length);
        
        assertTrue(mappedResult.isSuccess());
        assertEquals(7, mappedResult.getSuccess());
    }

    @Test
    @DisplayName("Should not map error value")
    void should_not_map_error_value() {
        Result<String, String> result = Result.error("Error");
        Result<Integer, String> mappedResult = result.map(String::length);
        
        assertTrue(mappedResult.isError());
        assertEquals("Error", mappedResult.getError());
    }

    @Test
    @DisplayName("Should execute onSuccess consumer for success result")
    void should_execute_on_success_consumer_for_success_result() {
        AtomicBoolean executed = new AtomicBoolean(false);
        AtomicReference<String> value = new AtomicReference<>();
        
        Result<String, String> result = Result.success("Success");
        result.onSuccess(s -> {
            executed.set(true);
            value.set(s);
        });
        
        assertTrue(executed.get());
        assertEquals("Success", value.get());
    }

    @Test
    @DisplayName("Should not execute onSuccess consumer for error result")
    void should_not_execute_on_success_consumer_for_error_result() {
        AtomicBoolean executed = new AtomicBoolean(false);
        
        Result<String, String> result = Result.error("Error");
        result.onSuccess(s -> executed.set(true));
        
        assertFalse(executed.get());
    }

    @Test
    @DisplayName("Should execute onError consumer for error result")
    void should_execute_on_error_consumer_for_error_result() {
        AtomicBoolean executed = new AtomicBoolean(false);
        AtomicReference<String> value = new AtomicReference<>();
        
        Result<String, String> result = Result.error("Error");
        result.onError(e -> {
            executed.set(true);
            value.set(e);
        });
        
        assertTrue(executed.get());
        assertEquals("Error", value.get());
    }

    @Test
    @DisplayName("Should not execute onError consumer for success result")
    void should_not_execute_on_error_consumer_for_success_result() {
        AtomicBoolean executed = new AtomicBoolean(false);
        
        Result<String, String> result = Result.success("Success");
        result.onError(e -> executed.set(true));
        
        assertFalse(executed.get());
    }
}