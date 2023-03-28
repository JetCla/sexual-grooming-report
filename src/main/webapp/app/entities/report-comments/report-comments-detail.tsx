import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './report-comments.reducer';

export const ReportCommentsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reportCommentsEntity = useAppSelector(state => state.reportComments.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reportCommentsDetailsHeading">
          <Translate contentKey="sexualGroomingReportsApp.reportComments.detail.title">ReportComments</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reportCommentsEntity.id}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="sexualGroomingReportsApp.reportComments.date">Date</Translate>
            </span>
          </dt>
          <dd>
            {reportCommentsEntity.date ? <TextFormat value={reportCommentsEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="sexualGroomingReportsApp.reportComments.reportComments">Report Comments</Translate>
          </dt>
          <dd>{reportCommentsEntity.reportComments ? reportCommentsEntity.reportComments.id : ''}</dd>
          <dt>
            <Translate contentKey="sexualGroomingReportsApp.reportComments.commentedBy">Commented By</Translate>
          </dt>
          <dd>{reportCommentsEntity.commentedBy ? reportCommentsEntity.commentedBy.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/report-comments" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/report-comments/${reportCommentsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReportCommentsDetail;
