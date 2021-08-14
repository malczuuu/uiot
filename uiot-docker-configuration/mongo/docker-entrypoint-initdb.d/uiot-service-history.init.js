db = db.getSiblingDB("uiot-service-history");

db.createCollection("thing_events");

db.runCommand({
    createIndexes: "thing_events",
    indexes: [{
        "name": "uuid",
        "key": {
            "uuid": -1
        },
        "unique": true
    }, {
        "name": "room",
        "key": {
            "room": -1
        }
    }, {
        "name": "room_thing",
        "key": {
            "room": -1,
            "thing": -1
        }
    }, {
        "name": "room_thing_time",
        "key": {
            "room": -1,
            "thing": -1,
            "time": -1
        }
    }, {
        "name": "room_thing_arrivalTime",
        "key": {
            "room": -1,
            "thing": -1,
            "arrivalTime": -1
        }
    }]
});
