/**
 * @author Stefan Schoeberl
 * @version 1.0
 * @modified 2018-08-09
 * 
 * @Class EulerNotPossibleException
 */

package graph;

public class EulerNotPossibleException extends PathException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8904822886683051696L;

	public EulerNotPossibleException(String message) {
		super(message);
	}

}
