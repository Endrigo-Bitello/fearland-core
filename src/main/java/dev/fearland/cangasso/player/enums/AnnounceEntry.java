package dev.fearland.cangasso.player.enums;

public enum AnnounceEntry {

    ATIVADO,
    DESATIVADO;

    public String getInkSack() {
        if(this == ATIVADO) {
            return "10";
        }

        return "8";

    }

    public String getName() {
        if(this == ATIVADO) {
            return "§aAtivado";
        }
        return "§cDesativado";

    }

    public AnnounceEntry next() {
        if(this == DESATIVADO) {
            return ATIVADO;
        }
        return DESATIVADO;
    }

    private static final AnnounceEntry[] VALUES = values();
    public static AnnounceEntry getByOrdinal(long ordinal) {
        if (ordinal < 2 && ordinal > -1) {
            return VALUES[(int) ordinal];
        }

        return null;
    }
}

