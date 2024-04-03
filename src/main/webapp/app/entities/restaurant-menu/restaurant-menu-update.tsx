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
import { IRestaurantMenu } from 'app/shared/model/restaurant-menu.model';
import { getEntity, updateEntity, createEntity, reset } from './restaurant-menu.reducer';

export const RestaurantMenuUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const restaurants = useAppSelector(state => state.restaurant.entities);
  const restaurantMenuEntity = useAppSelector(state => state.restaurantMenu.entity);
  const loading = useAppSelector(state => state.restaurantMenu.loading);
  const updating = useAppSelector(state => state.restaurantMenu.updating);
  const updateSuccess = useAppSelector(state => state.restaurantMenu.updateSuccess);

  const handleClose = () => {
    navigate('/restaurant-menu');
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

    const entity = {
      ...restaurantMenuEntity,
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
      ? {}
      : {
          ...restaurantMenuEntity,
          restaurant: restaurantMenuEntity?.restaurant?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rstrntgmgtApp.restaurantMenu.home.createOrEditLabel" data-cy="RestaurantMenuCreateUpdateHeading">
            <Translate contentKey="rstrntgmgtApp.restaurantMenu.home.createOrEditLabel">Create or edit a RestaurantMenu</Translate>
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
                  id="restaurant-menu-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rstrntgmgtApp.restaurantMenu.menuName')}
                id="restaurant-menu-menuName"
                name="menuName"
                data-cy="menuName"
                type="text"
              />
              <ValidatedField
                label={translate('rstrntgmgtApp.restaurantMenu.menuDescription')}
                id="restaurant-menu-menuDescription"
                name="menuDescription"
                data-cy="menuDescription"
                type="text"
              />
              <ValidatedField
                id="restaurant-menu-restaurant"
                name="restaurant"
                data-cy="restaurant"
                label={translate('rstrntgmgtApp.restaurantMenu.restaurant')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/restaurant-menu" replace color="info">
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

export default RestaurantMenuUpdate;
