/*
 * Axelor Business Solutions
 * 
 * Copyright (C) 2019 Axelor (<http://axelor.com>).
 * 
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.apps.sale.db.repo;

import com.axelor.apps.sale.db.SaleOrder;
import com.axelor.apps.sale.db.SaleOrderLine;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public class SaleOrderLineRepository extends JpaRepository<SaleOrderLine> {

	public SaleOrderLineRepository() {
		super(SaleOrderLine.class);
	}

	public Query<SaleOrderLine> findAllBySaleOrder(SaleOrder saleOrder) {
		return Query.of(SaleOrderLine.class)
				.filter("self.saleOrder = :saleOrder")
				.bind("saleOrder", saleOrder);
	}

	// TYPE SELECT
	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_TITLE = 1;
}

