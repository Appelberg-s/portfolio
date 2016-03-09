/***

Sebastian Appelberg

***/

:- [tokenizer].

run(InputFile,OutputFile):-
	tokenize(InputFile,Program),
	parse(ParseTree,Program,[]),
	evaluate(ParseTree,[],Variables),
	output_result(OutputFile,ParseTree,Variables).

output_result(OutputFile,ParseTree,Variables):-
	open(OutputFile,write,OutputStream),
	write(OutputStream,'PARSE TREE:'),
	nl(OutputStream),
	writeln_term(OutputStream,0,ParseTree),
	nl(OutputStream),
	write(OutputStream,'EVALUATION:'),
	nl(OutputStream),
	write_list(OutputStream,Variables),
	close(OutputStream).

output_result(OutputFile,ParseTree):-
		open(OutputFile,write,OutputStream),
		write(OutputStream,'PARSE TREE:'),
		nl(OutputStream),
		writeln_term(OutputStream,0,ParseTree),
		close(OutputStream).

writeln_term(Stream,Tabs,int(X)):-
	write_tabs(Stream,Tabs),
	writeln(Stream,int(X)).
writeln_term(Stream,Tabs,ident(X)):-
	write_tabs(Stream,Tabs),
	writeln(Stream,ident(X)).
writeln_term(Stream,Tabs,Term):-
	functor(Term,_Functor,0), !,
	write_tabs(Stream,Tabs),
	writeln(Stream,Term).
writeln_term(Stream,Tabs1,Term):-
	functor(Term,Functor,Arity),
	write_tabs(Stream,Tabs1),
	writeln(Stream,Functor),
	Tabs2 is Tabs1 + 1,
	writeln_args(Stream,Tabs2,Term,1,Arity).

writeln_args(Stream,Tabs,Term,N,N):-
	arg(N,Term,Arg),
	writeln_term(Stream,Tabs,Arg).
writeln_args(Stream,Tabs,Term,N1,M):-
	arg(N1,Term,Arg),
	writeln_term(Stream,Tabs,Arg),
	N2 is N1 + 1,
	writeln_args(Stream,Tabs,Term,N2,M).

write_tabs(_,0).
write_tabs(Stream,Num1):-
	write(Stream,'\t'),
	Num2 is Num1 - 1,
	write_tabs(Stream,Num2).

writeln(Stream,Term):-
	write(Stream,Term),
	nl(Stream).

write_list(_Stream,[]).
write_list(Stream,[Ident = Value|Vars]):-
	write(Stream,Ident),
	write(Stream,' = '),
	format(Stream,'~1f',Value),
	nl(Stream),
	write_list(Stream,Vars).

/***
Kod för parsing.
***/

parse(ParseTree) --> block(ParseTree).

block(block(LC, Statement, RC)) --> left_curly(LC), stmts(Statement), right_curly(RC).

stmts(statements(Assign, Statement)) --> assign(Assign), stmts(Statement).
stmts(statements) --> [].

assign(assignment(ID, Assign, Expr, SC)) --> id(ID), assign_op(Assign), expr(Expr), semicolon(SC).

expr(expression(Term)) --> term(Term).
expr(expression(Term, Add, Expr)) --> term(Term), add_op(Add), expr(Expr).
expr(expression(Term, Sub, Expr)) --> term(Term), sub_op(Sub), expr(Expr).

term(term(Factor)) --> factor(Factor).
term(term(Factor, Mult, Term)) --> factor(Factor), mult_op(Mult), term(Term).
term(term(Factor, Div, Term)) --> factor(Factor), div_op(Div), term(Term).

factor(factor(ID)) --> id(ID).
factor(factor(I)) --> int(I).
factor(factor(LP, Expr, RP)) --> left_paren(LP), expr(Expr), right_paren(RP).

assign_op(assign_op) --> [=].
mult_op(mult_op) --> [*].
div_op(div_op) --> [/].
sub_op(sub_op) --> [-].
add_op(add_op) --> [+].
semicolon(semicolon) --> [;].
left_paren(left_paren) --> ['('].
right_paren(right_paren) --> [')'].
left_curly(left_curly) --> ['{'].
right_curly(right_curly) --> ['}'].

int(int(I)) --> [I], {number(I)}.
id(ident(ID)) --> [ID], {atom(ID)}.

/***
Kod för evaluation.
***/

evaluate(block(left_curly, Statements, right_curly), VariablesIn, VariablesOut):-
		evaluate(Statements, VariablesIn, VariablesOut).

evaluate(statements, VariablesIn, VariablesOut):-
		VariablesOut = VariablesIn.
evaluate(statements(Assignment, Statements), VariablesIn, VariablesOut):-
		evaluate(Assignment, VariablesIn, AssignOut),
		append(VariablesIn, AssignOut, AppendOut),
		evaluate(Statements, AppendOut, VariablesOut).

evaluate(assignment(ident(ID), assign_op, Expression, semicolon), VariablesIn, VariablesOut):-
		evaluate(Expression, VariablesIn, ExprOut),
		VariablesOut = [ID = ExprOut].

evaluate(expression(Term), VariablesIn, VariablesOut):-
		evaluate(Term, VariablesIn, VariablesOut).
evaluate(expression(Term, Operator, Expression), VariablesIn, Accumulated, PrevOperator, VariablesOut):-
		evaluate(Term, VariablesIn, TermOut),
		((PrevOperator == add_op) ->
		Accumulation is Accumulated + TermOut;
		Accumulation is Accumulated - TermOut),
		evaluate(Expression, VariablesIn, Accumulation, Operator, VariablesOut).
evaluate(expression(Term), VariablesIn, Accumulated, Operator, VariablesOut):-
		evaluate(Term, VariablesIn, TermOut),
		((Operator == add_op) ->
		VariablesOut is Accumulated + TermOut;
		VariablesOut is Accumulated - TermOut).
evaluate(expression(Term, Operator, Expression), VariablesIn, VariablesOut):-
		evaluate(Term, VariablesIn, TermOut),
		((Operator == add_op) ->
		evaluate(Expression, VariablesIn, TermOut, add_op, VariablesOut);
		evaluate(Expression, VariablesIn, TermOut, sub_op, VariablesOut)).

evaluate(term(Factor), VariablesIn, VariablesOut):-
		evaluate(Factor, VariablesIn, VariablesOut).
evaluate(term(Factor, Operator, Term), VariablesIn, Accumulated, PrevOperator, VariablesOut):-
		evaluate(Factor, VariablesIn, FactorOut),
		((PrevOperator == mult_op) ->
		Accumulation is Accumulated * FactorOut;
		Accumulation is Accumulated / FactorOut),
		evaluate(Term, VariablesIn, Accumulation, Operator, VariablesOut).
evaluate(term(Factor), VariablesIn, Accumulated, Operator, VariablesOut):-
		evaluate(Factor, VariablesIn, FactorOut),
		((Operator == mult_op) ->
		VariablesOut is Accumulated * FactorOut;
		VariablesOut is Accumulated / FactorOut).
evaluate(term(Factor, Operator, Term), VariablesIn, VariablesOut):-
		evaluate(Factor, VariablesIn, FactorOut),
		((Operator == mult_op) ->
		evaluate(Term, VariablesIn, FactorOut, mult_op, VariablesOut);
		evaluate(Term, VariablesIn, FactorOut, div_op, VariablesOut)).

evaluate(factor(left_paren, Expression, right_paren), VariablesIn, VariablesOut):-
		evaluate(Expression, VariablesIn, VariablesOut).
evaluate(factor(Ident), VariablesIn, VariablesOut):-
		evaluate(Ident, VariablesIn, VariablesOut).
evaluate(factor(Int), VariablesIn, VariablesOut):-
		evaluate(Int, VariablesIn, VariablesOut).

evaluate(ident(ID), VariablesIn, VariablesOut):-
		member(ID=X,VariablesIn),
		evaluate(int(X), VariablesIn, VariablesOut).
evaluate(ident(ID), _, VariablesOut):- VariablesOut = ID.
evaluate(int(Int), _, VariablesOut):- VariablesOut is Int.
