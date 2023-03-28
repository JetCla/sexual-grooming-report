import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './report-log.reducer';

export const ReportLogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reportLogEntity = useAppSelector(state => state.reportLog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reportLogDetailsHeading">
          <Translate contentKey="sexualGroomingReportsApp.reportLog.detail.title">ReportLog</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reportLogEntity.id}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="sexualGroomingReportsApp.reportLog.date">Date</Translate>
            </span>
          </dt>
          <dd>{reportLogEntity.date ? <TextFormat value={reportLogEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="change">
              <Translate contentKey="sexualGroomingReportsApp.reportLog.change">Change</Translate>
            </span>
          </dt>
          <dd>{reportLogEntity.change}</dd>
          <dt>
            <Translate contentKey="sexualGroomingReportsApp.reportLog.reportLogs">Report Logs</Translate>
          </dt>
          <dd>{reportLogEntity.reportLogs ? reportLogEntity.reportLogs.id : ''}</dd>
          <dt>
            <Translate contentKey="sexualGroomingReportsApp.reportLog.updatedBy">Updated By</Translate>
          </dt>
          <dd>{reportLogEntity.updatedBy ? reportLogEntity.updatedBy.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/report-log" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/report-log/${reportLogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReportLogDetail;
