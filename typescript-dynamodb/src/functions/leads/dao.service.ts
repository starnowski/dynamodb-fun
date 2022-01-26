import { Lead } from "src/models/response";
import DatabaseService from "src/services/database.services";

const databaseService = new DatabaseService();

export default class LeadsDao {

    persist(lead:Lead):Promise<any>  {
        return databaseService.create({
            TableName: "leads",
            Item: {
                name: lead.name,
                type: lead.type,
                url: lead.url
            }
        });
    }
}