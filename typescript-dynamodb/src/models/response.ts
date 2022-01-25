import { Any } from "json-schema-to-ts/lib/meta-types";


interface IUserStat {
    user_id: string;
    timestamp: number;
    weight?: number;
    blood_pressure?: number;
}

class UserStat implements IUserStat{
    user_id: string;
    timestamp: number;
    weight?: number;
    blood_pressure?: number;
}

interface IResponse {
    results: IUserStat[];
}

interface IUserStatQueryRequest {

    userId: string;
    after_timestamp?: number;
    limit?: number;
}

interface Leads {

    name: string;
    type: string;
    url?: string;
}

export interface IResponseModel<Type> {
    body: Type;
    code: number;
}

export abstract class AbstractResponseModel<Type> implements IResponseModel<Type> {
    body: Type;
    code: number;
}

export default class ResponseModel extends AbstractResponseModel<any>{
    body: any;
    code: number;

    constructor(data = {}, code = 402, message = '') {
        super();
        this.body = {
            data: data,
            message: message
        };
        this.code = code;
    }

}