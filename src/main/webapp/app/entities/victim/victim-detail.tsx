import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './victim.reducer';

export const VictimDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const victimEntity = useAppSelector(state => state.victim.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="victimDetailsHeading">
          <Translate contentKey="sexualGroomingReportsApp.victim.detail.title">Victim</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{victimEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="sexualGroomingReportsApp.victim.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{victimEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="sexualGroomingReportsApp.victim.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{victimEntity.lastName}</dd>
          <dt>
            <span id="age">
              <Translate contentKey="sexualGroomingReportsApp.victim.age">Age</Translate>
            </span>
          </dt>
          <dd>{victimEntity.age}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="sexualGroomingReportsApp.victim.city">City</Translate>
            </span>
          </dt>
          <dd>{victimEntity.city}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="sexualGroomingReportsApp.victim.state">State</Translate>
            </span>
          </dt>
          <dd>{victimEntity.state}</dd>
          <dt>
            <span id="country">
              <Translate contentKey="sexualGroomingReportsApp.victim.country">Country</Translate>
            </span>
          </dt>
          <dd>{victimEntity.country}</dd>
          <dt>
            <span id="observations">
              <Translate contentKey="sexualGroomingReportsApp.victim.observations">Observations</Translate>
            </span>
          </dt>
          <dd>{victimEntity.observations}</dd>
        </dl>
        <Button tag={Link} to="/victim" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/victim/${victimEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VictimDetail;
