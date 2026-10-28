public class Laptop {
    private String model;
    private String brand;
    private LaptopCondition condition;
    private int year;
    private double price;
    private double batteryHealth;
    private String operatingSystem;
    private int storageGB;

    public Laptop (String model, String brand, LaptopCondition condition, int year, double price, double batteryHealth, String operatingSystem, int storageGB){
        this.model = model;
        this.brand = brand;
        this.condition = condition;
        this.year = year;
        this.price = price;
        this.batteryHealth = batteryHealth;
        this.operatingSystem = operatingSystem;
        this.storageGB = storageGB;

    }

    @Override
    public String toString(){
        return "Specyfikacja laptopa: marka: " + model + ", marka: " + brand + ", kondycja: " + condition + ", " +
                "rok produkcji: " + year + ", cena: " + price + ", kondycja baterii: " + batteryHealth + ", system opracyjny: " + operatingSystem +
                ", pamięć: " + storageGB + "GB.";
    }

}
