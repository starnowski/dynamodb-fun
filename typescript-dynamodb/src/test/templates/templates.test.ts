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

});