const client = new StompJs.Client({
    brokerURL: "ws://localhost:8280/ws",
    onConnect: () => {
        console.log("Connected!");

        client.subscribe("/topic/connect", (message) => {
            console.log(message.body);
        });

        client.subscribe("/user/queue/notify", (message) => {
            console.log("New notification: ", message.body);
        });

        client.publish({
            destination: "/app/connect",
            body: JSON.stringify("is connected.")
        });
    }
});

client.activate();