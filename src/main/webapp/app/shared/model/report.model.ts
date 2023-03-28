import dayjs from 'dayjs';
import { IReportComments } from 'app/shared/model/report-comments.model';
import { IReportLog } from 'app/shared/model/report-log.model';
import { IVictim } from 'app/shared/model/victim.model';
import { IUser } from 'app/shared/model/user.model';

export interface IReport {
  id?: number;
  number?: string;
  date?: string;
  officeAssigned?: string | null;
  description?: string | null;
  location?: string | null;
  reportComments?: IReportComments[] | null;
  reportLogs?: IReportLog[] | null;
  victim?: IVictim | null;
  owner?: IUser | null;
}

export const defaultValue: Readonly<IReport> = {};
