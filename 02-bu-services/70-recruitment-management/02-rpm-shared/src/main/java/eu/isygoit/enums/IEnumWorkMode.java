package eu.isygoit.enums;

/**
 * The interface Enum work mode.
 */
public interface IEnumWorkMode {

    /**
     * The constant STR_ENUM_SIZE.
     */
    int STR_ENUM_SIZE = 12;

    /**
     * The enum Types.
     */
    enum Types implements IEnum {
        /**
         * Remote types.
         */
        REMOTE("Remote"),
        /**
         * Hybrid types.
         */
        HYBRID("Hybrid"),
        /**
         * Presential types.
         */
        PRESENTIAL("Presential");

        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }
}
