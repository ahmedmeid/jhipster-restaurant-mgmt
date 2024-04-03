import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './menu-item.reducer';

export const MenuItem = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const menuItemList = useAppSelector(state => state.menuItem.entities);
  const loading = useAppSelector(state => state.menuItem.loading);

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
      <h2 id="menu-item-heading" data-cy="MenuItemHeading">
        <Translate contentKey="rstrntgmgtApp.menuItem.home.title">Menu Items</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="rstrntgmgtApp.menuItem.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/menu-item/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="rstrntgmgtApp.menuItem.home.createLabel">Create new Menu Item</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {menuItemList && menuItemList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="rstrntgmgtApp.menuItem.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('itemName')}>
                  <Translate contentKey="rstrntgmgtApp.menuItem.itemName">Item Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('itemName')} />
                </th>
                <th className="hand" onClick={sort('itemDescription')}>
                  <Translate contentKey="rstrntgmgtApp.menuItem.itemDescription">Item Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('itemDescription')} />
                </th>
                <th className="hand" onClick={sort('ingredients')}>
                  <Translate contentKey="rstrntgmgtApp.menuItem.ingredients">Ingredients</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ingredients')} />
                </th>
                <th className="hand" onClick={sort('price')}>
                  <Translate contentKey="rstrntgmgtApp.menuItem.price">Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('price')} />
                </th>
                <th>
                  <Translate contentKey="rstrntgmgtApp.menuItem.menuCategory">Menu Category</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {menuItemList.map((menuItem, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/menu-item/${menuItem.id}`} color="link" size="sm">
                      {menuItem.id}
                    </Button>
                  </td>
                  <td>{menuItem.itemName}</td>
                  <td>{menuItem.itemDescription}</td>
                  <td>{menuItem.ingredients}</td>
                  <td>{menuItem.price}</td>
                  <td>
                    {menuItem.menuCategory ? (
                      <Link to={`/menu-category/${menuItem.menuCategory.id}`}>{menuItem.menuCategory.categoryName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/menu-item/${menuItem.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/menu-item/${menuItem.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/menu-item/${menuItem.id}/delete`)}
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
              <Translate contentKey="rstrntgmgtApp.menuItem.home.notFound">No Menu Items found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default MenuItem;
