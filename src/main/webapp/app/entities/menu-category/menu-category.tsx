import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './menu-category.reducer';

export const MenuCategory = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const menuCategoryList = useAppSelector(state => state.menuCategory.entities);
  const loading = useAppSelector(state => state.menuCategory.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="menu-category-heading" data-cy="MenuCategoryHeading">
        <Translate contentKey="rstrntgmgtApp.menuCategory.home.title">Menu Categories</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="rstrntgmgtApp.menuCategory.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/menu-category/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="rstrntgmgtApp.menuCategory.home.createLabel">Create new Menu Category</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {menuCategoryList && menuCategoryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="rstrntgmgtApp.menuCategory.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('categoryName')}>
                  <Translate contentKey="rstrntgmgtApp.menuCategory.categoryName">Category Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('categoryName')} />
                </th>
                <th className="hand" onClick={sort('categoryDescription')}>
                  <Translate contentKey="rstrntgmgtApp.menuCategory.categoryDescription">Category Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('categoryDescription')} />
                </th>
                <th>
                  <Translate contentKey="rstrntgmgtApp.menuCategory.menu">Menu</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {menuCategoryList.map((menuCategory, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/menu-category/${menuCategory.id}`} color="link" size="sm">
                      {menuCategory.id}
                    </Button>
                  </td>
                  <td>{menuCategory.categoryName}</td>
                  <td>{menuCategory.categoryDescription}</td>
                  <td>
                    {menuCategory.menu ? <Link to={`/restaurant-menu/${menuCategory.menu.id}`}>{menuCategory.menu.menuName}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/menu-category/${menuCategory.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/menu-category/${menuCategory.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/menu-category/${menuCategory.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="rstrntgmgtApp.menuCategory.home.notFound">No Menu Categories found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default MenuCategory;
