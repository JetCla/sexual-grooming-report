import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVictim } from 'app/shared/model/victim.model';
import { getEntities as getVictims } from 'app/entities/victim/victim.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/shared/reducers/user-management';
import { IReport } from 'app/shared/model/report.model';
import { getEntity, updateEntity, createEntity, reset } from './report.reducer';

export const ReportUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const victims = useAppSelector(state => state.victim.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const reportEntity = useAppSelector(state => state.report.entity);
  const loading = useAppSelector(state => state.report.loading);
  const updating = useAppSelector(state => state.report.updating);
  const updateSuccess = useAppSelector(state => state.report.updateSuccess);

  const handleClose = () => {
    navigate('/report' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getVictims({}));
    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...reportEntity,
      ...values,
      victim: victims.find(it => it.id.toString() === values.victim.toString()),
      owner: users.find(it => it.id.toString() === values.owner.toString()),
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
          ...reportEntity,
          victim: reportEntity?.victim?.id,
          owner: reportEntity?.owner?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sexualGroomingReportsApp.report.home.createOrEditLabel" data-cy="ReportCreateUpdateHeading">
            <Translate contentKey="sexualGroomingReportsApp.report.home.createOrEditLabel">Create or edit a Report</Translate>
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
                  id="report-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sexualGroomingReportsApp.report.number')}
                id="report-number"
                name="number"
                data-cy="number"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('sexualGroomingReportsApp.report.date')}
                id="report-date"
                name="date"
                data-cy="date"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('sexualGroomingReportsApp.report.officeAssigned')}
                id="report-officeAssigned"
                name="officeAssigned"
                data-cy="officeAssigned"
                type="text"
              />
              <ValidatedField
                label={translate('sexualGroomingReportsApp.report.description')}
                id="report-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('sexualGroomingReportsApp.report.location')}
                id="report-location"
                name="location"
                data-cy="location"
                type="text"
              />
              <ValidatedField
                id="report-victim"
                name="victim"
                data-cy="victim"
                label={translate('sexualGroomingReportsApp.report.victim')}
                type="select"
              >
                <option value="" key="0" />
                {victims
                  ? victims.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.firstName} - {otherEntity.lastName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="report-owner"
                name="owner"
                data-cy="owner"
                label={translate('sexualGroomingReportsApp.report.owner')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.firstName} - {otherEntity.lastName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/report" replace color="info">
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

export default ReportUpdate;
