all:
	javac Client.java 
	echo "Main-Class: Client" > manifest.txt
	jar cvfm Client.jar manifest.txt *.class
	echo '#!/usr/bin/java -jar' > cliente.jar
	cat Client.jar >> cliente.jar
	chmod +x cliente.jar
	mkdir clientdoc
	javadoc -quiet -d clientdoc/ Client.java -Xdoclint:none
	cp clientdoc/Client.html clientdoc/Client.htm
	rm clientdoc/*.html
	rm Client.jar
	rm manifest.txt
	rm Client.class
	cp cliente.jar /usr/bin
	cat mancliente | gzip > /usr/share/man/man6/cliente.1.gz
	cp clientdoc/Client.htm /usr/share/man/man6/Client.htm
	rm cliente.jar
	rm -r clientdoc
clean:
	rm /usr/bin/cliente.jar
	rm /usr/share/man/man6/cliente.1.gz
	rm /usr/share/man/man6/Client.htm
	
