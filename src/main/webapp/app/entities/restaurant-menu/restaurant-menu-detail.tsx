import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './restaurant-menu.reducer';

export const RestaurantMenuDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const restaurantMenuEntity = useAppSelector(state => state.restaurantMenu.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="restaurantMenuDetailsHeading">
          <Translate contentKey="rstrntgmgtApp.restaurantMenu.detail.title">RestaurantMenu</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{restaurantMenuEntity.id}</dd>
          <dt>
            <span id="menuName">
              <Translate contentKey="rstrntgmgtApp.restaurantMenu.menuName">Menu Name</Translate>
            </span>
          </dt>
          <dd>{restaurantMenuEntity.menuName}</dd>
          <dt>
            <span id="menuDescription">
              <Translate contentKey="rstrntgmgtApp.restaurantMenu.menuDescription">Menu Description</Translate>
            </span>
          </dt>
          <dd>{restaurantMenuEntity.menuDescription}</dd>
          <dt>
            <Translate contentKey="rstrntgmgtApp.restaurantMenu.restaurant">Restaurant</Translate>
          </dt>
          <dd>{restaurantMenuEntity.restaurant ? restaurantMenuEntity.restaurant.restaurantName : ''}</dd>
        </dl>
        <Button tag={Link} to="/restaurant-menu" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/restaurant-menu/${restaurantMenuEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RestaurantMenuDetail;
