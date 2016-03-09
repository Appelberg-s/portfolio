/*
 * Sebastian Appelberg and Dat Trieu
 * Group 3
 */

package prop.assignment0;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class AssignmentNode implements INode {
	
	private Lexeme id;
	private Lexeme assignOp;
	private INode expr;
	private Lexeme semicolon;
	
	public AssignmentNode(Lexeme id, Lexeme assignOp, INode expr, Lexeme semicolon) {
		this.id = id;
		this.assignOp = assignOp;
		this.expr = expr;
		this.semicolon = semicolon;
	}

	@Override
	public Object evaluate(Object[] args) throws Exception {
		String value = "";
		value += id.value() + " ";
		value += assignOp.value() + " ";
		value += new DecimalFormat("#.##", 
				DecimalFormatSymbols.getInstance(Locale.ENGLISH)).format(expr.evaluate(args));
		return value;
	}

	@Override
	public void buildString(StringBuilder builder, int tabs) {
		String indents = "";
		for (int i = 0; i < tabs; i++)
			indents += "\t";
		builder.append(indents + "AssignmentNode\n");
		builder.append(indents + "\t" + id + "\n");
		builder.append(indents + "\t" + assignOp + "\n");
		expr.buildString(builder, ++tabs);
		builder.append(indents + "\t" + semicolon + "\n");
	}

}
