import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dine-in-order.reducer';

export const DineInOrderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const dineInOrderEntity = useAppSelector(state => state.dineInOrder.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dineInOrderDetailsHeading">
          <Translate contentKey="rstrntgmgtApp.dineInOrder.detail.title">DineInOrder</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{dineInOrderEntity.id}</dd>
          <dt>
            <span id="orderTime">
              <Translate contentKey="rstrntgmgtApp.dineInOrder.orderTime">Order Time</Translate>
            </span>
          </dt>
          <dd>
            {dineInOrderEntity.orderTime ? <TextFormat value={dineInOrderEntity.orderTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="tableNumber">
              <Translate contentKey="rstrntgmgtApp.dineInOrder.tableNumber">Table Number</Translate>
            </span>
          </dt>
          <dd>{dineInOrderEntity.tableNumber}</dd>
          <dt>
            <Translate contentKey="rstrntgmgtApp.dineInOrder.restaurant">Restaurant</Translate>
          </dt>
          <dd>{dineInOrderEntity.restaurant ? dineInOrderEntity.restaurant.restaurantName : ''}</dd>
        </dl>
        <Button tag={Link} to="/dine-in-order" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dine-in-order/${dineInOrderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DineInOrderDetail;
