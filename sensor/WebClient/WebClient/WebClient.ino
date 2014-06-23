

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
byte ftpserver[] = {46,252,18,34};
// Initialize the Ethernet client library
// with the IP address and port of the server 
// that you want to connect to (port 80 is default for HTTP):
EthernetClient client;

char *jpegCode = "FFD8";

//char *request = "GET /GetImage.cgi?Size=320x240 HTTP/1.1";
 char request[]="GET /GetImage.cgi?Size=320x240 HTTP/1.1";
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

  



  // if you get a connection, report back via serial:
  if (client.connect(server, 80)) {
    Serial.println("connected");
    // Make a HTTP request:
    //client.print("GET /GetImage.cgi?Size=320x240 HTTP/1.1");
    //client.println("GET /avi/20140612/20oclock/202614s.jpg HTTP/1.0");
    client.print(request);
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



int found = 0;
int counter = 0;
int cameraDataReceived = 0;
int ftpDataSent = 0;
int ftpconnected = 0;
void loop()
{
  if(cameraDataReceived == 0)
  {
    getImage();
  }
  else
  {
    Serial.println("ready");
    if(ftpconnected == 0)
    {
      connectToFTP();
    }
    else
    {
      sendDataToFTP();
    }
  }
}

void connectToFTP()
{
  if (client.connect(ftpserver, 21)) {
    Serial.println("ftpconnected");
    client.println(F("USER 187687-rs"));
    client.println(F("PASS Dp3Jp23SS9"));
    
    myFile = SD.open("test.jpg", FILE_READ);
    ftpconnected = 1;
  } 
  else {
    // kf you didn't get a connection to the server:
    Serial.println("connection failed");
  }
}

void sendDataToFTP()
{
  while(myFile.available())
  {
    char c = myFile.read();
    client.write(c);
  }
  
  // if the server's disconnected, stop the client:
  if (!client.connected()) {
    Serial.println();
    Serial.println("ftpdisconnecting.");
    client.stop();

    // do nothing forevermore:
    myFile.close();
    Serial.println("done");
    ftpDataSent = 1;
  }
}

void getImage()
{
   // if there are incoming bytes available 
  // from the server, read them and print them:
  if (client.available()) {

    char c = client.read();
    
    
    if( found == 1)
    {
      myFile.write(c);
      Serial.print(c);
    }
    if(found == 0)
    {
      if(c == '\r' || c == '\n')
      {
        counter++;
        if(counter == 4)
        {
          found = 1;
        }
      }
      else
      {
        counter = 0;
      }
    }
  }

  // if the server's disconnected, stop the client:
  if (!client.connected()) {
    Serial.println();
    Serial.println("disconnecting.");
    client.stop();

    // do nothing forevermore:
    myFile.close();
    Serial.println("done");
    cameraDataReceived = 1;
    //while(dataSent == 0);
  }
  
}
