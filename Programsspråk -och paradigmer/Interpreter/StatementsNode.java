/*
 * Sebastian Appelberg and Dat Trieu
 * Group 3
 */

package prop.assignment0;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatementsNode implements INode {
	
	private INode assign;
	private INode statements;
	
	public StatementsNode () {
	}
	
	public StatementsNode(INode assign, INode statements) {
		this.assign = assign;
		this.statements = statements;
	}

	@Override
	public Object evaluate(Object[] args) throws Exception {	
		List<Object>foundValues = new ArrayList<>(Arrays.asList(args));
		String value = "";
		if (assign != null)
			value = "" + assign.evaluate(foundValues.toArray());
		foundValues.add(value);
		if (statements != null)
			value += "\n" + statements.evaluate(foundValues.toArray());
		return value;
	}

	@Override
	public void buildString(StringBuilder builder, int tabs) {
		String indents = "";
		for (int i = 0; i < tabs; i++)
			indents += "\t";
		builder.append(indents + "StatementsNode\n");
		if (assign != null)
			assign.buildString(builder, ++tabs);
		if (statements != null)
			statements.buildString(builder, tabs);
	}

}
