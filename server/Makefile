all:
	javac Server.java 
	echo "Main-Class: Server" > manifest.txt
	jar cvfm Server.jar manifest.txt *.class
	echo '#!/usr/bin/java -jar' > server.jar
	cat Server.jar >> server.jar
	chmod +x server.jar
	mkdir serverdoc
	javadoc -quiet -d serverdoc/ Server.java -Xdoclint:none
	cp serverdoc/Server.html serverdoc/Server.htm
	rm serverdoc/*.html
	rm Server.jar
	rm manifest.txt
	rm Server.class
	rm test.class
	cp server.jar /usr/bin
	cp database /usr/bin
	chmod 666 /usr/bin/database
	cp -r imgs /usr/bin/imgs
	rm -r serverdoc
	rm server.jar
clean:
	rm /usr/bin/server.jar
	rm /usr/bin/database
	rm -r /usr/bin/imgs
