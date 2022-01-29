import * as sinon from 'sinon';
import DatabaseService, { PutItemOutput, QueryItemOutput } from '@services/database.services';
import UserStatsDao from '@functions/user-stats/dao.service';
import { IUserStatQueryRequest } from '@src/models/response';


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

  test("should pass user_id and limit for query request", async () => {
    // given
    let userStatQueryRequet:IUserStatQueryRequest = {
      userId: "blond",
      limit: 37
    };
    let passedQueryInput = null;
    let databaseService = sinon.createStubInstance(DatabaseService);
    let tested = new UserStatsDao(databaseService);
    let output = {
      Items: [
        {
          user_id: "blond",
          timestamp: 444,
          weight: 26,
          blood_pressure: 105
        },
        {
          user_id: "blondXXX",//Normally it would be the same user_id
          timestamp: 7775,
          blood_pressure: 65
        }
      ]
    };
    let expectedUserStatsResults = [
      {
        user_id: "blond",
        timestamp: 444,
        weight: 26,
        blood_pressure: 105
      },
      {
        user_id: "blondXXX",//Normally it would be the same user_id
        timestamp: 7775,
        weight: null,
        blood_pressure: 65
      }
    ];
    let promise = new Promise<QueryItemOutput>(resolve => resolve(output));
    databaseService.query.callsFake(input => {passedQueryInput = input; return promise;});
    
    // when
    let result = await tested.query(userStatQueryRequet);

    // then
    expect(result).toEqual(expectedUserStatsResults);
    // verify input
    expect(passedQueryInput.Limit).toEqual(37);
    expect(passedQueryInput.TableName).toEqual("user_stats");
    expect(passedQueryInput.KeyConditionExpression).toEqual("user_id = :val1");
    expect(passedQueryInput.ExpressionAttributeValues).toEqual({
      ":val1": "blond"
    });
  });

});



