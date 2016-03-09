/*
 * Sebastian Appelberg and Dat Trieu
 * Group 3
 */

package prop.assignment0;

public class BlockNode implements INode {
	
	private Lexeme leftCurly;
	private INode statements;
	private Lexeme rightCurly;
	
	public BlockNode(Lexeme leftCurly, INode statements, Lexeme rightCurly) {
		this.leftCurly = leftCurly;
		this.statements = statements;
		this.rightCurly = rightCurly;
	}

	@Override
	public Object evaluate(Object[] args) throws Exception {
		return statements.evaluate(new Object[0]);
	}

	@Override
	public void buildString(StringBuilder builder, int tabs) {
		String indents = "";
		for (int i = 0; i < tabs; i++)
			indents += "\t";
		builder.append(indents + "BlockNode\n");
		builder.append(indents + leftCurly + "\n");
		if (statements != null)
			statements.buildString(builder, ++tabs);
		builder.append(indents + rightCurly + "\n");
	}

}
