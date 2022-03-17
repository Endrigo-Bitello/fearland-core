package dev.fearland.cangasso.player.enums;

public enum VanishStaff {
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

    public VanishStaff next() {
        if (this == DESATIVADO) {
            return ATIVADO;
        }

        return DESATIVADO;
    }

    private static final VanishStaff[] VALUES = values();

    public static VanishStaff getByOrdinal(long ordinal) {
        if (ordinal < 2 && ordinal > -1) {
            return VALUES[(int) ordinal];
        }

        return null;
    }
}
