package agh.dg.enums;

public enum GameCommunicat {
    NO_GENOTYPE("No genotype to show"),
    NO_SELECTED_ANIMAL("No animal to save statistics");

    private final String communicat;

    GameCommunicat(String communicat) {
        this.communicat = communicat;
    }

    public String toString() {
        return communicat;
    }
}