# Telegram Camera Capture

Capture images and videos from a web camera using a motion detection engine and stream it remotely via Telegram.

[x] capture images and videos
[x] detect motion

Running this program on your computer, you can start asking your Telegram Bot to capture images and videos and send them to your chat. Bots can be added to a group, such as a family telegram group, so that captured data can be shared with your family and friends.


* [Supported Commands](https://github.com/MirYeh/TelegramCameraCapture#supported-commands)
* [Getting Started](https://github.com/MirYeh/TelegramCameraCapture#getting-started)




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

1. [Download TelegramCameraCapture jar][link to jar]
2. Create a bot (search @BotFather on Telegram)
3. Save you bot token to a file named _CameraCaptureBotToken_ in the same folder as your jar file ([see example file][link to botTokenExampleFile]
4. Run TelegramCameraCapture jar
```html
run -jar <i>jarname</i>.jar
```
5. Start sending commands to your bot!


<img src="https://github.com/MirYeh/TelegramCameraCapture/blob/master/extra/screenshots/detect-on-off-command.png" alt="detect on and off command screenshot" width="50%" height="50%">

<img src="https://github.com/MirYeh/TelegramCameraCapture/blob/master/extra/screenshots/video-command.png" alt="video command screenshot" width="50%" height="50%">








[link to jar]:(https://github.com/MirYeh/TelegramCameraCapture/raw/master/extra/TelegramCameraCapture-0.0.1.jar)

[link to botTokenExampleFile]:(https://github.com/MirYeh/TelegramCameraCapture/blob/master/extra/CameraCaptureBotTokenExample)

