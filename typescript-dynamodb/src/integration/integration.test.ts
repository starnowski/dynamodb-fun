import { diContainer } from "@src/DIRegister";
import { main as leadsMain} from "@src/functions/leads/handler";
import IConfig from "@src/services/config.interface";
import AWS from "aws-sdk";

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
});

