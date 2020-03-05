JCC = javac

JFLAGS = -d .

default: Pi.class

Pi.class: Pi.java
	$(JCC) $(JFLAG) Pi.java

clean:
	$(RM) *.class



