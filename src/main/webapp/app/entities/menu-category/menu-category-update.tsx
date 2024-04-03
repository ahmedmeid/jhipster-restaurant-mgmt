import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRestaurantMenu } from 'app/shared/model/restaurant-menu.model';
import { getEntities as getRestaurantMenus } from 'app/entities/restaurant-menu/restaurant-menu.reducer';
import { IMenuCategory } from 'app/shared/model/menu-category.model';
import { getEntity, updateEntity, createEntity, reset } from './menu-category.reducer';

export const MenuCategoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const restaurantMenus = useAppSelector(state => state.restaurantMenu.entities);
  const menuCategoryEntity = useAppSelector(state => state.menuCategory.entity);
  const loading = useAppSelector(state => state.menuCategory.loading);
  const updating = useAppSelector(state => state.menuCategory.updating);
  const updateSuccess = useAppSelector(state => state.menuCategory.updateSuccess);

  const handleClose = () => {
    navigate('/menu-category');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getRestaurantMenus({}));
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
      ...menuCategoryEntity,
      ...values,
      menu: restaurantMenus.find(it => it.id.toString() === values.menu?.toString()),
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
          ...menuCategoryEntity,
          menu: menuCategoryEntity?.menu?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rstrntgmgtApp.menuCategory.home.createOrEditLabel" data-cy="MenuCategoryCreateUpdateHeading">
            <Translate contentKey="rstrntgmgtApp.menuCategory.home.createOrEditLabel">Create or edit a MenuCategory</Translate>
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
                  id="menu-category-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rstrntgmgtApp.menuCategory.categoryName')}
                id="menu-category-categoryName"
                name="categoryName"
                data-cy="categoryName"
                type="text"
              />
              <ValidatedField
                label={translate('rstrntgmgtApp.menuCategory.categoryDescription')}
                id="menu-category-categoryDescription"
                name="categoryDescription"
                data-cy="categoryDescription"
                type="text"
              />
              <ValidatedField
                id="menu-category-menu"
                name="menu"
                data-cy="menu"
                label={translate('rstrntgmgtApp.menuCategory.menu')}
                type="select"
              >
                <option value="" key="0" />
                {restaurantMenus
                  ? restaurantMenus.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.menuName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/menu-category" replace color="info">
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

export default MenuCategoryUpdate;
