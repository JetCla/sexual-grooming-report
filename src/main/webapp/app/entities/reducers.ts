import report from 'app/entities/report/report.reducer';
import reportLog from 'app/entities/report-log/report-log.reducer';
import reportComments from 'app/entities/report-comments/report-comments.reducer';
import victim from 'app/entities/victim/victim.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  report,
  reportLog,
  reportComments,
  victim,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
