

import { UserStat } from "@models/response";
import { diContainer } from "@src/DIRegister";
import UserStatsDao from "@src/functions/user-stats/dao.service";
import { main } from "@src/functions/user-stats/handler";
import sinon from "sinon";


describe("UserStats handler", () => {

    // https://tech.gc.com/dependency-injection/
    test("should pass event to dao layer", async () => {
        // given
        let userStat = {
            "user_id": "1", 
            "timestamp": 1643468757270, 
            "weight": 83, 
            "blood_pressure": 123
          };
        let userStatsDao = sinon.createStubInstance(UserStatsDao);
        diContainer.registerInstance("UserStatsDao", userStatsDao);
        let promise = new Promise<UserStat>(resolve => resolve(userStat));
        userStatsDao.persist.returns(promise);
        let request = {
            "user_id": "1", 
            "timestamp": "2022-01-29T16:05:57.270471", 
            "weight": 83, 
            "blood_pressure": 123
          };

        // when
        let result = await main({
            body: JSON.stringify(request),
            path: "user_stats",
            httpMethod: "POST",
            headers: {
                "Content-Type": 'application/json'
            }
        }, null);

        // then
        expect(result.statusCode).toEqual(200);
        expect(result.body).toEqual(JSON.stringify(request));
    });

    afterEach(() => {
        diContainer.clearInstances();
    });
});