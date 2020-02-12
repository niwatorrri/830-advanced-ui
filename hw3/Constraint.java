import java.awt.*;

/* This is the generic definition of constraints. You should flesh this out to
make it actually work. 

It is fine to change this specification - it is just for guidance.
*/

public abstract class Constraint<T> {

	T value;
	BaseGraphicalObject[] dependsList;
	/**
	 * 
	 */
	public Constraint(T initialValue, BaseGraphicalObject[] depList) {
		value = initialValue;
		dependsList = depList;
	}

    /* Evaluates the constraint function if needed and returns the value. May return
    the value immediately if the constraint doesn't need to be re-evaluated.
 */
	public T valueGet () {
		return this.value;
	}
 
	/* value for the variable this constraint was in was set. This might remove the constraint in a formula
    constraint system, or set the local value and cause dependency invalidating in a multi-way constraint system
    */
	public void valueSet (T newVal) {
		this.value = newVal;
 }

}
