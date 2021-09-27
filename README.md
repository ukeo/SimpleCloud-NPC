# SimpleCloud-NPC

This is the documentation about SimpleCloud-NPC.

## Getting Started

Download the plugin [HERE](https://imposdev.eu/repo/org/spigotmc/simplecloudnpc/2.0.3/simplecloudnpc-2.0.3.jar) and put it on your server.
You need to install ProtocolLib onto your server. You can download it [HERE](https://github.com/dmulloy2/ProtocolLib/releases/download/4.7.0/ProtocolLib.jar)

### Prerequisites

You need SimpleCloud 2.0.0 or above.
Spigot 1.8.x and above. Make sure to set the items in the config correctly for your version

## List of commands

/cloudnpc reload - Reload all npcs.

/cloudnpc create (skinName) (displayName) (shouldLookClose) (shouldImitate) (itemInHand) (configName) (serverGroup) (renderSkinLayer)

## Built With

* [PaperSpigot](https://papermc.io/downloads)
* [Maven](https://maven.apache.org/) - Dependency Management
* [NPC-Lib](https://github.com/juliarn/NPC-Lib) - Handle NPC

## Maven repository

```maven
<repository>
    <id>spigotRepo</id>
    <url>https://imposdev.eu/repo</url>
</repository>
```

```maven
<dependency>
    <groupId>org.spigotmc</groupId>
    <artifactId>simplecloudnpc</artifactId>
    <version>2.0.3</version>
</dependency>
```

## Authors

* **Espen** - *SimpleCloud-NPC* - [Espen](https://github.com/EhreGetaken)
* **Juliarn** - *NPC-Lib* - [Juliarn](https://github.com/juliarn)
