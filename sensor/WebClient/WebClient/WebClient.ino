

#include <SPI.h>         
#include <Ethernet.h>
#include <SD.h>




File myFile;

byte mac[] = { 0x90, 0xA2, 0xDA, 0x0F, 0x15, 0x9E };
//IPAddress dnServer(141,37,120,101);
//IPAddress gateway(141,37,28,254);
//IPAddress subnet(255,255,252,0);

IPAddress ip(192,168,1,10);
//IPAddress ip(141,37,31,128);

//IPAddress server(74,125,26,147);
//char server[] = "www.google.de";

//byte server[] = { 74,125,26,147 };
byte server[] = { 192,168,1,200 };

// Initialize the Ethernet client library
// with the IP address and port of the server 
// that you want to connect to (port 80 is default for HTTP):
EthernetClient client;

String FF = String(255, HEX);
String D8 = String(216, HEX);
String jpeg = String(FF + D8);

char *jpegCode = "FFD8";

void setup() 
{
  // start Ethernet
  //Ethernet.begin(mac, ip, dnServer, gateway, subnet);
  pinMode(10, OUTPUT);
   
  if (!SD.begin(4)) {
    Serial.println("SD initialization failed!");
    return;
  }

  Serial.println("SD initialization done.");
  Ethernet.begin(mac, ip);
  Serial.begin(9600);
  
  delay(1000);
  Serial.println("connecting...");
    Serial.println(jpegCode);
  



  // if you get a connection, report back via serial:
  if (client.connect(server, 80)) {
    Serial.println("connected");
    // Make a HTTP request:
    client.println("GET /GetImage.cgi?Size=320x240 HTTP/1.1");
    //client.println("GET /avi/20140612/20oclock/202614s.jpg HTTP/1.1");
    client.println("Host: www.google.com");
    client.println("Connection: close");
    client.println();
    SD.remove("test.jpg");
    myFile = SD.open("test.jpg", FILE_WRITE);
  } 
  else {
    // kf you didn't get a connection to the server:
    Serial.println("connection failed");
  }
  
}

int first = 0;
void loop()
{
   // if there are incoming bytes available 
  // from the server, read them and print them:
  if (client.available()) {
    
    if(first != 1)
    {
      first = 1;
    //  client.find("Content-Type: image/jpeg<br>");
      //client.find("<br><br>");
      //myFile.write(jpeg);
      client.find("\r\n\r\n");
    }

    //myFile.write(client.read());
    char c = client.read();
    myFile.write(c);
    Serial.print(c);
  }

  // if the server's disconnected, stop the client:
  if (!client.connected()) {
    Serial.println();
    Serial.println("disconnecting.");
    client.stop();

    // do nothing forevermore:
    myFile.close();
    Serial.println("done");
    while(true);
  }
}
