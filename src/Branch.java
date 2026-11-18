
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
            throw new BranchFullException("Przekrocozno liczbę laptopów");
        }
        for (Laptop l : laptopList ){
            if (l.compareTo(laptop) == 0){
                throw new DuplicateLaptopException("Podany laptop istnieje już w bazie oddziału!");
            }
        }
        laptopList.add(laptop);
    }

    public void removeLaptop(Laptop laptop) {

        boolean removed = laptopList.removeIf(l -> l.compareTo(laptop) == 0);
        if (!removed) {
            throw new LaptopNotFoundException("Nie znaleziono laptopa: " + laptop.getModel());
        }
    }

    public void sellLaptop(Laptop laptop){
        boolean sold= laptopList.removeIf(s -> s.compareTo(laptop) == 0);
        if (!sold) {
            throw new LaptopNotFoundException("Nie znaleziono laptopa do sprzedaży: " + laptop.getModel());
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
        List<Laptop> results = new ArrayList<>();
        for (Laptop l : laptopList){
            if (l.getModel().toLowerCase().contains(text.toLowerCase()) ||
                    l.getBrand().toLowerCase().contains(text.toLowerCase())){
                results.add(l);
            }
        }

        return results;

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

    public void transferOldestOfTwoExpensive(Branch targetBranch) throws LaptopNotFoundException, BranchFullException, DuplicateLaptopException {
        if(targetBranch == null) {
            throw new LaptopNotFoundException("Podany oddział nie istnieje.");
        }
        if (laptopList.size() < 2) {
            throw new LaptopNotFoundException("Oddział ma mniej niż 2 laptopy, brak możliwości wyboru.");
        }
        if (targetBranch.getLaptopCount() >= targetBranch.getMaxCapacity()) {
            throw new BranchFullException("Oddział docelowy nie ma wolnego miejsca.");
        }
        List<Laptop> sorted = sortByPrice();

        Laptop top1 = sorted.get(sorted.size() - 1);
        Laptop top2 = sorted.get(sorted.size() - 2);
        Laptop chosen = (top1.getYear() <= top2.getYear() ? top1 : top2);

        targetBranch.addLaptop(chosen);
        this.removeLaptop(chosen);

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

    public List<Laptop> getLaptopList() {
        return laptopList;
    }

    public double getCurrentLoad() {
        if (maxCapacity == 0) return 0.0;
        return (double) laptopList.size() / maxCapacity * 100.0;
    }

}
