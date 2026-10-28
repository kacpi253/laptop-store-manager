public class Main {

    public static void main(String[] args){
        Laptop laptop = new Laptop(
                "ThinkPad T14",
                "Lenovo",
                LaptopCondition.SPRAWNY,
                2022,
                3500.0,
                92.5,
                "Windows 11",
                512
        );
        System.out.println(laptop);
    }
}
