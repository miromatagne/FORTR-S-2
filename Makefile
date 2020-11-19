all:
	jflex src/LexicalAnalyzer.flex
	javac -d bin -cp src src/Main.java
	jar cfe dist/part2.jar Main -C bin .

normal:
	java -jar dist/part2.jar test/Average/Average.fs

verbose:
	java -jar dist/part2.jar test/Average/Average.fs -v

tree:
	java -jar dist/part2.jar test/Average/Average.fs -wt tree.tex