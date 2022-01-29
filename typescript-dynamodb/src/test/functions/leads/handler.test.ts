

import LeadsDao from "@functions/leads/dao.service";
import { main } from "@functions/leads/handler";
import { Lead } from "@models/response";
import { diContainer } from "@src/DIRegister";
import sinon from "sinon";


describe("Leads handler", () => {

    // https://tech.gc.com/dependency-injection/
    test("should pass event to dao layer", async () => {
        // given
        let name = "DaveSoft";
        let type = "Company";
        let url = "www.somepage.com";
        let lead = {
            name: name,
            type: type,
            url: url
          };
        let leadsDao = sinon.createStubInstance(LeadsDao);
        diContainer.registerInstance("LeadsDao", leadsDao);
        let promise = new Promise<Lead>(resolve => resolve(lead));
        leadsDao.persist.returns(promise);

        // when
        let result = await main({
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

    afterEach(() => {
        diContainer.clearInstances();
    });
});