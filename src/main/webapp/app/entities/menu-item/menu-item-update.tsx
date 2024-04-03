import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMenuCategory } from 'app/shared/model/menu-category.model';
import { getEntities as getMenuCategories } from 'app/entities/menu-category/menu-category.reducer';
import { IMenuItem } from 'app/shared/model/menu-item.model';
import { getEntity, updateEntity, createEntity, reset } from './menu-item.reducer';

export const MenuItemUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const menuCategories = useAppSelector(state => state.menuCategory.entities);
  const menuItemEntity = useAppSelector(state => state.menuItem.entity);
  const loading = useAppSelector(state => state.menuItem.loading);
  const updating = useAppSelector(state => state.menuItem.updating);
  const updateSuccess = useAppSelector(state => state.menuItem.updateSuccess);

  const handleClose = () => {
    navigate('/menu-item');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMenuCategories({}));
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
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }

    const entity = {
      ...menuItemEntity,
      ...values,
      menuCategory: menuCategories.find(it => it.id.toString() === values.menuCategory?.toString()),
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
          ...menuItemEntity,
          menuCategory: menuItemEntity?.menuCategory?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rstrntgmgtApp.menuItem.home.createOrEditLabel" data-cy="MenuItemCreateUpdateHeading">
            <Translate contentKey="rstrntgmgtApp.menuItem.home.createOrEditLabel">Create or edit a MenuItem</Translate>
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
                  id="menu-item-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rstrntgmgtApp.menuItem.itemName')}
                id="menu-item-itemName"
                name="itemName"
                data-cy="itemName"
                type="text"
              />
              <ValidatedField
                label={translate('rstrntgmgtApp.menuItem.itemDescription')}
                id="menu-item-itemDescription"
                name="itemDescription"
                data-cy="itemDescription"
                type="text"
              />
              <ValidatedField
                label={translate('rstrntgmgtApp.menuItem.ingredients')}
                id="menu-item-ingredients"
                name="ingredients"
                data-cy="ingredients"
                type="text"
              />
              <ValidatedField
                label={translate('rstrntgmgtApp.menuItem.price')}
                id="menu-item-price"
                name="price"
                data-cy="price"
                type="text"
              />
              <ValidatedField
                id="menu-item-menuCategory"
                name="menuCategory"
                data-cy="menuCategory"
                label={translate('rstrntgmgtApp.menuItem.menuCategory')}
                type="select"
              >
                <option value="" key="0" />
                {menuCategories
                  ? menuCategories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.categoryName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/menu-item" replace color="info">
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

export default MenuItemUpdate;
