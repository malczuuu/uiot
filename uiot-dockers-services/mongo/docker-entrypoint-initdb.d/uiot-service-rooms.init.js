db = db.getSiblingDB("uiot-service-rooms");

db.createCollection("rooms");

db.runCommand({
    createIndexes: "rooms",
    indexes: [{
        "name": "room_unique",
        "key": {
            "room": -1
        },
        "unique": true
    }]
});
