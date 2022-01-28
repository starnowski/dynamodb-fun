import 'reflect-metadata';
import { Lead } from "@models/response";
import DatabaseService from "@services/database.services";
import { inject, injectable } from "tsyringe";

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
            return {
                name: value.Attributes ? value.Attributes.name : lead.name,
                type: value.Attributes ? value.Attributes.type : lead.type,
                url: value.Attributes ? value.Attributes.url : lead.url
            };
        } );
    }
}