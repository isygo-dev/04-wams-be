package eu.isygoit.exception.handler;


import eu.isygoit.annotation.MsgLocale;
import eu.isygoit.exception.ManagedException;


/**
 * The type Employee not found exception.
 */
@MsgLocale("employee.not.found.exception")
public class EmployeeNotFoundException extends ManagedException {

    /**
     * Instantiates a new Employee not found exception.
     *
     * @param s the s
     */
    public EmployeeNotFoundException(String s) {
    }
}
