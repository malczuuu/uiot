db = db.getSiblingDB("uiot-service-things");

db.createCollection("things");

db.runCommand({
    createIndexes: "things",
    indexes: [{
        "name": "thing_unique",
        "key": {
            "room": -1,
            "thing": -1
        },
        "unique": true
    }]
});
