package com.example.gestionale.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@Qualifier("prezzoStandardStrategy")
public class PrezzoStandardStrategy implements PrezzoStrategy {
    
    @Override
    public BigDecimal calcolaPrezzo(BigDecimal importoBase) {
        return importoBase;
    }

}