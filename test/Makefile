all:
	jflex ../src/LexicalAnalyzer.flex
	javac -d ../bin -cp ../src ../src/Main.java

.PHONY: all atom comments errors average exponents factorial fibonacci maximum instructions program

atom:
	@java -cp ../bin Main ./Atom/Atom.fs >file 
	@diff -b --strip-trailing-cr ./Atom/Atom.output file && echo "Atom : Test Successful" || echo "Atom : Test Failed"
	@rm -f file

comments:
	@java -cp ../bin Main ./Comments/Comments.fs >file 
	@diff -b --strip-trailing-cr ./Comments/Comments.output file && echo "Comments : Test Successful" || echo "Comments : Test Failed"
	@rm -f file

errors:
	@java -cp ../bin Main ./Errors/Errors.fs >file 
	@diff -b --strip-trailing-cr ./Errors/Errors.output file && echo "Errors : Test Successful" || echo "Errors : Test Failed"
	@rm -f file

average:
	@java -cp ../bin Main ./FortrS/Average.fs >file 
	@diff -b --strip-trailing-cr ./FortrS/Average.output file && echo "Average : Test Successful" || echo "Average : Test Failed"
	@rm -f file

exponents:
	@java -cp ../bin Main ./FortrS/Exponents.fs >file 
	@diff -b --strip-trailing-cr ./FortrS/Exponents.output file && echo "Exponents : Test Successful" || echo "Exponents : Test Failed"
	@rm -f file

factorial:
	@java -cp ../bin Main ./FortrS/Factorial.fs >file 
	@diff -b --strip-trailing-cr ./FortrS/Factorial.output file && echo "Factorial : Test Successful" || echo "Factorial : Test Failed"
	@rm -f file

fibonacci:
	@java -cp ../bin Main ./FortrS/Fibonacci.fs >file 
	@diff -b --strip-trailing-cr ./FortrS/Fibonacci.output file && echo "Fibonacci : Test Successful" || echo "Fibonacci : Test Failed"
	@rm -f file

maximum:
	@java -cp ../bin Main ./FortrS/Maximum.fs >file 
	@diff -b --strip-trailing-cr ./FortrS/Maximum.output file && echo "Maximum : Test Successful" || echo "Maximum : Test Failed"
	@rm -f file

instructions:
	@java -cp ../bin Main ./FortrS/Instructions.fs >file 
	@diff -b --strip-trailing-cr ./FortrS/Instructions.output file && echo "Instructions : Test Successful" || echo "Instructions : Test Failed"
	@rm -f file

program:
	@java -cp ../bin Main ./FortrS/Program.fs >file 
	@diff -b --strip-trailing-cr ./FortrS/Program.output file && echo "Program : Test Successful" || echo "Program : Test Failed"
	@rm -f file