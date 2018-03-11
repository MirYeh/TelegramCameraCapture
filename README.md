# Telegram Camera Capture

Capture images and videos from a web camera using a motion detection engine and stream it remotely via Telegram.

Running this program on your computer, you can start asking your Telegram Bot to capture images and videos and send them to your chat. Bots can be added to a group, such as a family telegram group, so that captured data can be shared with your family and friends.

- [x] capture images and videos
- [x] detect motion




## Supported Commands

_TelegramCameraCapture_ supports these following commands:

Command		| Description
------------|-------------
_image_		| Requests an image
_video_		| Requests a video
_detectOn_	| Starts motion detection
_detectOff_ | Stops motion detection

\* commands are case-insensitive




## Getting Started

1. [Download TelegramCameraCapture jar](https://github.com/MirYeh/TelegramCameraCapture/raw/master/extra/TelegramCameraCapture-0.0.1.jar)
2. Create a bot (search @BotFather on Telegram)
3. Save you bot token to a file named _CameraCaptureBotToken_ in the same folder as your jar file ([see example file](https://github.com/MirYeh/TelegramCameraCapture/blob/master/extra/CameraCaptureBotTokenExample))
4. Run TelegramCameraCapture jar
```
java -jar TelegramCameraCapture-0.0.1.jar
```
5. Start sending commands to your bot!


<img src="https://github.com/MirYeh/TelegramCameraCapture/blob/master/extra/screenshots/small-detect-on-off-command.png" alt="screenshot" title="detectOn command">





