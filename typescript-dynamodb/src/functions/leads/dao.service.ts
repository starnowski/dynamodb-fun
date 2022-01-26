import { Lead } from "src/models/response";
import DatabaseService from "src/services/database.services";
import { Service } from "typedi";

@Service()
export default class LeadsDao {

    constructor(private readonly databaseService: DatabaseService) { }
    persist(lead:Lead):Promise<Lead>  {
        return this.databaseService.create({
            TableName: "leads",
            Item: {
                name: lead.name,
                type: lead.type,
                url: lead.url
            }
        }).then();
    }
}