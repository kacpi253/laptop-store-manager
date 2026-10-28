public enum LaptopCondition {
    SPRAWNY{
        @Override
        public String toString(){
           return  "Sprawny";
        }
    },
    USZKODZONY,
    W_NAPRAWIE,
    W_TRAKCIE_TESTOW
}
