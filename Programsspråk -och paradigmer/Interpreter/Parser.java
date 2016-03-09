/*
 * Sebastian Appelberg and Dat Trieu
 * Group 3
 */

package prop.assignment0;

import java.io.*;

public class Parser implements IParser{

	private Tokenizer tokenizer;
	
	@Override
	public void open(String fileName) throws IOException, TokenizerException {
		tokenizer = new Tokenizer();
		tokenizer.open(fileName);
	}

	@Override
	public INode parse() throws IOException, TokenizerException,
	ParserException {
		return block(tokenizer);
	}

	@Override
	public void close() throws IOException {
		if (tokenizer != null)
			tokenizer.close();
	}
	
	private INode block (Tokenizer tokenizer) 
			throws ParserException, TokenizerException, IOException {
		tokenizer.moveNext();
		if (tokenizer.current().token() != Token.LEFT_CURLY)
			throw new ParserException("Missing opening bracket");
		Lexeme leftCurly = tokenizer.current();
		tokenizer.moveNext();
		INode statement = statements(tokenizer);
		if (tokenizer.current().token() != Token.RIGHT_CURLY)
			throw new ParserException("Missing closing bracket");
		Lexeme rightCurly = tokenizer.current();
		return new BlockNode(leftCurly, statement, rightCurly);		
	}
	
	private INode statements (Tokenizer tokenizer) 
			throws ParserException, TokenizerException, IOException {
		if (tokenizer.current().token() == Token.RIGHT_CURLY)
			return new StatementsNode();
		INode assign = assignment(tokenizer);
		tokenizer.moveNext();
		INode statements = statements(tokenizer);
		return new StatementsNode(assign, statements);
	}
	
	private INode assignment (Tokenizer tokenizer) 
			throws ParserException, TokenizerException, IOException {
		if (tokenizer.current().token() != Token.IDENT)
			throw new ParserException("No starting symbol");
		Lexeme id = tokenizer.current();
		tokenizer.moveNext();
		if (tokenizer.current().token() != Token.ASSIGN_OP)
			throw new ParserException("No assign operation");
		Lexeme assignOp = tokenizer.current();
		tokenizer.moveNext();
		INode expr = expression(tokenizer);
		if (tokenizer.current().token() != Token.SEMICOLON)
			throw new ParserException("Missing semicolon");
		Lexeme semicolon = tokenizer.current();
		return new AssignmentNode(id, assignOp, expr, semicolon);
	}
	
	private INode expression (Tokenizer tokenizer) 
			throws ParserException, TokenizerException, IOException {
		INode term = term(tokenizer);
		if (tokenizer.current().token() == Token.ADD_OP || 
				tokenizer.current().token() == Token.SUB_OP) {
			Lexeme operator = tokenizer.current();
			tokenizer.moveNext();
			INode expr = expression(tokenizer);
			return new ExpressionNode(term, operator, expr);
		} 
		return new ExpressionNode(term);
	}
	
	private INode term (Tokenizer tokenizer) 
			throws ParserException, TokenizerException, IOException {
		INode factor = factor(tokenizer);
		tokenizer.moveNext();
		if (tokenizer.current().token() == Token.MULT_OP || 
				tokenizer.current().token() == Token.DIV_OP) {
			Lexeme operator = tokenizer.current();
			tokenizer.moveNext();
			INode term = term (tokenizer);
			return new TermNode(factor, operator, term);
		} 
		return new TermNode(factor);
	}
	
	private INode factor (Tokenizer tokenizer) 
			throws ParserException, TokenizerException, IOException {
		if (tokenizer.current().token() == Token.INT_LIT || 
				tokenizer.current().token() == Token.IDENT)
			return new FactorNode(tokenizer.current());
		else if (tokenizer.current().token() == Token.LEFT_PAREN) {
			Lexeme leftParen = tokenizer.current();
			tokenizer.moveNext();
			INode expr = expression(tokenizer);
			Lexeme rightParen = tokenizer.current();
			return new FactorNode(leftParen, expr, rightParen);
		} else 
			throw new ParserException("Incorrect factor");
	}
	
}
