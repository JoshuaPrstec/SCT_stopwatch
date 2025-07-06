<!--suppress HtmlDeprecatedAttribute -->
<h1 align="center">
SCT stopwatch
</h1>

## Installation

### Android device

https://github.com/user-attachments/assets/47c3cf00-284d-4941-923c-db877bf35241

1. Download the [latest SCTstopwatch.apk](https://github.com/JoshuaPrstec/SCT_stopwatch/releases/latest/download/SCTStopwatch.apk)
   or [latest BTstopwatch.apk](https://github.com/JoshuaPrstec/SCT_stopwatch/releases/latest/download/BTStopwatch.apk)
2. Once downloaded, open the Files app and press ```Downloads```
3. Press on the SCTstopwatch.apk file
4. If prompted, allow Files to install unknown apps
5. Navigate to the app drawer and locate the SCT stopwatch app
6. If prompted, allow SCT stopwatch to access nearby devices
7. Turn Bluetooth on

### Computer

#### Windows

https://github.com/user-attachments/assets/2f1724e6-594a-43d4-b01a-1a04c884442f


1. Right-click on the desktop and click New -> Shortcut
2. In the location text field, type the following:
   ```shell
   fsquirt.exe -receive
   ```
3. Name the file ```Bluetooth File Receive```

#### Mac

1. Go to Settings -> General -> Sharing
2. Turn on Bluetooth Sharing
3. Go to Settings -> Bluetooth
4. Connect to the Android device

## Usage Guide

### Recording race

https://github.com/user-attachments/assets/4c2939b5-5ab4-44d7-b9f6-2686d03687ad


1. Press ```Start``` when the race starts
2. Press ```Lap``` for each finishing time
3. Once all times are recorded, press ```Stop```

>[!IMPORTANT]
>Make sure to lap the last time **before** pressing ```Stop```

To resume the stopwatch, press ```Resume```

### Uploading results

https://github.com/user-attachments/assets/5e0a9551-a927-4c60-aa51-092b9e9252b3

https://github.com/user-attachments/assets/0e500d4f-f5e0-4b4d-8f6e-c8411ea3f3c8

1. [Windows only] Double-click on the ```Bluetooth File Receive``` app
2. On the Android device, press ```Upload```
3. Select a race distance (or enter a custom distance) and press ```OK```
4. When the share menu appears, press ```Bluetooth```
5. Press the computer name
6. [Windows only] Select the desired download location
   
   [Mac only] The file will be automatically downloaded to the ```Downloads``` location
7. On the computer, copy the times in the .xlsx file (column B) and paste them into the ```times``` column of the master file

## Troubleshooting

- To turn vibration on/off, press the settings icon (top right) and toggle the ```Vibration``` switch.
- If the vibration is not working, ensure touch feedback is enabled (Settings -> Sound and vibration -> Vibration and haptics -> Touch feedback)
- To turn the reset confirmation message on/off, press the settings icon (top right) and toggle the ```Reset Confirmation``` switch
- If the computer does not show up in the Bluetooth menu, ensure Bluetooth is on and the ```Bluetooth File Receive``` app is running
- To delete a race time, hold the lap time until the deletion popup appears
- To delete a race from the history, hold the file name until the deletion popup appears
- To resend an old race.xlsx file, press the settings icon in the top right corner, press ```View History```, select the file name, and then press ```Upload```
>[!IMPORTANT]
>To access this guide in the app, press the info icon (```i```) in the top right corner
