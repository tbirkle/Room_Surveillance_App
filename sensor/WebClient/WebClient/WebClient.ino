

#include <SPI.h>         
#include <Ethernet.h>
#include <SD.h>

#define FTPPORT 21
#define HTTPPORT 80
#define PIR_INPUT_PIN 2
#define LED_PIN 13
#define CALIBRATION_TIME 30


#define DEBUG 1

#if DEBUG
#define debugf(fmt, ...) Serial.println(fmt, __VA_ARGS__);
#define debug(fmt) Serial.println(fmt);
#else
#define debugf(fmt, ...)
#define debug(fmt)
#endif


File myFile;

byte mac[] = { 0x90, 0xA2, 0xDA, 0x0F, 0x15, 0x9E };
IPAddress dnServer(141,37,120,101);
IPAddress gateway(141,37,28,254);
IPAddress subnet(255,255,252,0);

//IPAddress ip(192,168,1,10);
IPAddress ip(141,37,31,128);

//IPAddress server(74,125,26,147);
//char server[] = "www.google.de";

//byte server[] = { 74,125,26,147 };
byte server[] = { 141,37,31,129 };
byte ftpserver[] = {46,252,18,34};
//byte ftpserver[] = {141,37,31,115};
//byte ftpserver[] = {144,76,167,69};

char webserver[] = "rs.g8j.de";
// Initialize the Ethernet client library
// with the IP address and port of the server 
// that you want to connect to (port 80 is default for HTTP):
EthernetClient client;
EthernetClient clientphp;
EthernetClient dclient;

char *jpegCode = "FFD8";

//char *request = "GET /GetImage.cgi?Size=320x240 HTTP/1.1";
 char request[]="GET /GetImage.cgi?Size=320x240 HTTP/1.1";

char outBuf[128];
char outCount;
char fileName[13] = "test.jpg";
 
 
void setup() 
{
  // start Ethernet
  //Ethernet.begin(mac, ip, dnServer, gateway, subnet);
  pinMode(10, OUTPUT);
  pinMode(PIR_INPUT_PIN, INPUT);
  pinMode(LED_PIN, OUTPUT);
  
  
  if (!SD.begin(4)) {
    Serial.println("SD initialization failed!");
    return;
  }

  Serial.println("SD initialization done.");
  //Ethernet.begin(mac, ip);
  Ethernet.begin(mac, ip, dnServer, gateway, subnet);
  Serial.begin(9600);
  
  delay(1000);
  Serial.println("calibrating...");


  
  delay(CALIBRATION_TIME * 1000); // Delay for the calibration of the PIR
  debug("start sensing")
}




int cameraDataReceived = 0;
int motion = 0;
void loop()
{
  //debug("2000")
  //delay(2000);
  int val = 0;
  val = digitalRead(PIR_INPUT_PIN);
  if(val == LOW && motion != 1)
  {
    debug("Motion detected")
    motion = 1;
    digitalWrite(LED_PIN, HIGH);
  }
  
  if(motion == 1)
  {
    //debug("Motion function")
    motionDetected();
  }

}

void motionDetected()
{
  if(cameraDataReceived == 0)
  {
    getImage();
  }
  else
  {
    delay(1000);
    if(doFTP()) Serial.println(F("FTP OK"));
    else Serial.println(F("FTP FAIL"));
  
    
    sendPushMessage();
    motion = 0;
    cameraDataReceived = 0;
  }
}


int connectToWebserver(char *server, int port)
{
  if (clientphp.connect(server,port)) {
  Serial.println(F("Command connected"));
  } 
  else {
    Serial.println(F("webserver Command connection failed"));
    return 0;
  }
}

void disconnectFromServer()
{
  Serial.println("start disconnecting");
  // if the server's disconnected, stop the client:
  /*if (!clientphp.connected()) {
    debug("disconnecting")
    clientphp.stop();

    // do nothing forevermore:
    Serial.println("done");
  }*/
  clientphp.stop();
}



void sendPushMessage()
{
  if(!connectToWebserver(webserver, HTTPPORT))
    return;
  clientphp.print("GET /startInform.php");
  clientphp.println(" HTTP/1.1");
  clientphp.println("Host: rs.g8j.de");
  clientphp.println();
  clientphp.println();
  disconnectFromServer();
}






void getImage()
{
  int found = 0;
int counter = 0;
  
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
    //SD.remove("test.jpg");
    myFile = SD.open("test.jpg", FILE_WRITE);
  } 
  else {
    // kf you didn't get a connection to the server:
    Serial.println("connection failed");
  }
   // if there are incoming bytes available 
  // from the server, read them and print them:
   while (client.available()) {

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












File fh;


byte doFTP()
{
  fh = SD.open(fileName,FILE_READ);


  if(!fh)
  {
    Serial.println(F("SD open fail"));
    return 0;    
  }

  Serial.println(F("SD opened"));




  if (client.connect(ftpserver,21)) {
  Serial.println(F("Command connected"));
  } 
  else {
    fh.close();
    Serial.println(F("Command connection failed"));
    return 0;
  }

  //if(!connectToWebserver(ftpserver, FTPPORT))
  //{
  //  fh.close();
  //}

  if(!eRcv()) return 0;

  client.println(F("USER 187687-rs"));
  //client.println(F("USER ftpuser"));
  //client.println(F("USER ubiwtf_1234"));


  if(!eRcv()) return 0;

  client.println(F("PASS Dp3Jp23SS9"));
  //client.println(F("PASS test"));

  if(!eRcv()) return 0;

  client.println(F("SYST"));

  if(!eRcv()) return 0;

  client.println(F("PASV"));

  if(!eRcv()) return 0;

  char *tStr = strtok(outBuf,"(,");
  int array_pasv[6];
  for ( int i = 0; i < 6; i++) {
    tStr = strtok(NULL,"(,");
    array_pasv[i] = atoi(tStr);
    if(tStr == NULL)
    {
      Serial.println(F("Bad PASV Answer"));    

    }
  }

  unsigned int hiPort,loPort;

  hiPort = array_pasv[4] << 8;
  loPort = array_pasv[5] & 255;

  Serial.print(F("Data port: "));
  hiPort = hiPort | loPort;
  Serial.println(hiPort);

  if (dclient.connect(ftpserver,hiPort)) {
    Serial.println(F("Data connected"));
  } 
  else {
    Serial.println(F("Data connection failed"));
    client.stop();
    fh.close();
    return 0;
  }

  //client.print(F("TYPE I"));
  
  
  
  client.print(F("STOR "));
  client.println(fileName);
  
  


  if(!eRcv())
  {
    dclient.stop();
    return 0;
  }


  Serial.println(F("Writing"));

  byte clientBuf[64];
  int clientCount = 0;

  while(fh.available())
  {
    clientBuf[clientCount] = fh.read();
    clientCount++;

    if(clientCount > 63)
    {
      
      dclient.write(clientBuf,64);
      
      
      clientCount = 0;
    }
    //char c = fh.read();
    //Serial.print(c);
    //dclient.print(c);
    
    
  }

  if(clientCount > 0)
  {
    dclient.write(clientBuf,clientCount);
  }

  dclient.stop();
  Serial.println(F("Data disconnected"));

  if(!eRcv()) 
  {
    debug("asdf");
    return 0;
  }//return 0;

  client.println(F("QUIT"));

  if(!eRcv()) return 0;

  client.stop();
  Serial.println(F("Command disconnected"));

  fh.close();
  Serial.println(F("SD closed"));
  return 1;
}

byte eRcv()
{
  byte respCode;
  byte thisByte;
  debug("eRcv");
  while(!client.available()) delay(1);
  debug("wtf");
  respCode = client.peek();

  outCount = 0;

  while(client.available())
  {  
    thisByte = client.read();    
    Serial.write(thisByte);

    if(outCount < 127)
    {
      outBuf[outCount] = thisByte;
      outCount++;      
      outBuf[outCount] = 0;
    }
  }

  if(respCode >= '4')
  {
    efail();
    return 0;  
  }

  return 1;
}


void efail()
{
  byte thisByte = 0;

  client.println(F("QUIT"));

  while(!client.available()) delay(1);

  while(client.available())
  {  
    thisByte = client.read();    
    Serial.write(thisByte);
  }

  client.stop();
  Serial.println(F("Command disconnected"));
  fh.close();
  Serial.println(F("SD closed"));
}
