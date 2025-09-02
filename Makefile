all: 
		javac com/craftinginterpreters/lox/*.java

run:
		java com.craftinginterpreters.lox.Lox

clean:
		rm com/craftinginterpreters/lox/*.class
