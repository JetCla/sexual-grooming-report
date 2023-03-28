import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IReport } from 'app/shared/model/report.model';
import { getEntities as getReports } from 'app/entities/report/report.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/shared/reducers/user-management';
import { IReportLog } from 'app/shared/model/report-log.model';
import { getEntity, updateEntity, createEntity, reset } from './report-log.reducer';

export const ReportLogUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const reports = useAppSelector(state => state.report.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const reportLogEntity = useAppSelector(state => state.reportLog.entity);
  const loading = useAppSelector(state => state.reportLog.loading);
  const updating = useAppSelector(state => state.reportLog.updating);
  const updateSuccess = useAppSelector(state => state.reportLog.updateSuccess);

  const handleClose = () => {
    navigate('/report-log');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getReports({}));
    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...reportLogEntity,
      ...values,
      reportLogs: reports.find(it => it.id.toString() === values.reportLogs.toString()),
      updatedBy: users.find(it => it.id.toString() === values.updatedBy.toString()),
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
          ...reportLogEntity,
          reportLogs: reportLogEntity?.reportLogs?.id,
          updatedBy: reportLogEntity?.updatedBy?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sexualGroomingReportsApp.reportLog.home.createOrEditLabel" data-cy="ReportLogCreateUpdateHeading">
            <Translate contentKey="sexualGroomingReportsApp.reportLog.home.createOrEditLabel">Create or edit a ReportLog</Translate>
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
                  id="report-log-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sexualGroomingReportsApp.reportLog.date')}
                id="report-log-date"
                name="date"
                data-cy="date"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('sexualGroomingReportsApp.reportLog.change')}
                id="report-log-change"
                name="change"
                data-cy="change"
                type="text"
              />
              <ValidatedField
                id="report-log-reportLogs"
                name="reportLogs"
                data-cy="reportLogs"
                label={translate('sexualGroomingReportsApp.reportLog.reportLogs')}
                type="select"
              >
                <option value="" key="0" />
                {reports
                  ? reports.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="report-log-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                label={translate('sexualGroomingReportsApp.reportLog.updatedBy')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/report-log" replace color="info">
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

export default ReportLogUpdate;
