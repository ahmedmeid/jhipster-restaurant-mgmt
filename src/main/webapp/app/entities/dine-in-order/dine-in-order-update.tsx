import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRestaurant } from 'app/shared/model/restaurant.model';
import { getEntities as getRestaurants } from 'app/entities/restaurant/restaurant.reducer';
import { IDineInOrder } from 'app/shared/model/dine-in-order.model';
import { getEntity, updateEntity, createEntity, reset } from './dine-in-order.reducer';

export const DineInOrderUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const restaurants = useAppSelector(state => state.restaurant.entities);
  const dineInOrderEntity = useAppSelector(state => state.dineInOrder.entity);
  const loading = useAppSelector(state => state.dineInOrder.loading);
  const updating = useAppSelector(state => state.dineInOrder.updating);
  const updateSuccess = useAppSelector(state => state.dineInOrder.updateSuccess);

  const handleClose = () => {
    navigate('/dine-in-order');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getRestaurants({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.orderTime = convertDateTimeToServer(values.orderTime);
    if (values.tableNumber !== undefined && typeof values.tableNumber !== 'number') {
      values.tableNumber = Number(values.tableNumber);
    }

    const entity = {
      ...dineInOrderEntity,
      ...values,
      restaurant: restaurants.find(it => it.id.toString() === values.restaurant?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          orderTime: displayDefaultDateTime(),
        }
      : {
          ...dineInOrderEntity,
          orderTime: convertDateTimeFromServer(dineInOrderEntity.orderTime),
          restaurant: dineInOrderEntity?.restaurant?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rstrntgmgtApp.dineInOrder.home.createOrEditLabel" data-cy="DineInOrderCreateUpdateHeading">
            <Translate contentKey="rstrntgmgtApp.dineInOrder.home.createOrEditLabel">Create or edit a DineInOrder</Translate>
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
                  id="dine-in-order-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rstrntgmgtApp.dineInOrder.orderTime')}
                id="dine-in-order-orderTime"
                name="orderTime"
                data-cy="orderTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('rstrntgmgtApp.dineInOrder.tableNumber')}
                id="dine-in-order-tableNumber"
                name="tableNumber"
                data-cy="tableNumber"
                type="text"
              />
              <ValidatedField
                id="dine-in-order-restaurant"
                name="restaurant"
                data-cy="restaurant"
                label={translate('rstrntgmgtApp.dineInOrder.restaurant')}
                type="select"
              >
                <option value="" key="0" />
                {restaurants
                  ? restaurants.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.restaurantName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/dine-in-order" replace color="info">
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

export default DineInOrderUpdate;
