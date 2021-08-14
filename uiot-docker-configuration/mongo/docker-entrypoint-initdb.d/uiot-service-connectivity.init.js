db = db.getSiblingDB("uiot-service-connectivity");

db.createCollection("connectivity");

db.runCommand({
    createIndexes: "connectivity",
    indexes: [{
        "name": "room_thing_unique",
        "key": {
            "room": -1,
            "thing": -1
        },
        "unique": true
    }]
});
