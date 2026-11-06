
import java.util.*;

public class Branch {
    private String branchName;
    private List<Laptop> laptopList;
    private int maxCapacity;

    public Branch(String branchName, int maxCapacity){
        this.branchName = branchName;
        this.laptopList = new ArrayList<>();
        this.maxCapacity = maxCapacity;
    }

    public void addLaptop(Laptop laptop){
        if (laptopList.size() >= maxCapacity){
            System.err.println("Przekroczono liczbę laptopów");
            return;
        }
        for (Laptop l : laptopList ){
            if (l.compareTo(laptop) == 0){
                System.out.println("Podany laptop istnieje już w bazie");
                return;
            }
        }
        laptopList.add(laptop);
    }

    public void removeLaptop(Laptop laptop){

        boolean removed = laptopList.removeIf(l -> l.compareTo(laptop) == 0);
        if (removed){
            System.out.println("Laptop pomyślnie usunięty");
        }else {
            System.out.println("Laptop nie został odnaleziony");
        }
    }

    public void sellLaptop(Laptop laptop){
        boolean sold= laptopList.removeIf(s -> s.compareTo(laptop) == 0);
        if (sold){
            System.out.println("Laptop został sprzedany");
        }else {
            System.out.println("Laptop nie został odnaleziony");
        }
    }

    public void changeCondition(Laptop laptop, LaptopCondition condition){
        laptop.setCondition(condition);
        System.out.println("Zmieniono kondycję laptopa na: " + condition.toString());
    }

    public void changeYear(Laptop laptop, int year){
        laptop.setYear(year);
        System.out.println("Zmieniono rok produkcji laptopa na: " + year);
    }

    public int countByCondition(LaptopCondition condition){
        int c = 0;
        for (Laptop l : laptopList){
            if(l.getCondition() == condition){
                c++;
            }
        }
        return c;
    }

    public List<Laptop> sortByName(){
        List<Laptop> sortedByName = new ArrayList<>(laptopList);
        Collections.sort(sortedByName);
        return sortedByName;
    }

    public List<Laptop> sortByPrice(){
        List<Laptop> sortedByPrice = new ArrayList<>(laptopList);
        sortedByPrice.sort(Comparator.comparingDouble(Laptop::getPrice));
        return sortedByPrice;
    }

    public List<Laptop> search(String model){
        List<Laptop> names = new ArrayList<>();
        for (Laptop l : laptopList){
            if (l.getModel().equalsIgnoreCase(model)){
                names.add(l);
            }
        }

        return names;
    }

    public List<Laptop> searchPartial(String text){
        List<Laptop> names = new ArrayList<>();
        for (Laptop l : laptopList){
            if (l.getModel().toLowerCase().contains(text.toLowerCase())){
                names.add(l);
            }
        }

        return names;

    }

    public void summary(){
        System.out.println("Oto zestawienie wszystkich laptopów: ");
        for (Laptop l : laptopList){
            l.print();
        }
    }

    public Laptop max(){
        if (laptopList.isEmpty()) {
            return null;
        }
        return Collections.max(laptopList, Comparator.comparingDouble(Laptop::getPrice));
    }

    public double totalValue() {
        double total = 0.0;
        for (Laptop l : laptopList) {
            total += l.getPrice();
        }
        return total;
    }

    public int countByBrand(String brand) {
        int count = 0;
        for (Laptop l : laptopList) {
            if (l.getBrand().equalsIgnoreCase(brand)) {
                count++;
            }
        }
        return count;
    }

    public void transferOldestOfTwoExpensive(Branch targetBranch){
        if(targetBranch == null) {
            System.out.println("Oddział docelowy nie istnieje");
            return;
        }
        if (laptopList.size() < 2){
            System.out.println("Oddział ma mniej niż 2 laptopy, brak możliwości wyboru");
            return;
        }
        if (targetBranch.getLaptopCount() >= targetBranch.getMaxCapacity()){
            System.out.println("Oddział docelowy nie ma wolnego miejsca! ");
            return;
        }
        List<Laptop> sorted = sortByPrice();

        Laptop top1 = sorted.get(sorted.size() - 1);
        Laptop top2 = sorted.get(sorted.size() - 2);
        Laptop chosen = (top1.getYear() <= top2.getYear() ? top1 : top2);
        this.removeLaptop(chosen);
        targetBranch.addLaptop(chosen);
        System.out.println("Przeniesiono laptop: " + chosen.getModel() + " (" + chosen.getYear() + ") do oddziału " + targetBranch.getBranchName());

    }
    public String getBranchName() {
        return branchName;
    }

    public int getLaptopCount() {
        return laptopList.size();
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public boolean isEmpty() {
        return laptopList.isEmpty();
    }

}
