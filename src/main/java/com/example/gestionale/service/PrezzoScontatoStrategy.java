package com.example.gestionale.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("prezzoScontatoStrategy")
public class PrezzoScontatoStrategy implements PrezzoStrategy {
    @Override
    public BigDecimal calcolaPrezzo(BigDecimal importoBase) {
        return importoBase.multiply(BigDecimal.valueOf(0.9)); // Sconto del 10%
    }
}