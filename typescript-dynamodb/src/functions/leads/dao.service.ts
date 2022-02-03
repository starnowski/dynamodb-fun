import 'reflect-metadata';
import { Lead } from "@models/response";
import { inject, injectable } from "tsyringe";
import { IDatabaseService } from '@src/services/database.services';
import { logInfo } from '@src/libs/logger';

@injectable()
export default class LeadsDao {

    constructor(
        // @Inject() 
        @inject("DatabaseService")
    public databaseService: IDatabaseService) { }

    persist(lead:Lead):Promise<Lead>  {
        logInfo("databaseService");
        logInfo(this.databaseService);
        return this.databaseService.create({
            TableName: "leads",
            Item: {
                name: lead.name,
                type: lead.type,
                url: lead.url
            }
        }).then((value) => {
            return {
                name: value.Attributes ? value.Attributes.name : lead.name,
                type: value.Attributes ? value.Attributes.type : lead.type,
                url: value.Attributes ? value.Attributes.url : lead.url
            };
        } );
    }
}