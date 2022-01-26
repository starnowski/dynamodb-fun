import LeadsDao from '@functions/leads/dao.service';
import * as sinon from 'sinon';
import { Lead } from 'src/models/response';
import DatabaseService, { PutItemOutput } from 'src/services/database.services';


describe("LeadsDao", () => {

  afterEach(function () {
    sinon.restore();
  });

  test("should save lead", async () => {
    // given
    let lead:Lead = {
      name: "xxx",
      type: "xxx-business",
      url: "www.xxx.com"
    };
    let databaseService = sinon.createStubInstance(DatabaseService);
    let tested = new LeadsDao(databaseService);
    let output:PutItemOutput = {
      Attributes: {
        name: "11223",
        type: "secret",
        url: "www.yyy.eu"
      }
    };
    let promise:Promise<PutItemOutput> = new Promise<PutItemOutput>(resolve => resolve(output));
    sinon.stub(databaseService, "create").returns(promise);
    
    
    // when
    let result = await tested.persist(lead);

    // then
    expect(result).toBeDefined();
  });
});



