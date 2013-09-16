Busometer
=========

my experiments with raspberry pi!


Build and copy to raspi (check ip and username):

```
mvn clean install -DskipTests && scp target/busometer-with-dependencies.jar pi@192.168.2.3:~
```

Start with:

```
java -classpath .:classes:/opt/pi4j/lib/'*':busometer-with-dependencies.jar no.bekk.Main
```
