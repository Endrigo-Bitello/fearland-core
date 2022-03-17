package dev.fearland.cangasso.player.enums;

public enum PartyRequest {
    ATIVADO,
    DESATIVADO;

    public String getInkSack() {
        if (this == ATIVADO) {
            return "10";
        }

        return "8";
    }

    public String getName() {
        if (this == ATIVADO) {
            return "§aAtivado";
        }

        return "§cDesativado";
    }

    public PartyRequest next() {
        if (this == DESATIVADO) {
            return ATIVADO;
        }

        return DESATIVADO;
    }

    private static final PartyRequest[] VALUES = values();

    public static PartyRequest getByOrdinal(long ordinal) {
        if (ordinal < 2 && ordinal > -1) {
            return VALUES[(int) ordinal];
        }

        return null;
    }


}
