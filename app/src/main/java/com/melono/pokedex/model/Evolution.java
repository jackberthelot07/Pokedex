package com.melono.pokedex.model;

public class Evolution {
    private Family family;
    private String description;

    public Evolution(Family family, String description) {
        this.family = family;
        this.description = description;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
