import dayjs from 'dayjs';
import { IReport } from 'app/shared/model/report.model';
import { IUser } from 'app/shared/model/user.model';

export interface IReportComments {
  id?: number;
  date?: string;
  reportComments?: IReport | null;
  commentedBy?: IUser | null;
}

export const defaultValue: Readonly<IReportComments> = {};
