package com.example.gestionale.model;

public enum StatoOrdine {
    
    IN_ELABORAZIONE("In elaborazione"),
    SPEDITO("Spedito"),
    CONSEGNATO("Consegnato"),
    ANNULLATO("Annullato");

    private String stato;

    private StatoOrdine(String stato) {
        this.stato = stato;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}
