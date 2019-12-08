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
package com.axelor.apps.koryo.db;

import java.math.BigDecimal;
import java.util.HashSet;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.axelor.apps.base.db.Partner;
import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "KORYO_SUPPLIER_CONSULATION", indexes = { @Index(columnList = "fournisseur"), @Index(columnList = "supplier_option1"), @Index(columnList = "supplier_option2"), @Index(columnList = "quantite") })
public class SupplierConsulation extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KORYO_SUPPLIER_CONSULATION_SEQ")
	@SequenceGenerator(name = "KORYO_SUPPLIER_CONSULATION_SEQ", sequenceName = "KORYO_SUPPLIER_CONSULATION_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Fournisseur")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner fournisseur;

	@Widget(title = "Contacts consultés")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Partner> contactsConsultes;

	@Widget(title = "Envoyer/renvoyer demande unitaire ")
	private Boolean envoyerRenvoyerDemandeUnitaire = Boolean.FALSE;

	@Widget(title = "Envoyée")
	private Boolean envoyee = Boolean.FALSE;

	@Widget(title = "Selection Option 1")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private SupplierOption supplierOption1;

	@Widget(title = "Selection Option 2")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private SupplierOption supplierOption2;

	@Widget(title = "Quantité")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private SupplierQtity quantite;

	@Widget(title = "prix achat")
	private BigDecimal prixAchat = BigDecimal.ZERO;

	@Widget(title = "Prix de vente")
	private BigDecimal prixDeVente = BigDecimal.ZERO;

	@Widget(title = "Marge en montant")
	private BigDecimal margeEnMontant = BigDecimal.ZERO;

	@Widget(title = "Marge en %")
	private Integer margeEn = 0;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public SupplierConsulation() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Partner getFournisseur() {
		return fournisseur;
	}

	public void setFournisseur(Partner fournisseur) {
		this.fournisseur = fournisseur;
	}

	public Set<Partner> getContactsConsultes() {
		return contactsConsultes;
	}

	public void setContactsConsultes(Set<Partner> contactsConsultes) {
		this.contactsConsultes = contactsConsultes;
	}

	/**
	 * Add the given {@link Partner} item to the {@code contactsConsultes}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addContactsConsulte(Partner item) {
		if (getContactsConsultes() == null) {
			setContactsConsultes(new HashSet<>());
		}
		getContactsConsultes().add(item);
	}

	/**
	 * Remove the given {@link Partner} item from the {@code contactsConsultes}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeContactsConsulte(Partner item) {
		if (getContactsConsultes() == null) {
			return;
		}
		getContactsConsultes().remove(item);
	}

	/**
	 * Clear the {@code contactsConsultes} collection.
	 *
	 */
	public void clearContactsConsultes() {
		if (getContactsConsultes() != null) {
			getContactsConsultes().clear();
		}
	}

	public Boolean getEnvoyerRenvoyerDemandeUnitaire() {
		return envoyerRenvoyerDemandeUnitaire == null ? Boolean.FALSE : envoyerRenvoyerDemandeUnitaire;
	}

	public void setEnvoyerRenvoyerDemandeUnitaire(Boolean envoyerRenvoyerDemandeUnitaire) {
		this.envoyerRenvoyerDemandeUnitaire = envoyerRenvoyerDemandeUnitaire;
	}

	public Boolean getEnvoyee() {
		return envoyee == null ? Boolean.FALSE : envoyee;
	}

	public void setEnvoyee(Boolean envoyee) {
		this.envoyee = envoyee;
	}

	public SupplierOption getSupplierOption1() {
		return supplierOption1;
	}

	public void setSupplierOption1(SupplierOption supplierOption1) {
		this.supplierOption1 = supplierOption1;
	}

	public SupplierOption getSupplierOption2() {
		return supplierOption2;
	}

	public void setSupplierOption2(SupplierOption supplierOption2) {
		this.supplierOption2 = supplierOption2;
	}

	public SupplierQtity getQuantite() {
		return quantite;
	}

	public void setQuantite(SupplierQtity quantite) {
		this.quantite = quantite;
	}

	public BigDecimal getPrixAchat() {
		return prixAchat == null ? BigDecimal.ZERO : prixAchat;
	}

	public void setPrixAchat(BigDecimal prixAchat) {
		this.prixAchat = prixAchat;
	}

	public BigDecimal getPrixDeVente() {
		return prixDeVente == null ? BigDecimal.ZERO : prixDeVente;
	}

	public void setPrixDeVente(BigDecimal prixDeVente) {
		this.prixDeVente = prixDeVente;
	}

	public BigDecimal getMargeEnMontant() {
		return margeEnMontant == null ? BigDecimal.ZERO : margeEnMontant;
	}

	public void setMargeEnMontant(BigDecimal margeEnMontant) {
		this.margeEnMontant = margeEnMontant;
	}

	public Integer getMargeEn() {
		return margeEn == null ? 0 : margeEn;
	}

	public void setMargeEn(Integer margeEn) {
		this.margeEn = margeEn;
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
		if (!(obj instanceof SupplierConsulation)) return false;

		final SupplierConsulation other = (SupplierConsulation) obj;
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
			.add("envoyerRenvoyerDemandeUnitaire", getEnvoyerRenvoyerDemandeUnitaire())
			.add("envoyee", getEnvoyee())
			.add("prixAchat", getPrixAchat())
			.add("prixDeVente", getPrixDeVente())
			.add("margeEnMontant", getMargeEnMontant())
			.add("margeEn", getMargeEn())
			.omitNullValues()
			.toString();
	}
}
