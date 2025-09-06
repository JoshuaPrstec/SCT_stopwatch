<!--suppress HtmlDeprecatedAttribute -->
<h1 align="center">
SCT stopwatch
</h1>

## Installation

### Android device

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

1. Press ```Start``` when the race starts
2. Press ```Lap``` for each finishing time
3. Once all times are recorded, press ```Stop```

>[!IMPORTANT]
>Make sure to lap the last time **before** pressing ```Stop```

To resume the stopwatch, press ```Resume```

### Uploading results (via computer)

1. [Windows only] Double-click on the ```Bluetooth File Receive``` app
2. On the Android device, press ```Upload```
3. Select a race distance (or enter a custom distance) and press ```Upload XLSX```
4. When the share menu appears, press ```Bluetooth```
5. Press the computer name
6. [Windows only] Select the desired download location
   
   [Mac only] The file will be automatically downloaded to the ```Downloads``` location
7. On the computer, copy the times in the .xlsx file (column B) and paste them into the ```times``` column of the master file

### Uploading results (via Appsheet)

1. On the Android device, press ```Upload```
2. Select a race distance (or enter a custom distance) and press ```Save CSV```
3. Open the AppSheet website and go to the Races tab
4. Press the stopwatch icon and select the CSV file
5. Wait for times to upload and sync, then verify that the times have correctly loaded into the race

## Troubleshooting

- To turn vibration on/off, press the settings icon (top right) and toggle the ```Vibration``` switch.
- If the vibration is not working, ensure touch feedback is enabled (Settings -> Sound and vibration -> Vibration and haptics -> Touch feedback)
- To turn the reset confirmation message on/off, press the settings icon (top right) and toggle the ```Reset Confirmation``` switch
- If the computer does not show up in the Bluetooth menu, ensure Bluetooth is on and the ```Bluetooth File Receive``` app is running
- To delete a race time, hold the lap time until the deletion popup appears
- To delete a race from the history, hold the file name until the deletion popup appears
- To resend an old race.xlsx file, press the settings icon (top right), press ```View History```, and select the file to load into the timer
>[!TIP]
>To access this guide in the app, press the info icon (```i```) in the top right corner
