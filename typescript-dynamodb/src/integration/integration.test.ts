import { diContainer } from "@src/DIRegister";
import { main as leadsMain} from "@src/functions/leads/handler";
import { main as userStatMain } from "@src/functions/user-stats/handler";
import IConfig from "@src/services/config.interface";
import AWS from "aws-sdk";
import _ from "lodash";

var ServerMock = require("mock-http-server");
var portfinder = require('portfinder');
jest.setTimeout(120000);

describe('Integration tests', function() {

    var server:any;

    beforeAll(async function() {
        jest.setTimeout(100000);
        let generatedPort = null;
        await portfinder.getPortPromise({
        port: 9000,    // minimum port
        stopPort: 9500 // maximum port
        }).then((port) => {
            console.log("found port: " + port);
                generatedPort = port;
        })
        .catch((err) => {
            console.log("error during port picking: " + err);
        });
        server = new ServerMock({ host: "localhost", port: generatedPort });
        console.log("found port is " + generatedPort);

        // Setting AWS config
        const config: IConfig = { region: "eu-west-1" };
        config.region="us-east-1";
        config.accessKeyId="DUMMYIDEXAMPLE";
        config.secretAccessKey="DUMMYEXAMPLEKEY";
        config.endpoint="http://localhost:" + generatedPort;
        AWS.config.update(config);
        const documentClient = new AWS.DynamoDB.DocumentClient();
        diContainer.registerInstance("DocumentClient", documentClient);
    });

    beforeEach(function(done) {
        jest.setTimeout(60000);
        server.start(done);

    });

    afterEach(function(done) {
        server.stop(done);
    });

    test("should save lead", async () => {
        // given
        jest.setTimeout(120000);
        server.on({
            method: 'POST',
            path: '/',
            filter: function (req: { body: any; }) {
                console.log("request body is: " + req.body);
                return _.isEqual(JSON.parse(req.body), {"TableName":"leads","Item":{"name":{"S":"Simon"},"type":{"S":"Company"},"url":{"S":"www.dog.com"}}} );
              },
            reply: {
                status:  200,
                headers: { "content-type": "application/json" },
                body:    JSON.stringify({})
            }
        });
        let name = "Simon";
        let type = "Company";
        let url = "www.dog.com";
        let lead = {
            name: name,
            type: type,
            url: url
          };

        // when
        let result = await leadsMain({
            body: JSON.stringify({
                name: name,
                type: type,
                url: url
            }),
            path: "leads",
            httpMethod: "POST",
            headers: {
                "Content-Type": 'application/json'
            }
        }, null);

        // then
        expect(result.statusCode).toEqual(200);
        expect(result.body).toEqual(JSON.stringify(lead));
    });

    test("should save user_stat", async () => {
        // given
        jest.setTimeout(120000);
        server.on({
            method: 'POST',
            path: '/',
            filter: function (req: { body: any; }) {
                console.log("request body is: " + req.body);
                return _.isEqual(JSON.parse(req.body), {"TableName":"user_stats","Item":{"user_id":{"S":"113"},"timestamp":{"N":"1643472357270471"},"weight":{"N":"83"},"blood_pressure":{"N":"123"}}} );
              },
            reply: {
                status:  200,
                headers: { "content-type": "application/json" },
                body:    JSON.stringify({})
            }
        });
        let request = {
            "user_id": "113", 
            "timestamp": "2022-01-29T16:05:57.270471", 
            "weight": 83, 
            "blood_pressure": 123
          };

        // when
        let result = await userStatMain({
            body: JSON.stringify(request),
            path: "/user_stats",
            httpMethod: "POST",
            headers: {
                "Content-Type": 'application/json'
            }
        }, null);

        // then
        expect(result.statusCode).toEqual(200);
        expect(result.body).toEqual(JSON.stringify(request));
    });

    test("should query user_stat", async () => {
        // given
        jest.setTimeout(120000);
        server.on({
            method: 'POST',
            path: '/',
            filter: function (req: { body: any; }) {
                console.log("request body is: " + req.body);
                return _.isEqual(JSON.parse(req.body), {"TableName":"user_stats","KeyConditionExpression":"user_id = :val1","ExpressionAttributeValues":{":val1":{"S":"113"}}} );
              },
            reply: {
                status:  200,
                headers: { "content-type": "application/json" },
                body:    "{\"Items\":[{\"weight\":{\"N\":\"83\"},\"blood_pressure\":{\"N\":\"123\"},\"user_id\":{\"S\":\"113\"},\"timestamp\":{\"N\":\"1643472357270471\"}}],\"Count\":1,\"ScannedCount\":1}"
            }
        });
        let request = {
            "user_id": "113"
        };
        let response = "{\"results\":[{\"user_id\":\"113\",\"timestamp\":\"2022-01-29T16:05:57.270471\",\"weight\":83,\"blood_pressure\":123}]}";

        // when
        let result = await userStatMain({
            body: JSON.stringify(request),
            path: "/user_stats/search",
            httpMethod: "POST",
            headers: {
                "Content-Type": 'application/json'
            }
        }, null);

        // then
        expect(result.statusCode).toEqual(200);
        expect(result.body).toEqual(response);
    });
});

