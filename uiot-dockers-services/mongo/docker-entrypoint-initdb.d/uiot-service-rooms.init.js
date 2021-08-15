db = db.getSiblingDB("uiot-service-rooms");

db.createCollection("rooms");

db.runCommand({
    createIndexes: "rooms",
    indexes: [{
        "name": "uid_unique",
        "key": {
            "uid": -1
        },
        "unique": true
    }]
});
