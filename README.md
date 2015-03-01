# livestreamerFM

Right now, this is a just small commandline utility that uses Livestreamer (http://livestreamer.tanuki.se/) and VLC to create an audio http server that streams audio from twitch.tv and youtube.com streams. It is written in scala (and thus it requires java to be installed), and currently supports only OSX. Multiplatform support is incomming shortly.

At the moment, livestreamer must be located at "/usr/local/bin/livestreamer" and VLC must be located at "/Applications/VLC.app/Contents/MacOS/VLC" (both of which are the default installation locations). 

##Usage:

Run it from the command line with these options: 

[STREAM_URL] [AUDIO_OPTION] [IP_ADDRESS] [PORT] [LIVESTREAMER_CONFIG_FILE_LOCATION]

Example: twitch.tv/arteezy audio localhost 9999 ~/configs

STREAM_URL: The URL of the stream that you want to play. At the moment, only twitch and youtube are supported. 

AUDIO_OPTION: The specific audio option to pass to livestreamer. At the moment this is dictated by whatever the corresponding livestreamer plugin says the name of the audio option is. For twitch it is "audio" and for youtube it's "audio_mp4".

IP_ADDRESS: The ip address that you want the http server to bind to. 

PORT: The port that you want VLC to use. 

LIVESTREAMER_CONFIG_FILE_LOCATION: This utility generates a config file that the Livestreamer app uses. This part of the command tells livestreamerFM where to store the generated file. 

