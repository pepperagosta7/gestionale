package com.example.gestionale.service;

import java.math.BigDecimal;

public interface PrezzoStrategy {

    BigDecimal calcolaPrezzo(BigDecimal importoBase);
    
}