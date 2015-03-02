# livestreamerFM

Right now, this is just an extremely small commandline utility that uses Livestreamer (http://livestreamer.tanuki.se/) and VLC to create an audio http server that streams audio from your favorite livestreamer streams. It is written in scala (and thus it requires java to be installed), and currently supports only OSX. Multiplatform support is incomming shortly.

At the moment, VLC must be located at "/Applications/VLC.app/Contents/MacOS/VLC" (which is the default installation location).

##Usage:

Run it from the command line with these options: 

[STREAM_URL] [IP_ADDRESS] [PORT] [LIVESTREAMER_CONFIG_FILE_LOCATION]

Example: twitch.tv/arteezy localhost 9999 ~/configs

STREAM_URL: The URL of the stream that you want to play.

IP_ADDRESS: The ip address that you want the http server to bind to. 

PORT: The port that you want the http server to bind to. 

LIVESTREAMER_CONFIG_FILE_LOCATION: This utility generates a config file that the Livestreamer app uses. This part of the command tells livestreamerFM where to store the generated file. 

##TODO:
-add config file support so that you don't have to type in all these commands every time 

-~~add support for sites other than twitch.tv and youtube~~ Done-ish. I figured out how to get vlc to only stream the audio from a video, but I haven't tested this with every single
site on the supported site list for livestreamer.

-~~add basic parsing of url so that there is an url -> audio_option matching~~ Done.

-add support for windows and linux (this involves figuring out how to look through the path maybe), will involve some refactoring

-write basic webui

