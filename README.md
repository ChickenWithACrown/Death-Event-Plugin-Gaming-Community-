# Death Event Plugin for Gaming Community

The Death Event Plugin is a Minecraft plugin designed for gaming communities to add dynamic challenges and tasks for players to complete. When players join the server, they are assigned a random task with a timer. If they fail to complete the task within the allotted time, they are kicked from the server. However, if they manage to complete the task before time runs out, they receive a "You Survived" message.

## Features

- Assigns random tasks to players upon joining the server.
- Displays a countdown timer and task information using Boss Bars.
- Automatically kicks players who fail to complete the task within the specified time.
- Provides feedback to players who successfully complete the task.
- Handles player disconnections and task removal gracefully.

## Usage

To start the Death Event, use the `/DeathEventStart` command. This will initiate the task timer for all online players.

To stop the Death Event, use the `/DeathEventStop` command. This will cancel the task timer and remove any remaining tasks.

## Installation

1. Download the plugin JAR file from the [releases page](https://github.com/ChickenWithACrown/Death-Event-Plugin-Gaming-Community-/releases).
2. Place the JAR file in the `plugins` folder of your Minecraft server.
3. Restart or reload your server to load the plugin.
4. To get to the .jar file follow the address `C:\Users\yourname\IdeaProjects\Death Event\src\main\java\deathevent\deathevent`
## Commands

- `/DeathEventStart`: Starts the Death Event and assigns tasks to players.
- `/DeathEventStop`: Stops the Death Event and removes all active tasks.

## Contributing

Contributions to the Death Event Plugin are welcome! If you have any ideas for improvements or new features, feel free to open an issue or submit a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
