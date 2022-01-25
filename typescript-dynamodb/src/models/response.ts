

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
