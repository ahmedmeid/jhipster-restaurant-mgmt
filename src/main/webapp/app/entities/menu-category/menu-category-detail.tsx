import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './menu-category.reducer';

export const MenuCategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const menuCategoryEntity = useAppSelector(state => state.menuCategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="menuCategoryDetailsHeading">
          <Translate contentKey="rstrntgmgtApp.menuCategory.detail.title">MenuCategory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{menuCategoryEntity.id}</dd>
          <dt>
            <span id="categoryName">
              <Translate contentKey="rstrntgmgtApp.menuCategory.categoryName">Category Name</Translate>
            </span>
          </dt>
          <dd>{menuCategoryEntity.categoryName}</dd>
          <dt>
            <span id="categoryDescription">
              <Translate contentKey="rstrntgmgtApp.menuCategory.categoryDescription">Category Description</Translate>
            </span>
          </dt>
          <dd>{menuCategoryEntity.categoryDescription}</dd>
          <dt>
            <Translate contentKey="rstrntgmgtApp.menuCategory.menu">Menu</Translate>
          </dt>
          <dd>{menuCategoryEntity.menu ? menuCategoryEntity.menu.menuName : ''}</dd>
        </dl>
        <Button tag={Link} to="/menu-category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/menu-category/${menuCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MenuCategoryDetail;
