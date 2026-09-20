const client = new StompJs.Client({
    brokerURL: "ws://localhost:8280/ws",
    onConnect: () => {
        console.log("Connected!");

        client.subscribe("/topic/test", (message) => {
            console.log("Broadcast message: ", message.body);
        });

        client.subscribe("/user/queue/test", (message) => {
            console.log("Private message: ", message.body);
        });

        client.subscribe("/user/queue/notify", (message) => {
            console.log("New notification: ", message.body);
        });

        client.publish({
            destination: "/app/greet",
            body: JSON.stringify("A new user connected!")
        });

        client.publish({
            destination: "/app/greet-private",
            body: JSON.stringify("Hello user!")
        });
    }
});

client.activate();