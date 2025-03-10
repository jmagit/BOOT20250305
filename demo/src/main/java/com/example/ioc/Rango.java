package com.example.ioc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Component
@ConfigurationProperties("rango")
public class Rango {
    private int min;
    private int max;
    
 }
