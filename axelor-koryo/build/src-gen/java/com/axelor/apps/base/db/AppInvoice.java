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
package com.axelor.apps.base.db;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.axelor.db.EntityHelper;
import com.axelor.db.annotations.Widget;

@Entity
@Table(name = "BASE_APP_INVOICE")
public class AppInvoice extends App {

	@Widget(title = "Nb de jours de relance après dernière relance")
	private Integer nbDeJoursDeRelanceApresDerniereRelance = 0;

	@Widget(title = "Coefficient of increase")
	private Integer coefficientOfIncrease = 0;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public AppInvoice() {
	}

	public Integer getNbDeJoursDeRelanceApresDerniereRelance() {
		return nbDeJoursDeRelanceApresDerniereRelance == null ? 0 : nbDeJoursDeRelanceApresDerniereRelance;
	}

	public void setNbDeJoursDeRelanceApresDerniereRelance(Integer nbDeJoursDeRelanceApresDerniereRelance) {
		this.nbDeJoursDeRelanceApresDerniereRelance = nbDeJoursDeRelanceApresDerniereRelance;
	}

	public Integer getCoefficientOfIncrease() {
		return coefficientOfIncrease == null ? 0 : coefficientOfIncrease;
	}

	public void setCoefficientOfIncrease(Integer coefficientOfIncrease) {
		this.coefficientOfIncrease = coefficientOfIncrease;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	@Override
	public boolean equals(Object obj) {
		return EntityHelper.equals(this, obj);
	}

	@Override
	public int hashCode() {
		return EntityHelper.hashCode(this);
	}

	@Override
	public String toString() {
		return EntityHelper.toString(this);
	}
}
