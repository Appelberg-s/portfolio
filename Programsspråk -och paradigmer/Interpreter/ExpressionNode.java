/*
 * Sebastian Appelberg and Dat Trieu
 * Group 3
 */

package prop.assignment0;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpressionNode implements INode {
	
	private INode term;
	private Lexeme operator;
	private INode expr;
	
	public ExpressionNode (INode term) {
		this.term = term;
	}
	
	public ExpressionNode(INode term, Lexeme operator, INode expr) {
		this.term = term;
		this.operator = operator;
		this.expr = expr;
	}
	
	@Override
	public Object evaluate(Object[] args) throws Exception {
		List<Object> foundValues = new ArrayList<>(Arrays.asList(args));
		double value = (double) term.evaluate(args);
		if (operator != null && operator.token() == Token.ADD_OP) {
			foundValues.add("+");
			if (args.length > 0 && args[args.length-1].equals("-") && !args[args.length-1].equals("("))
				value -= (double) expr.evaluate(foundValues.toArray());
			else
				value += (double) expr.evaluate(foundValues.toArray());
		} else if (operator != null && operator.token() == Token.SUB_OP) {
			foundValues.add("-");
			if (args.length > 0 && args[args.length-1].equals("-") && !args[args.length-1].equals("("))
				value += (double) expr.evaluate(foundValues.toArray());
			else
				value -= (double) expr.evaluate(foundValues.toArray());
		}
		return value;
	}

	@Override
	public void buildString(StringBuilder builder, int tabs) {
		String indents = "";
		for (int i = 0; i < tabs; i++)
			indents += "\t";
		builder.append(indents + "ExpressionNode\n");
		term.buildString(builder, ++tabs);
		if (operator != null) {
			builder.append(indents + "\t" + operator + "\n");
			expr.buildString(builder, tabs);
		}
	}

}
