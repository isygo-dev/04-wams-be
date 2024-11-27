package eu.isygoit.enums;

/**
 * The interface Enum prime type.
 */
public interface IEnumPrimeType {
    /**
     * The constant STR_ENUM_SIZE.
     */
    int STR_ENUM_SIZE = 40;

    /**
     * The enum Types.
     */
    enum Types implements IEnum {

        /**
         * Cash types.
         */
        CASH("Cash"),
        /**
         * Benefits types.
         */
        BENEFITS("Benefits"),
        /**
         * Bonus types.
         */
        BONUS("Bonus"),
        /**
         * Allowance types.
         */
        ALLOWANCE("Allowance"),
        /**
         * Commission types.
         */
        COMMISSION("Commission"),
        /**
         * The Stock options.
         */
        STOCK_OPTIONS("Stock Options"),
        /**
         * The Profit sharing.
         */
        PROFIT_SHARING("Profit Sharing"),
        /**
         * Overtime types.
         */
        OVERTIME("Overtime"),
        /**
         * Incentives types.
         */
        INCENTIVES("Incentives"),
        /**
         * The Per diem.
         */
        PER_DIEM("Per Diem"),
        /**
         * Retention types.
         */
        RETENTION("Retention"),
        /**
         * Other types.
         */
        OTHER("Other");
        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }

}
