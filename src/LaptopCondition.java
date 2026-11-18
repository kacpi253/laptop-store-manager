public enum LaptopCondition {
    SPRAWNY{
        @Override
        public String toString(){
           return  "Sprawny";
        }
    },
    USZKODZONY{
        @Override
        public String toString() {
            return "Uszkodzony";
        }
    },
    W_NAPRAWIE{
        @Override
        public String toString() {
            return "W naprawie";
        }
    },
    W_TRAKCIE_TESTOW{
    @Override
    public String toString() {
        return "W trakcie testów";
    }
    },
    ZAREZERWOWANY{
        @Override
        public String toString() {
            return "Zarezerwowany";
        }
    }
}
