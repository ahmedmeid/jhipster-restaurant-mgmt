import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMenuItem } from 'app/shared/model/menu-item.model';
import { getEntities as getMenuItems } from 'app/entities/menu-item/menu-item.reducer';
import { IDineInOrder } from 'app/shared/model/dine-in-order.model';
import { getEntities as getDineInOrders } from 'app/entities/dine-in-order/dine-in-order.reducer';
import { IOrderItem } from 'app/shared/model/order-item.model';
import { getEntity, updateEntity, createEntity, reset } from './order-item.reducer';

export const OrderItemUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const menuItems = useAppSelector(state => state.menuItem.entities);
  const dineInOrders = useAppSelector(state => state.dineInOrder.entities);
  const orderItemEntity = useAppSelector(state => state.orderItem.entity);
  const loading = useAppSelector(state => state.orderItem.loading);
  const updating = useAppSelector(state => state.orderItem.updating);
  const updateSuccess = useAppSelector(state => state.orderItem.updateSuccess);

  const handleClose = () => {
    navigate('/order-item');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMenuItems({}));
    dispatch(getDineInOrders({}));
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
    if (values.quantity !== undefined && typeof values.quantity !== 'number') {
      values.quantity = Number(values.quantity);
    }

    const entity = {
      ...orderItemEntity,
      ...values,
      menuItem: menuItems.find(it => it.id.toString() === values.menuItem?.toString()),
      dineInOrder: dineInOrders.find(it => it.id.toString() === values.dineInOrder?.toString()),
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
          ...orderItemEntity,
          menuItem: orderItemEntity?.menuItem?.id,
          dineInOrder: orderItemEntity?.dineInOrder?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rstrntgmgtApp.orderItem.home.createOrEditLabel" data-cy="OrderItemCreateUpdateHeading">
            <Translate contentKey="rstrntgmgtApp.orderItem.home.createOrEditLabel">Create or edit a OrderItem</Translate>
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
                  id="order-item-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('rstrntgmgtApp.orderItem.quantity')}
                id="order-item-quantity"
                name="quantity"
                data-cy="quantity"
                type="text"
              />
              <ValidatedField
                label={translate('rstrntgmgtApp.orderItem.specialRequests')}
                id="order-item-specialRequests"
                name="specialRequests"
                data-cy="specialRequests"
                type="text"
              />
              <ValidatedField
                id="order-item-menuItem"
                name="menuItem"
                data-cy="menuItem"
                label={translate('rstrntgmgtApp.orderItem.menuItem')}
                type="select"
              >
                <option value="" key="0" />
                {menuItems
                  ? menuItems.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.itemName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="order-item-dineInOrder"
                name="dineInOrder"
                data-cy="dineInOrder"
                label={translate('rstrntgmgtApp.orderItem.dineInOrder')}
                type="select"
              >
                <option value="" key="0" />
                {dineInOrders
                  ? dineInOrders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/order-item" replace color="info">
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

export default OrderItemUpdate;
