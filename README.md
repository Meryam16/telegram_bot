# TelegramBot
Here's a telegram bot which asks questions about the tour u want to make and collects data and sends you an offer from the tour agents
The whole application consists of 2 modules front app and bot app. Front app is responsible for getting data from DB and with the restApi to send to Frontend 
and also get data from frontend and send to Queue (RabbitMQ). Bot app is responsible for managing bot commands getting questions from db and respectively asking
to the user and also saving user answers in the dataBase. Also bot app has a RabbitListener queue which listens if there's any sent data from AdminPanel in 
frontend.
Used technology sets are PostgreSql, Redis, RabbitMQ, DockerCompose and as a language used Java.
