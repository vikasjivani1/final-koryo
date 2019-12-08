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
package com.axelor.apps.stock.db.repo;

import com.axelor.apps.stock.db.StockMoveLine;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public class StockMoveLineRepository extends JpaRepository<StockMoveLine> {

	public StockMoveLineRepository() {
		super(StockMoveLine.class);
	}

	public StockMoveLine findByName(String name) {
		return Query.of(StockMoveLine.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

	// CONFORMITY SELECT
	public static final int CONFORMITY_NONE = 1;
	public static final int CONFORMITY_COMPLIANT = 2;
	public static final int CONFORMITY_NON_COMPLIANT = 3;

	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_TITLE = 1;

	// AVAILABLE STATUS SELECT
	public static final int STATUS_AVAILABLE = 1;
	public static final int STATUS_AVAILABLE_FOR_PRODUCT = 2;
	public static final int STATUS_MISSING = 3;
}

