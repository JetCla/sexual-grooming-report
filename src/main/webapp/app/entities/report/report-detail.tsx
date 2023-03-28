import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './report.reducer';

export const ReportDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reportEntity = useAppSelector(state => state.report.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reportDetailsHeading">
          <Translate contentKey="sexualGroomingReportsApp.report.detail.title">Report</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reportEntity.id}</dd>
          <dt>
            <span id="number">
              <Translate contentKey="sexualGroomingReportsApp.report.number">Number</Translate>
            </span>
          </dt>
          <dd>{reportEntity.number}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="sexualGroomingReportsApp.report.date">Date</Translate>
            </span>
          </dt>
          <dd>{reportEntity.date ? <TextFormat value={reportEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="officeAssigned">
              <Translate contentKey="sexualGroomingReportsApp.report.officeAssigned">Office Assigned</Translate>
            </span>
          </dt>
          <dd>{reportEntity.officeAssigned}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="sexualGroomingReportsApp.report.description">Description</Translate>
            </span>
          </dt>
          <dd>{reportEntity.description}</dd>
          <dt>
            <span id="location">
              <Translate contentKey="sexualGroomingReportsApp.report.location">Location</Translate>
            </span>
          </dt>
          <dd>{reportEntity.location}</dd>
          <dt>
            <Translate contentKey="sexualGroomingReportsApp.report.victim">Victim</Translate>
          </dt>
          <dd>{reportEntity.victim ? reportEntity.victim.id : ''}</dd>
          <dt>
            <Translate contentKey="sexualGroomingReportsApp.report.owner">Owner</Translate>
          </dt>
          <dd>{reportEntity.owner ? reportEntity.owner.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/report" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/report/${reportEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReportDetail;
