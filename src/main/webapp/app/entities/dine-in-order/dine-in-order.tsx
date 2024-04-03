import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './dine-in-order.reducer';

export const DineInOrder = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const dineInOrderList = useAppSelector(state => state.dineInOrder.entities);
  const loading = useAppSelector(state => state.dineInOrder.loading);

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
      <h2 id="dine-in-order-heading" data-cy="DineInOrderHeading">
        <Translate contentKey="rstrntgmgtApp.dineInOrder.home.title">Dine In Orders</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="rstrntgmgtApp.dineInOrder.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/dine-in-order/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="rstrntgmgtApp.dineInOrder.home.createLabel">Create new Dine In Order</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {dineInOrderList && dineInOrderList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="rstrntgmgtApp.dineInOrder.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('orderTime')}>
                  <Translate contentKey="rstrntgmgtApp.dineInOrder.orderTime">Order Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('orderTime')} />
                </th>
                <th className="hand" onClick={sort('tableNumber')}>
                  <Translate contentKey="rstrntgmgtApp.dineInOrder.tableNumber">Table Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tableNumber')} />
                </th>
                <th>
                  <Translate contentKey="rstrntgmgtApp.dineInOrder.restaurant">Restaurant</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {dineInOrderList.map((dineInOrder, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/dine-in-order/${dineInOrder.id}`} color="link" size="sm">
                      {dineInOrder.id}
                    </Button>
                  </td>
                  <td>
                    {dineInOrder.orderTime ? <TextFormat type="date" value={dineInOrder.orderTime} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{dineInOrder.tableNumber}</td>
                  <td>
                    {dineInOrder.restaurant ? (
                      <Link to={`/restaurant/${dineInOrder.restaurant.id}`}>{dineInOrder.restaurant.restaurantName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/dine-in-order/${dineInOrder.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/dine-in-order/${dineInOrder.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/dine-in-order/${dineInOrder.id}/delete`)}
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
              <Translate contentKey="rstrntgmgtApp.dineInOrder.home.notFound">No Dine In Orders found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default DineInOrder;
