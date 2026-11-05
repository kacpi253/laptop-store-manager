import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreManager {
    private Map<String, Branch> branches;

    public StoreManager(){
         this.branches = new HashMap<>();
    }

    public void addBranch(String name, int capacity){
        branches.put(name, new Branch(name,capacity));
    }

    public void removeBranch(String name){
        branches.remove(name);
    }

    public List<Branch> findEmpty() {
        List<Branch> emptyBranches = new ArrayList<>();
        for (Branch b : branches.values()) {
            if (b.isEmpty()) {
                emptyBranches.add(b);
            }
        }
        return emptyBranches;
    }

    public void summary(){
        System.out.println("Zestawienie oddziałów:");

        for (Branch b : branches.values()) {
            double percent = 0.0;
            if(b.getMaxCapacity() > 0){
                percent = ((double) b.getLaptopCount() / b.getMaxCapacity() * 100.0);
                System.out.printf("Oddział: %s | Zapełnienie: %.2f%%%n", b.getBranchName(), percent);
            }
        }
    }

    public Branch getBranch(String name) {

        return branches.get(name);
    }

    public void transferLaptop(Laptop laptop, String fromBranch, String toBranch) {
        Branch from = branches.get(fromBranch);
        Branch to = branches.get(toBranch);

        if (from != null && to != null && to.getLaptopCount() < to.getMaxCapacity()) {
            from.removeLaptop(laptop);
            to.addLaptop(laptop);
        }else {
            System.out.println("Nie można przenieść laptopa (Brak oddziału lub miejsca)");
        }
    }

    public double totalValue(String branchName) {
        Branch b = branches.get(branchName);
        return (b != null) ? b.totalValue() : 0;
    }

    public int countByBrand(String brand) {
        int count = 0;
        for (Branch b : branches.values()) {
            count += b.countByBrand(brand);
        }
        return count;
    }

}
