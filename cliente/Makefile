all:
	javac Client.java 
	echo "Main-Class: Client" > manifest.txt
	jar cvfm Client.jar manifest.txt *.class
	echo '#!/usr/bin/java -jar' > cliente.jar
	cat Client.jar >> cliente.jar
	chmod +x cliente.jar
	mkdir clientdoc
	javadoc -d clientdoc/ Client.java
	cp clientdoc/Client.html clientdoc/Client.htm
	rm clientdoc/*.html
	rm Client.jar
	rm manifest.txt

clean:
	rm Client.class
	rm cliente.jar
	rm -r clientdocs
