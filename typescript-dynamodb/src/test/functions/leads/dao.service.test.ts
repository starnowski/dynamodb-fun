import LeadsDao from '@functions/leads/dao.service';
import * as sinon from 'sinon';
import DatabaseService, { PutItemOutput } from '@services/database.services';


describe("LeadsDao", () => {

  afterEach(function () {
    sinon.restore();
  });

  test("should save lead", async () => {
    // given
    let lead = {
      name: "xxx",
      type: "xxx-business",
      url: "www.xxx.com"
    };
    let databaseService = sinon.createStubInstance(DatabaseService);
    let tested = new LeadsDao(databaseService);
    let output = {
      Attributes: {
        name: "11223",
        type: "secret",
        url: "www.yyy.eu"
      }
    };
    let promise = new Promise<PutItemOutput>(resolve => resolve(output));
    databaseService.create.returns(promise);
    
    // when
    let result = await tested.persist(lead);

    // then
    expect(result).toBeDefined();
    expect(result.name).toEqual("11223");
    expect(result.type).toEqual("secret");
    expect(result.url).toEqual("www.yyy.eu");
  });

  test("should save lead without url", async () => {
    // given
    let lead = {
      name: "xxx",
      type: "xxx-business",
      url: "www.aaaa.com"
    };
    let databaseService = sinon.createStubInstance(DatabaseService);
    let tested = new LeadsDao(databaseService);
    let output = {
      Attributes: {
        name: "2233",
        type: "true"
      }
    };
    let promise = new Promise<PutItemOutput>(resolve => resolve(output));
    databaseService.create.returns(promise);
    
    // when
    let result = await tested.persist(lead);

    // then
    expect(result).toBeDefined();
    expect(result.name).toEqual("2233");
    expect(result.type).toEqual("true");
    expect(result.url).toBeUndefined();
  });
});



