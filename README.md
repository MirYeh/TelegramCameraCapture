# Telegram Camera Capture

A desktop service for capturing images and videos using a motion detection engine and streaming them remotely via Telegram

Running this program on your computer, you can start asking your Telegram Bot to capture images and videos and send them to your Telegram chat. Bots can be added to a group chat, such as a family telegram group, so that captured media can be shared with your family and friends.



## Main Features

- [x] Capture images and videos
- [x] Activate a motion detection engine
- [x] Stream media remotely via Telegram
- [x] Share media with friends and family




## Supported Commands

_TelegramCameraCapture_ supports these following commands:

Command*	| Description
------------|-------------
_image_		| Requests an image
_video_		| Requests a video
_detectOn_	| Starts motion detection
_detectOff_ | Stops motion detection

\* commands are case-insensitive




## Getting Started

1. [Download TelegramCameraCapture jar](https://github.com/MirYeh/TelegramCameraCapture/raw/master/extra/TelegramCameraCapture-0.0.1.jar)
2. [Create a bot](https://web.telegram.org/#/im?p=@BotFather)

<img src="https://github.com/MirYeh/TelegramCameraCapture/blob/master/extra/screenshots/small-create-bot.png" alt="screenshot" title="create bot">

3. Save you bot token to a file named _CameraCaptureBotToken_ in the same folder as your jar file ([see example token file](https://github.com/MirYeh/TelegramCameraCapture/blob/master/extra/CameraCaptureBotTokenExample))

4. Run TelegramCameraCapture jar
```
java -jar TelegramCameraCapture-0.0.1.jar
```

5. Start sending commands to your bot!


<img src="https://github.com/MirYeh/TelegramCameraCapture/blob/master/extra/screenshots/small-detect-on-off-command.png" alt="screenshot" title="detectOn command">





