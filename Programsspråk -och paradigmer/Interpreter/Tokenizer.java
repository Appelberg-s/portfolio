/*
 * Sebastian Appelberg and Dat Trieu
 * Group 3
 */

package prop.assignment0;

import java.io.IOException;
import java.util.regex.Pattern;
//import java.util.HashSet;

public class Tokenizer implements ITokenizer{
	private final Pattern ALPHABETIC = Pattern.compile("[a-z]");
	private final Pattern NUMERIC = Pattern.compile("[0-9]");

	private Scanner scanner;
	private Lexeme currentLexeme = new Lexeme(null, Token.NULL);

	@Override
	public void open(String fileName) throws IOException, TokenizerException {
		scanner = new Scanner();
		scanner.open(fileName);	
	}

	@Override
	public Lexeme current() {
		return currentLexeme;
	}

	@Override
	public void moveNext() throws IOException, TokenizerException {
		if (scanner == null)
			throw new TokenizerException("No file opened");
		if (currentLexeme.token() == Token.EOF)
			return;
		
		while (scanner.current() == Scanner.NULL || scanner.current() == ' '
				|| scanner.current() == '\n' || scanner.current() == '\r') 
			scanner.moveNext();

		if (scanner.current() == Scanner.EOF) {
			currentLexeme = new Lexeme(scanner.current(), Token.EOF);
		} else if (ALPHABETIC.matcher(Character.toString(scanner.current())).matches()) {
			String lexemeValue = "";
			while (ALPHABETIC.matcher(Character.toString(scanner.current())).matches()){
				lexemeValue += scanner.current();
				scanner.moveNext();
			}
			currentLexeme = new Lexeme(lexemeValue, Token.IDENT);
		} else if (NUMERIC.matcher(Character.toString(scanner.current())).matches()) {
			String lexemeValue = "";
			while (NUMERIC.matcher(Character.toString(scanner.current())).matches()){
				lexemeValue += scanner.current();
				scanner.moveNext();
			}
			currentLexeme = new Lexeme(Double.parseDouble(lexemeValue), Token.INT_LIT);
		} else {
			switch (scanner.current()) {
			case '=': currentLexeme = new Lexeme(scanner.current(), Token.ASSIGN_OP);
			break;
			case '+': currentLexeme = new Lexeme(scanner.current(), Token.ADD_OP);
			break;
			case '-': currentLexeme = new Lexeme(scanner.current(), Token.SUB_OP);
			break;
			case '*': currentLexeme = new Lexeme(scanner.current(), Token.MULT_OP);
			break;
			case '/': currentLexeme = new Lexeme(scanner.current(), Token.DIV_OP);
			break;
			case '{': currentLexeme = new Lexeme(scanner.current(), Token.LEFT_CURLY);
			break;
			case '}': currentLexeme = new Lexeme(scanner.current(), Token.RIGHT_CURLY);
			break;
			case '(': currentLexeme = new Lexeme(scanner.current(), Token.LEFT_PAREN);
			break;
			case ')': currentLexeme = new Lexeme(scanner.current(), Token.RIGHT_PAREN);
			break;
			case ';': currentLexeme = new Lexeme(scanner.current(), Token.SEMICOLON);
			break;
			default: 
				throw new TokenizerException("Illegal syntax: '" + scanner.current() + "'");
			}
			scanner.moveNext();
		}
	}

	@Override
	public void close() throws IOException {
		if (scanner != null)
			scanner.close();
	}
}
