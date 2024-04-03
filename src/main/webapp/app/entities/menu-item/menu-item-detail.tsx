import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './menu-item.reducer';

export const MenuItemDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const menuItemEntity = useAppSelector(state => state.menuItem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="menuItemDetailsHeading">
          <Translate contentKey="rstrntgmgtApp.menuItem.detail.title">MenuItem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{menuItemEntity.id}</dd>
          <dt>
            <span id="itemName">
              <Translate contentKey="rstrntgmgtApp.menuItem.itemName">Item Name</Translate>
            </span>
          </dt>
          <dd>{menuItemEntity.itemName}</dd>
          <dt>
            <span id="itemDescription">
              <Translate contentKey="rstrntgmgtApp.menuItem.itemDescription">Item Description</Translate>
            </span>
          </dt>
          <dd>{menuItemEntity.itemDescription}</dd>
          <dt>
            <span id="ingredients">
              <Translate contentKey="rstrntgmgtApp.menuItem.ingredients">Ingredients</Translate>
            </span>
          </dt>
          <dd>{menuItemEntity.ingredients}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="rstrntgmgtApp.menuItem.price">Price</Translate>
            </span>
          </dt>
          <dd>{menuItemEntity.price}</dd>
          <dt>
            <Translate contentKey="rstrntgmgtApp.menuItem.menuCategory">Menu Category</Translate>
          </dt>
          <dd>{menuItemEntity.menuCategory ? menuItemEntity.menuCategory.categoryName : ''}</dd>
        </dl>
        <Button tag={Link} to="/menu-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/menu-item/${menuItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MenuItemDetail;
