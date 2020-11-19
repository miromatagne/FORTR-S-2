all:
	jflex ../src/LexicalAnalyzer.flex
	javac -d ../bin -cp ../src ../src/Main.java

.PHONY: all atom average

atom:
	@java -cp ../bin Main ./Atom/Atom.fs >file 
	@diff -b --strip-trailing-cr ./Atom/Atom.output file && echo "Atom : Test Successful" || echo "Comments : Test Failed"
	@rm -f file

average:
	@java -cp ../bin Main ./Average/Average.fs >file 
	@diff -b --strip-trailing-cr ./Average/Average.output file && echo "Average : Test Successful" || echo "Comments : Test Failed"
	@rm -f file