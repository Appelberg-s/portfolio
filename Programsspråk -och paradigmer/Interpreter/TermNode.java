/*
 * Sebastian Appelberg and Dat Trieu
 * Group 3
 */

package prop.assignment0;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TermNode implements INode {
	
	private INode factor;
	private Lexeme operator;
	private INode term;
	
	public TermNode(INode factor) {
		this.factor = factor;
	}
	
	public TermNode(INode factor, Lexeme operator, INode term) {
		this.factor = factor;
		this.operator = operator;
		this.term = term;
	}

	@Override
	public Object evaluate(Object[] args) throws Exception {
		List<Object> foundValues = new ArrayList<>(Arrays.asList(args));
		double value = (double) factor.evaluate(args);
		if (operator != null && operator.token() == Token.DIV_OP) {
			foundValues.add("/");
			if (args.length > 0 && args[args.length-1].equals("/") && !args[args.length-1].equals("("))
				value *= (double) term.evaluate(foundValues.toArray());
			else
				value /= (double) term.evaluate(foundValues.toArray());
		} else if (operator != null && operator.token() == Token.MULT_OP) {
			foundValues.add("*");
			if (args.length > 0 && args[args.length-1].equals("/") && !args[args.length-1].equals("("))
				value /= (double) term.evaluate(foundValues.toArray());
			else 
				value *= (double) term.evaluate(foundValues.toArray());
		}
		return value;
	}

	@Override
	public void buildString(StringBuilder builder, int tabs) {
		String indents = "";
		for (int i = 0; i < tabs; i++)
			indents += "\t";
		builder.append(indents + "TermNode\n");
		factor.buildString(builder, ++tabs);
		if (operator != null) {
			builder.append(indents + "\t" + operator + "\n");
			term.buildString(builder, tabs);
		}
	}

}
