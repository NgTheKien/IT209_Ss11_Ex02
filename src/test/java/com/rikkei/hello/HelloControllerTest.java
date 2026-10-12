package com.rikkei.hello;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HelloControllerTest {

    @Test
    void helloReturnsHelloWorld() {
        assertEquals("Hello World", new HelloController().hello());
    }
}
