# BotServer-kt

## About
A game that is meant to be played by two computers. Based on the game played in _Ender's Game_. This server was created to surpass the ruby one in speed, security, and stability. The development was halted when the ruby one was finished, but after the discovery of massive bugs that would require a rewrite of the bot and team managment in the ruby, BotServer-kt's development was resumed.

## Building
1. Download or Clone
2. Create a directory called ```libs```
3. Download and ```gradlew build``` and place the jar in the ```libs``` directory [link](https://github.com/n9Mtq4/KotlinExtLib)
4. Download the latest release of the BotGraphics and place it in the ```libs``` directory [link](https://github.com/RMHSProgrammingClub/Bot-Graphics/releases/latest)
5. ```gradlew build``` the server
6. Jar is located in ```./build/libs/```

## Security
This server makes sure that every command sent is acceptable twice. Once with regex, and once with native java string parsing. Any bugs that are found that are not "features" will promptly patched. We want the game to be fair for everyone. If you find a bug, you can use it, but expect that as soon as you exploit anything it will be found.
