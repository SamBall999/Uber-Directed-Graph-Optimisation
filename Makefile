BINDIR = ./bin
DOCDIR = ./doc
SRCDIR = ./src

JAVAC = javac
JFLAGS = -g  

.SUFFIXES: .java .class

$(BINDIR)/%.class: $(SRCDIR)/%.java
	$(JAVAC) $(JFLAGS) $< -cp $(BINDIR) -d $(BINDIR) 

$(BINDIR)/SimulatorTwo.class: $(BINDIR)/Graph.class $(SRCDIR)/SimulatorTwo.java


docs:
	javadoc -classpath $(BINDIR) -d $(DOCDIR) $(SRCDIR)/*.java

clean:
	@rm -f $(BINDIR)/*.class

cleandocs:
	@rm -f -r $(DOCDIR)/*


