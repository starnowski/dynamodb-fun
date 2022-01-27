import 'reflect-metadata';
import { Lead } from "src/models/response";
import DatabaseService from "src/services/database.services";
import { inject, injectable } from "tsyringe";
// import { Inject, Service } from "typedi";

// @Service()
@injectable()
export default class LeadsDao {

    constructor(
        // @Inject() 
        @inject("DatabaseService")
    public databaseService: DatabaseService) { }

    persist(lead:Lead):Promise<Lead>  {
        console.log("databaseService");
        console.log(this.databaseService);
        return this.databaseService.create({
            TableName: "leads",
            Item: {
                name: lead.name,
                type: lead.type,
                url: lead.url
            }
        }).then((value) => {
            value.Attributes;
            return {
                name: value.Attributes.name,
                type: value.Attributes.type,
                url: value.Attributes.url !== undefined ? value.Attributes.url : null
            };
        } );
    }
}