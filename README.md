Busometer
=========

Først, a word to the wise: dette er første versjonen av denne dokumentasjonen, og den inneholder helt sikkert både skrivefeil og
tekniske feil. Gi meg et rop eller et pull request hvis det er noe som bør fikses. Busometeret er fortsatt ikke så konfigurerbart
som det bør være: foreløpig virker det kun med busstoppet utenfor leiligheten min.

Gjør livet ditt lettere med Raspberry Pi og Ruters API! Dette prosjektet lar deg bygge din egen busstavle hjemme på kjøkkenbenken.
Det benytter seg av Ruter sitt sanntids-[API](http://labs.trafikanten.no/how-to-use-the-api/realtime-departures.aspx).

For å komme i gang med prosjektet trenger du:

1. Raspberry Pi
2. SD-kort (jeg kjøpte fra [dealextreme](http://dx.com/p/genuine-samsung-class-6-sdhc-card-silver-grey-8gb-125025))
3. WiFi USB dongle (jeg kjøpte fra [dealextreme](http://dx.com/p/ultra-mini-nano-usb-2-0-802-11n-b-g-150mbps-wi-fi-wlan-wireless-network-adapter-black-71905))
4. Breadboard, motstander, kabler og LEDs. (jeg ville prøvd [http://www.adafruit.com/](adafruit))

Sett opp Raspberry PI med Raspian og Java
-----------------------------------------

Installer Raspbian som beskrevet på [elinux](http://elinux.org/RPi_Easy_SD_Card_Setup).
Hvis du bruker Mac fungerer oppskriften for Linux helt utmerket. Når du er ferdig skal du kunne koble deg til Pien din
over SSH. Den letteste måten å koble deg til på en Mac er å slå på "Internet sharing" av "Thunderbolt Ethernet" under Sharing i System Preferences.

Følg deretter instruksjonene for å [legge inn PI4J](http://pi4j.com/install.html) (merk at du må installere Java og WiringPi først).

Koble opp breadboard med motstander og LEDs, som vist på bildet under.

![breadboard koblinger](https://raw.github.com/lillesand/raspberry-pi/f6d3045bb3f09bd20b84961c47f444130161de19/img/breadboard.jpg)

Der koblingene er gjort som følger i henhold til [Raspberry Pi sin GPIO](http://pi4j.com/images/p1header-large.png):

<table>
  <tr>
    <td>Grå</td>
    <td>0V (ground)</td>
  </tr>
  <tr>
    <td>Gul</td>
    <td>GPIO0</td>
  </tr>
  <tr>
    <td>Grønn</td>
    <td>GPIO1</td>
  </tr>
  <tr>
    <td>Blå</td>
    <td>GPIO2</td>
  </tr>
  <tr>
    <td>Orange</td>
    <td>GPIO3</td>
  </tr>
  <tr>
    <td>Lilla</td>
    <td>GPIO4</td>
  </tr>
<table>

Sett opp WiFi
-------------

Konfigurer WiFi som beskrevet for eksempel på [adafruit](http://learn.adafruit.com/adafruits-raspberry-pi-lesson-3-network-setup/setting-up-wifi-with-occidentalis).

Jeg endte opp med følgende ```/etc/network/interfaces``` :

<pre>
auto lo

iface lo inet loopback
iface eth0 inet dhcp

allow-hotplug wlan0
iface wlan0 inet dhcp
  wpa-ssid --ssid--
  wpa-psk --wpa-passord--

iface default inet dhcp
</pre>

Sett opp auto-reconnect på WiFi
-------------------------------

Jeg har hatt en del problemer med at Pien faller av trådløsnettet. Det fungerer ikke noe særlig når man har en buss å rekke.

Løsningen for meg ble å sette opp en cron som regelmessig sjekker om jeg har nett, og restarter WiFi-interfacet hvis ikke.
Scriptet er en modifisert utgave av WiFi_Check som linkes til fra [Raspberry Pi-forumet](http://www.raspberrypi.org/phpBB3/viewtopic.php?t=16054).
Gi meg beskjed hvis du har samme problem, så kan jeg beskrive i større detalj hvordan jeg løste det.

Bygg prosjektet
---------------

Busometer bygges ved hjelp av Maven. Jeg har ikke akkurat overfokusert på best practices, så det er en del tester som går mot
Ruter sitt API. Du bygger derfor prosjektet best uten testene:

```
mvn clean install -DskipTests
```

Kopier koden over til Pien
--------------------------

Kopier koden over ved hjelp av scp. Forhåpentligvis vet du hva IPen til Pien er innen nå. Bytt ut 192.168.2.2 med IPen til DIN Pi.

```
scp target/busometer-with-dependencies.jar pi@192.168.2.2:~
```

Kjør koden på Pien
------------------

Busometeret må kjøre som en bakgrunnsprosess på Pien, sånn at du slipper å være tilkoblet den hele tiden.

Den letteste måten jeg fant å gjøre dette på var å start en screen.

Screen starter du med kommandoen screen.

```
screen
```

Start deretter busometeret:

```
sudo java -classpath .:classes:/opt/pi4j/lib/'*':busometer-with-dependencies.jar no.bekk.Main
```

Trykk så ```ctrl-a``` etterfulgt av ```d``` for å koble fra screenen. Hvis du ønsker å koble deg til den senere kan du skrive

```
screen -r
```