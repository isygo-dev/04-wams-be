package eu.isygoit.enums;

/**
 * The interface Enum integration order.
 */
public interface IEnumIntegrationOrder {

    /**
     * The constant STR_ENUM_SIZE.
     */
    int STR_ENUM_SIZE = 6;

    /**
     * The enum Types.
     */
    enum Types implements IEnum {
        /**
         * Create types.
         */
        CREATE("CREATE", IEnumRequest.Types.POST),
        /**
         * Update types.
         */
        UPDATE("UPDATE", IEnumRequest.Types.PUT),
        /**
         * Delete types.
         */
        DELETE("DELETE", IEnumRequest.Types.DELETE),

        /**
         * Extract types.
         */
        EXTRACT("EXTRACT", IEnumRequest.Types.GET);

        private final String meaning;
        private final IEnumRequest.Types request;

        Types(String meaning, IEnumRequest.Types request) {
            this.meaning = meaning;
            this.request = request;
        }

        public String meaning() {
            return meaning;
        }

        /**
         * Request enum request . types.
         *
         * @return the enum request . types
         */
        public IEnumRequest.Types request() {
            return request;
        }
    }
}
