# livestreamerFM

Right now, this is just an extremely small command-line utility that uses [Livestreamer](http://livestreamer.tanuki.se/)
and [VLC](https://www.videolan.org/vlc/index.html) to create an audio http server that streams audio from your favorite 
livestreamer streams. 

LivestreamerFM is written in scala (and thus requires java to be installed).
Both VLC and Livestreamer must be installed before using this program.




##Command-line usage:

Run it from the command-line with these options:

    [STREAM_URL] [IP_ADDRESS] [PORT] [LIVESTREAMER_CONFIG_FILE_LOCATION]

Or: 

    --config [LIVESTREAMERFM_CONFIG_FILE_LOCATION] [STREAM_URL]

Example: 

    twitch.tv/arteezy localhost 9999 ~/configs 
Or: 

    --config ~/Desktop/LSFMconfig.txt twitch.tv/arteezy

`STREAM_URL`: The URL of the stream that you want to play.

`IP_ADDRESS`: The ip address that you want the http server to bind to.

`PORT`: The port that you want the http server to bind to.

`LIVESTREAMER_CONFIG_FILE_LOCATION`: This utility generates a config file that the Livestreamer app uses. This part of the
command tells livestreamerFM where to store the generated file. 

`LIVESTREAMERFM_CONFIG_FILE_LOCATION`: The location of the LivestreamerFM config file, format described below.

##LivestreamerFM config file format:

Ex:

    player = /Applications/VLC.app/Contents/MacOS/VLC
    delay = 5005
    ipAndPort = 192.168.1.1:9999
    livestreamerConfigLocation = /Users/gmgilmore/

The options that you can choose from are `player` , `delay` , `ipAndPort` , and `livestreamerConfigLocation`. All these
options are optional (heh) - you can include any of them, or none of them. They are also case-insensitive.

The file should be a plain text file with each new line having the following format:

    [OPTION] = [VALUE]
    
If you don't follow this format, livestreamerFM will throw an exception when it tries to read the file.

###Option descriptions:

`player` - The value should be path to the vlc executable on your machine. If this option is not used, LivestreamerFM will
attempt to guess where VLC is located on your machine. 

`delay` - The value should be number of milliseconds that you want LivestreamerFM to cache the audio for. This is used to
improve performance and mitigate stuttering. If this option is not used, `delay` will default to 5000 ms.

`ipAndPort` - The value should the IP address that you want the audio server to bind to, followed by the port that you want
the audio server to use, separated by a colon. If this option is not used, `ipAndPort` will default to `localhost:9999`.

`livestreamerConfigLocation` - The value should be the path that you want the livestreamer config file that is generated
by livestreamerFM to be saved in. If this option is not used, `livestreamerConfigLocation` will default to the directory
that livestreamerFM is running in. 

##TODO:
-~~add config file support so that you don't have to type in all these commands every time~~ Done.

-~~add support for sites other than twitch.tv and youtube~~ Done-ish. I figured out how to get vlc to only stream the 
audio from a video, but I haven't tested this with every single site on the supported site list for livestreamer.

-~~add basic parsing of url so that there is an url -> audio_option matching~~ Done.

-~~add support for windows and linux (this involves figuring out how to look through the path maybe), will involve some 
refactoring~~ Done. LivestreamerFM now attempts to guess the path that vlc installed in for windows, linux, and OSX. 
Users can also use the --config option to specify the vlc path manually using the config file.

-write basic webui

