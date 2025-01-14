package com.HowTo.spring_boot_HowTo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(JacksonConfig.class)
public class JacksonConfigTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testJavaTimeModule() {
        assertThat(objectMapper.getRegisteredModuleIds())
                .contains(JavaTimeModule.class.getTypeName());
    }
}
