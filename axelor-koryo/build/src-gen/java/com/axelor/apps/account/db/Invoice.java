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
package com.axelor.apps.account.db;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.axelor.apps.base.db.Partner;
import com.axelor.apps.koryo.db.UserLog;
import com.axelor.auth.db.AuditableModel;
import com.axelor.auth.db.User;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "ACCOUNT_INVOICE", indexes = { @Index(columnList = "fabrican") })
public class Invoice extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNT_INVOICE_SEQ")
	@SequenceGenerator(name = "ACCOUNT_INVOICE_SEQ", sequenceName = "ACCOUNT_INVOICE_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Date dernière relance")
	private LocalDate dateDerniereRelance;

	@Widget(title = "Date prochaine relance")
	private LocalDate dateProchaineRelance;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Partner> listeDesContactsFacturation;

	@Widget(title = "Anomalie montant")
	private Boolean anomalieMontant = Boolean.FALSE;

	@Widget(title = "User logs")
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<UserLog> userLogList;

	@Widget(title = "Fabrican")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User fabrican;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Invoice() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDateDerniereRelance() {
		return dateDerniereRelance;
	}

	public void setDateDerniereRelance(LocalDate dateDerniereRelance) {
		this.dateDerniereRelance = dateDerniereRelance;
	}

	public LocalDate getDateProchaineRelance() {
		return dateProchaineRelance;
	}

	public void setDateProchaineRelance(LocalDate dateProchaineRelance) {
		this.dateProchaineRelance = dateProchaineRelance;
	}

	public Set<Partner> getListeDesContactsFacturation() {
		return listeDesContactsFacturation;
	}

	public void setListeDesContactsFacturation(Set<Partner> listeDesContactsFacturation) {
		this.listeDesContactsFacturation = listeDesContactsFacturation;
	}

	/**
	 * Add the given {@link Partner} item to the {@code listeDesContactsFacturation}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addListeDesContactsFacturation(Partner item) {
		if (getListeDesContactsFacturation() == null) {
			setListeDesContactsFacturation(new HashSet<>());
		}
		getListeDesContactsFacturation().add(item);
	}

	/**
	 * Remove the given {@link Partner} item from the {@code listeDesContactsFacturation}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeListeDesContactsFacturation(Partner item) {
		if (getListeDesContactsFacturation() == null) {
			return;
		}
		getListeDesContactsFacturation().remove(item);
	}

	/**
	 * Clear the {@code listeDesContactsFacturation} collection.
	 *
	 */
	public void clearListeDesContactsFacturation() {
		if (getListeDesContactsFacturation() != null) {
			getListeDesContactsFacturation().clear();
		}
	}

	public Boolean getAnomalieMontant() {
		return anomalieMontant == null ? Boolean.FALSE : anomalieMontant;
	}

	public void setAnomalieMontant(Boolean anomalieMontant) {
		this.anomalieMontant = anomalieMontant;
	}

	public List<UserLog> getUserLogList() {
		return userLogList;
	}

	public void setUserLogList(List<UserLog> userLogList) {
		this.userLogList = userLogList;
	}

	/**
	 * Add the given {@link UserLog} item to the {@code userLogList}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addUserLogListItem(UserLog item) {
		if (getUserLogList() == null) {
			setUserLogList(new ArrayList<>());
		}
		getUserLogList().add(item);
	}

	/**
	 * Remove the given {@link UserLog} item from the {@code userLogList}.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeUserLogListItem(UserLog item) {
		if (getUserLogList() == null) {
			return;
		}
		getUserLogList().remove(item);
	}

	/**
	 * Clear the {@code userLogList} collection.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 */
	public void clearUserLogList() {
		if (getUserLogList() != null) {
			getUserLogList().clear();
		}
	}

	public User getFabrican() {
		return fabrican;
	}

	public void setFabrican(User fabrican) {
		this.fabrican = fabrican;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof Invoice)) return false;

		final Invoice other = (Invoice) obj;
		if (this.getId() != null || other.getId() != null) {
			return Objects.equals(this.getId(), other.getId());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("id", getId())
			.add("dateDerniereRelance", getDateDerniereRelance())
			.add("dateProchaineRelance", getDateProchaineRelance())
			.add("anomalieMontant", getAnomalieMontant())
			.omitNullValues()
			.toString();
	}
}
