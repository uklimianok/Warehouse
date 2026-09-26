const token = prompt("Paste access token");

const client = new StompJs.Client({
    brokerURL: `ws://${location.host}/ws`,  // Gets host and port it was loaded from
    connectHeaders: { Authorization: `Bearer ${token}` },
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