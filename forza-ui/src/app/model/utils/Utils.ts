import * as moment from 'moment';
import 'moment/locale/pt-br';

export class Utils {

    public translateStatus(status: boolean) {
        return status ? 'Sim' : 'Não';
    }

    public formatterDate(date: string) {
        return moment(date).format('L');
    }

    public translateStatusForString(status: string) {
        return status ? 'Sim' : 'Não';
    }

    public getDBDate(date: string) {
        const newDate = moment(date).toDate();
        return newDate.toJSON().substr(0, 10);
    }
}
