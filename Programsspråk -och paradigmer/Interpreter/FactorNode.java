/*
 * Sebastian Appelberg and Dat Trieu
 * Group 3
 */

package prop.assignment0;

public class FactorNode implements INode {
	
	private Lexeme lit;
	private Lexeme leftParen;
	private INode expr;
	private Lexeme rightParen;
	
	public FactorNode(Lexeme lit) {
		this.lit = lit;
	}
	
	public FactorNode (Lexeme leftParen, INode expr, Lexeme righParen) {
		this.leftParen = leftParen;
		this.expr = expr;
		this.rightParen = righParen;
	}

	@Override
	public Object evaluate(Object[] args) throws Exception {
		if (lit != null && lit.token() == Token.INT_LIT)
			return lit.value();
		else if (lit != null && lit.token() == Token.IDENT) {
			double numValue = 0;
			String variable = (String) lit.value();
			for (int i = 0; i < args.length; i++) {
				String value = (String) args[i];
				if (value.startsWith(variable))
					numValue = Double.parseDouble(value.substring(value.indexOf('=')+1, value.length()));
			}
			return numValue;
		}
		return expr.evaluate(new Object[]{"("});
	}

	@Override
	public void buildString(StringBuilder builder, int tabs) {
		String indents = "";
		for (int i = 0; i < tabs; i++)
			indents += "\t";
		builder.append(indents + "FactorNode\n");
		if (lit != null) {
			builder.append(indents + "\t" + lit + "\n");
		} else {
			builder.append(indents + "\t" + leftParen + "\n");
			expr.buildString(builder, ++tabs);
			builder.append(indents + "\t" + rightParen + "\n");
		}
	}

}
