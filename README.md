# Queensland University of Technology, Women In Technology Hackathon 2022

THE BIG SOUND's submission for the WIT Hackathon for 2022. The submission is a spooky game with a bot imposter.

# The basics of the game

A chat room is created with at least three players and a singler bot. The game will begin with the game posting a
conversation starter in chat. The players will have to respond to the starter trying to sound like the bot. A voting
round will begin after a few chat messages are exchanged and players will have to vote on who they believed was the bot.
Being voted as the bot, or successfully guessing the bot both award one point. The person with the most points at the
end of three rounds wins.

# Compiling

1. Clone this repository
2. Download the Maven dependencies
3. Download the latest version of cleverbotapi and store it at ./libs/cleverbotapi-1.0.3.jar
4. Compile or run com.thebigsound.recapturethegame.Launcher

# How to play

1. Go to the website, default port 7683.
2. Enter in your username, and the same Game ID as the other 2+ players.
3. Wait for the game countdown to start. Follow the dialogue.

# Contributors

- Jesse O'Sullivan (HTML, CSS, Game Design)
- Rhys B (Java Backend, JavaScript Frontend)
- Shreya Arora (Graphics Design, Game Design, Pitch Presentation)
- Julio Medeiros (HTML, CSS)

# Referenced material

1. https://github.com/miguelgrinberg/Flask-SocketIO-Chat (For our original Flask implementation. Difficulties with
   SocketIO, Flask, and multithreading lead to the decision to move to a backend Java implementation.)
2. https://github.com/RhysB/Legacy-Minecraft-Server-List-Receiver
3. https://www.w3schools.com
4. https://reqbin.com
5. https://getbootstrap.com/

# Languages/Technologies used during demonstration

- HTML, CSS, and JavaScript
- Java
- Jetty (Java Embedded Web Server)
- CleverBot
- [CleverBot Wrapper (Thanks to a293)](https://github.com/a2937/CleverBotAPI-Java/)
- Azure (Hosting)
- JQuery (HTML manipulation)
- Bootstrap

# Hackathon Notice

Please note, this game was coded in a quick manner for a 48-hour hackathon. As a result, this project doesn't always
follow the best conventions or follow best practices.