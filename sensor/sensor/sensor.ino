#define PIR_INPUT_PIN 2
#define LED_PIN 13
#define CALIBRATION_TIME 30

void setup()
{
  Serial.begin(9600);
  pinMode(PIR_INPUT_PIN, INPUT);
  pinMode(LED_PIN, OUTPUT);
  
  delay(CALIBRATION_TIME * 1000); // Delay for the calibration of the PIR
}


void loop()
{
  int val = 0;
  val = digitalRead(PIR_INPUT_PIN);
  if(val == LOW)
  {
    digitalWrite(LED_PIN, HIGH);
    // Function call for camera
  }
}

