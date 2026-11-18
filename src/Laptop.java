public class Laptop implements Comparable<Laptop> {
    private String model;
    private String brand;
    private LaptopCondition condition;
    private int year;
    private double price;
    private double batteryHealth;
    private String operatingSystem;
    private int storageGB;

    public Laptop (String model, String brand, LaptopCondition condition, int year, double price, double batteryHealth, String operatingSystem, int storageGB)
        throws InvalidPriceException{
            if(price < 0){
                throw new InvalidPriceException("Cena laptopa nie może być mneijsza od 0");
            }
        this.model = model;
        this.brand = brand;
        this.condition = condition;
        this.year = year;
        this.price = price;
        this.batteryHealth = batteryHealth;
        this.operatingSystem = operatingSystem;
        this.storageGB = storageGB;

    }
    //metoda która wybiera 2 najdrozse laptopy, wybiera starszy i przenosi do wybraego brancha

    @Override
    public String toString(){
        return "Specyfikacja laptopa: model: " + model + ", marka: " + brand + ", kondycja: " + condition + ", " +
                "rok produkcji: " + year + ", cena: " + price + ", kondycja baterii: " + batteryHealth + ", system operacyjny: " + operatingSystem +
                ", pamięć: " + storageGB + "GB.";
    }

    @Override
    public int compareTo(Laptop other){
        int modelCompare = this.model.compareToIgnoreCase(other.model);
        if (modelCompare != 0){
            return modelCompare;
        }
        int brandCompare = this.brand.compareToIgnoreCase(other.brand);
        if(brandCompare != 0){
            return brandCompare;
        }
       return Integer.compare(this.year, other.year);
    }

    public void print(){
        System.out.println(this);
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws InvalidPriceException {
        if (price < 0){
            throw new InvalidPriceException("Cena laptopa nie może byc mniejsza od 0");
        }
        this.price = price;
    }

    public LaptopCondition getCondition() {
        return condition;
    }

    public void setCondition(LaptopCondition condition) {
        this.condition = condition;
    }

    public double getBatteryHealth() {
        return batteryHealth;
    }

    public void setBatteryHealth(double batteryHealth) {
        this.batteryHealth = batteryHealth;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public int getStorageGB() {
        return storageGB;
    }

    public void setStorageGB(int storageGB) {
        this.storageGB = storageGB;
    }


}
