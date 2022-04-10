
describe("VTL template", () => {

    var mappingTemplate = require('api-gateway-mapping-template');

    // https://github.com/ToQoz/api-gateway-mapping-template
    test("should pass event to dao layer", async () => {
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

});