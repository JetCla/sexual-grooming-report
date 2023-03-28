import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVictim } from 'app/shared/model/victim.model';
import { getEntity, updateEntity, createEntity, reset } from './victim.reducer';

export const VictimUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const victimEntity = useAppSelector(state => state.victim.entity);
  const loading = useAppSelector(state => state.victim.loading);
  const updating = useAppSelector(state => state.victim.updating);
  const updateSuccess = useAppSelector(state => state.victim.updateSuccess);

  const handleClose = () => {
    navigate('/victim' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...victimEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...victimEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sexualGroomingReportsApp.victim.home.createOrEditLabel" data-cy="VictimCreateUpdateHeading">
            <Translate contentKey="sexualGroomingReportsApp.victim.home.createOrEditLabel">Create or edit a Victim</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="victim-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sexualGroomingReportsApp.victim.firstName')}
                id="victim-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
              />
              <ValidatedField
                label={translate('sexualGroomingReportsApp.victim.lastName')}
                id="victim-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
              />
              <ValidatedField
                label={translate('sexualGroomingReportsApp.victim.age')}
                id="victim-age"
                name="age"
                data-cy="age"
                type="text"
              />
              <ValidatedField
                label={translate('sexualGroomingReportsApp.victim.city')}
                id="victim-city"
                name="city"
                data-cy="city"
                type="text"
              />
              <ValidatedField
                label={translate('sexualGroomingReportsApp.victim.state')}
                id="victim-state"
                name="state"
                data-cy="state"
                type="text"
              />
              <ValidatedField
                label={translate('sexualGroomingReportsApp.victim.country')}
                id="victim-country"
                name="country"
                data-cy="country"
                type="text"
              />
              <ValidatedField
                label={translate('sexualGroomingReportsApp.victim.observations')}
                id="victim-observations"
                name="observations"
                data-cy="observations"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/victim" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default VictimUpdate;
