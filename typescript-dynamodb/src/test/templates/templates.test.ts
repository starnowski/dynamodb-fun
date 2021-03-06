import * as fs from "fs";
import * as path from "path";

describe("VTL template", () => {

    var mappingTemplate = require('api-gateway-mapping-template');

    // https://github.com/ToQoz/api-gateway-mapping-template
    test("should produce correct payload", async () => {
        // given
        var vtl = '$input.json(\'$.data\')';
        var payload = '{"data": {"url": "https://github.com/ToQoz/api-gateway-mapping-template"}}';

        // when
        let result = mappingTemplate({template: vtl, payload: payload});

        // then
        expect(JSON.parse(result)).toEqual({
            "url": "https://github.com/ToQoz/api-gateway-mapping-template"
          });
    });

    test("should produce correct payload for file", async () => {
        // given
        var vtl = fs.readFileSync(path.join(__dirname, './velocity-template.vm'), { encoding: 'utf8' });
        var payload = JSON.stringify({ name: 'David' });

        // when
        console.log(payload);
        let result = mappingTemplate({template: vtl, payload: payload});

        // console.log(result);
        // then
        expect(JSON.parse(result)).toEqual({
            "hello": "David"
          });
    });

    test("should print headers", async () => {
        // given
        var vtl = fs.readFileSync(path.join(__dirname, './velocity-template_2.vm'), { encoding: 'utf8' });
        var payload = JSON.stringify({ name: 'David' });

        // when
        console.log(payload);
        let result = mappingTemplate({template: vtl, payload: payload, params: { header: { "some_strange_name": "XXX" } }});

        console.log(result);
        // then
        expect(JSON.parse(result)).toEqual({
            "params": {
                "header": {
                    "some_strange_name": "XXX"
                }
            }
        }
        );
    });

    test("should print error message", async () => {
        // given
        var vtl = fs.readFileSync(path.join(__dirname, './velocity-template_3.vm'), { encoding: 'utf8' });
        var payload = JSON.stringify({ name: 'David' });

        // when
        console.log(payload);
        let result = mappingTemplate({template: vtl, payload: payload, params: { header: { "some_strange_name": "XXX" } },
            context: {
                error: {
                    messageString: "Invalid request"
                }
            } });

        console.log(result);
        // then
        expect(JSON.parse(result)).toEqual({
            "message": "Invalid request"
        }
        );
    });

    test("should print error message and conversation id", async () => {
        // given
        var vtl = fs.readFileSync(path.join(__dirname, './velocity-template_3.vm'), { encoding: 'utf8' });
        var payload = JSON.stringify({ name: 'David' });

        // when
        console.log(payload);
        let result = mappingTemplate({template: vtl, payload: payload, params: { header: { "con_id": "4345-4345-342" } },
            context: {
                error: {
                    messageString: "Invalid request with conversation id"
                }
            } });

        console.log(result);
        // then
        expect(JSON.parse(result)).toEqual({
            "message": "Invalid request with conversation id",
            "conversation_id": "4345-4345-342"
        }
        );
    });

});