import * as sinon from 'sinon';
import DatabaseService, { PutItemOutput } from '@services/database.services';
import UserStatsDao from '@functions/user-stats/dao.service';


describe("UserStatsDao", () => {

  afterEach(function () {
    sinon.restore();
  });

  test("should save user_stats and pass values from response", async () => {
    // given
    let userStat = {
      user_id: "blond",
      timestamp: 9999999,
      weight: 88,
      blood_pressure: 110
    };
    let databaseService = sinon.createStubInstance(DatabaseService);
    let tested = new UserStatsDao(databaseService);
    let output = {
      Attributes: {
        user_id: "11223",
        timestamp: 9999999,
        weight: 64,
        blood_pressure: 107
      }
    };
    let promise = new Promise<PutItemOutput>(resolve => resolve(output));
    databaseService.create.returns(promise);
    
    // when
    let result = await tested.persist(userStat);

    // then
    expect(result).toBeDefined();
    expect(result.user_id).toEqual("11223");
    expect(result.timestamp).toEqual(9999999);
    expect(result.weight).toEqual(64);
    expect(result.blood_pressure).toEqual(107);
  });

  test("should save user_stats and pass values from request when no attributes in response were passed", async () => {
    // given
    let userStat = {
      user_id: "blond",
      timestamp: 9999999,
      weight: 88,
      blood_pressure: 110
    };
    let databaseService = sinon.createStubInstance(DatabaseService);
    let tested = new UserStatsDao(databaseService);
    let output = {
    };
    let promise = new Promise<PutItemOutput>(resolve => resolve(output));
    databaseService.create.returns(promise);
    
    // when
    let result = await tested.persist(userStat);

    // then
    expect(result).toBeDefined();
    expect(result.user_id).toEqual("blond");
    expect(result.timestamp).toEqual(9999999);
    expect(result.weight).toEqual(88);
    expect(result.blood_pressure).toEqual(110);
  });

});



