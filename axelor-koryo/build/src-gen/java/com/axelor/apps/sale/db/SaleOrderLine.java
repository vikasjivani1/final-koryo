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
package com.axelor.apps.sale.db;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axelor.apps.account.db.TaxEquiv;
import com.axelor.apps.account.db.TaxLine;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.Unit;
import com.axelor.apps.koryo.db.ModelDocument;
import com.axelor.apps.koryo.db.SupplierConsulation;
import com.axelor.apps.koryo.db.SupplierOption;
import com.axelor.apps.koryo.db.SupplierQtity;
import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.VirtualColumn;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "SALE_SALE_ORDER_LINE", indexes = { @Index(columnList = "fullName"), @Index(columnList = "sale_order"), @Index(columnList = "product"), @Index(columnList = "tax_line"), @Index(columnList = "tax_equiv"), @Index(columnList = "unit"), @Index(columnList = "supplier_partner"), @Index(columnList = "modele_de_descriptif_de_consultation"), @Index(columnList = "fournisseur_selectionne"), @Index(columnList = "supplier_option1"), @Index(columnList = "supplier_option2") })
public class SaleOrderLine extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SALE_SALE_ORDER_LINE_SEQ")
	@SequenceGenerator(name = "SALE_SALE_ORDER_LINE_SEQ", sequenceName = "SALE_SALE_ORDER_LINE_SEQ", allocationSize = 1)
	private Long id;

	@NameColumn
	@VirtualColumn
	@Access(AccessType.PROPERTY)
	private String fullName;

	@Widget(title = "Sale order")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private SaleOrder saleOrder;

	@Widget(title = "Seq.")
	private Integer sequence = 0;

	@Widget(title = "Product")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Product product;

	@Widget(title = "Qty")
	private BigDecimal qty = new BigDecimal("1");

	@Widget(hidden = true)
	private BigDecimal oldQty = BigDecimal.ZERO;

	@Widget(title = "Print subtotal / line")
	private Boolean isToPrintLineSubTotal = Boolean.FALSE;

	@Widget(title = "Displayed Product name", translatable = true)
	@NotNull
	private String productName;

	@Widget(title = "Unit price W.T.")
	@Digits(integer = 10, fraction = 10)
	private BigDecimal price = BigDecimal.ZERO;

	@Widget(title = "Unit price A.T.I.")
	@Digits(integer = 10, fraction = 10)
	private BigDecimal inTaxPrice = BigDecimal.ZERO;

	@Widget(title = "Unit price discounted")
	@Digits(integer = 10, fraction = 10)
	private BigDecimal priceDiscounted = BigDecimal.ZERO;

	@Widget(title = "Tax")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private TaxLine taxLine;

	@Widget(title = "Tax Equiv")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private TaxEquiv taxEquiv;

	@Widget(title = "Total W.T.")
	private BigDecimal exTaxTotal = BigDecimal.ZERO;

	@Widget(title = "Total A.T.I.")
	private BigDecimal inTaxTotal = BigDecimal.ZERO;

	@Widget(title = "Description")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String description;

	@Widget(title = "Unit")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Unit unit;

	@Widget(title = "Supplier")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner supplierPartner;

	@Widget(title = "Discount amount")
	@Digits(integer = 10, fraction = 10)
	private BigDecimal discountAmount = BigDecimal.ZERO;

	@Widget(title = "Discount Type", selection = "base.price.list.line.amount.type.select")
	private Integer discountTypeSelect = 0;

	@Widget(title = "Estimated shipping date")
	private LocalDate estimatedDelivDate;

	@Widget(title = "Desired delivery date")
	private LocalDate desiredDelivDate;

	@Widget(title = "Delivered quantity")
	private BigDecimal deliveredQty = BigDecimal.ZERO;

	@Widget(title = "SubTotal cost price")
	private BigDecimal subTotalCostPrice = BigDecimal.ZERO;

	@Widget(title = "Sub Total gross profit")
	private BigDecimal subTotalGrossMargin = BigDecimal.ZERO;

	@Widget(title = "Sub margin rate")
	private BigDecimal subMarginRate = BigDecimal.ZERO;

	@Widget(title = "Sub Total markup")
	private BigDecimal subTotalMarkup = BigDecimal.ZERO;

	@Widget(title = "Total W.T. in company currency", hidden = true)
	private BigDecimal companyExTaxTotal = BigDecimal.ZERO;

	@Widget(title = "Unit cost price in company currency", hidden = true)
	private BigDecimal companyCostPrice = BigDecimal.ZERO;

	@Widget(title = "Total A.T.I. in company currency", hidden = true)
	private BigDecimal companyInTaxTotal = BigDecimal.ZERO;

	@Widget(title = "Total cost in company currency", hidden = true)
	private BigDecimal companyCostTotal = BigDecimal.ZERO;

	@Widget(title = "Type", selection = "sale.order.line.type.select")
	private Integer typeSelect = 0;

	@Widget(title = "Show Total")
	private Boolean isShowTotal = Boolean.FALSE;

	@Widget(title = "Hide Unit Amounts")
	private Boolean isHideUnitAmounts = Boolean.FALSE;

	@Widget(title = "Statut", selection = "koryo.sale.order.line.statut")
	private Integer statut = 0;

	@Widget(title = "Modèle de descriptif de consultation")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ModelDocument modeleDeDescriptifDeConsultation;

	@Widget(title = "Descriptif technique de la consultation")
	private String descriptifTechniqueDeLaConsultation;

	@Widget(title = "Consultations fournisseurs")
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<SupplierConsulation> consultationsFournisseursList;

	@Widget(title = "Fournisseur sélectionné")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner fournisseurSelectionne;

	@Widget(title = "Option 1 sélectionnée")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private SupplierOption supplierOption1;

	@Widget(title = "Option 2 sélectionnée")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private SupplierOption supplierOption2;

	@Widget(title = "Qté à acheter")
	private BigDecimal qteaAcheter = BigDecimal.ZERO;

	@Widget(title = "Sous total d'achat HT")
	private BigDecimal sousTotaldAchatHT = BigDecimal.ZERO;

	@Widget(title = "Prix unitaire d'achat")
	private BigDecimal prixUnitairedAchat = BigDecimal.ZERO;

	@Widget(title = "Cacher ligne à l'impression")
	private Boolean cacherLignealImpression = Boolean.FALSE;

	@Widget(title = "Cacher sous totaux")
	private Boolean cacherSousTotaux = Boolean.FALSE;

	@Widget(title = "Liste des options")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<SupplierOption> listeDesOptions;

	@Widget(title = "Liste des quantités")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<SupplierQtity> listeDesQuantites;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public SaleOrderLine() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		try {
			fullName = computeFullName();
		} catch (NullPointerException e) {
			Logger logger = LoggerFactory.getLogger(getClass());
			logger.error("NPE in function field: getFullName()", e);
		}
		return fullName;
	}

	protected String computeFullName() {
		String fullName = "";
		if(saleOrder != null && saleOrder.getSaleOrderSeq() != null){
			fullName += saleOrder.getSaleOrderSeq();
		}
		if(productName != null)  {
			fullName += "-";
			if(productName.length() > 100)  {
				fullName += productName.substring(1, 100);
			}
			else  {  fullName += productName;  }
		}
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public SaleOrder getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(SaleOrder saleOrder) {
		this.saleOrder = saleOrder;
	}

	public Integer getSequence() {
		return sequence == null ? 0 : sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public BigDecimal getQty() {
		return qty == null ? BigDecimal.ZERO : qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getOldQty() {
		return oldQty == null ? BigDecimal.ZERO : oldQty;
	}

	public void setOldQty(BigDecimal oldQty) {
		this.oldQty = oldQty;
	}

	public Boolean getIsToPrintLineSubTotal() {
		return isToPrintLineSubTotal == null ? Boolean.FALSE : isToPrintLineSubTotal;
	}

	public void setIsToPrintLineSubTotal(Boolean isToPrintLineSubTotal) {
		this.isToPrintLineSubTotal = isToPrintLineSubTotal;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getPrice() {
		return price == null ? BigDecimal.ZERO : price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getInTaxPrice() {
		return inTaxPrice == null ? BigDecimal.ZERO : inTaxPrice;
	}

	public void setInTaxPrice(BigDecimal inTaxPrice) {
		this.inTaxPrice = inTaxPrice;
	}

	public BigDecimal getPriceDiscounted() {
		return priceDiscounted == null ? BigDecimal.ZERO : priceDiscounted;
	}

	public void setPriceDiscounted(BigDecimal priceDiscounted) {
		this.priceDiscounted = priceDiscounted;
	}

	public TaxLine getTaxLine() {
		return taxLine;
	}

	public void setTaxLine(TaxLine taxLine) {
		this.taxLine = taxLine;
	}

	public TaxEquiv getTaxEquiv() {
		return taxEquiv;
	}

	public void setTaxEquiv(TaxEquiv taxEquiv) {
		this.taxEquiv = taxEquiv;
	}

	public BigDecimal getExTaxTotal() {
		return exTaxTotal == null ? BigDecimal.ZERO : exTaxTotal;
	}

	public void setExTaxTotal(BigDecimal exTaxTotal) {
		this.exTaxTotal = exTaxTotal;
	}

	public BigDecimal getInTaxTotal() {
		return inTaxTotal == null ? BigDecimal.ZERO : inTaxTotal;
	}

	public void setInTaxTotal(BigDecimal inTaxTotal) {
		this.inTaxTotal = inTaxTotal;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Partner getSupplierPartner() {
		return supplierPartner;
	}

	public void setSupplierPartner(Partner supplierPartner) {
		this.supplierPartner = supplierPartner;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount == null ? BigDecimal.ZERO : discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Integer getDiscountTypeSelect() {
		return discountTypeSelect == null ? 0 : discountTypeSelect;
	}

	public void setDiscountTypeSelect(Integer discountTypeSelect) {
		this.discountTypeSelect = discountTypeSelect;
	}

	public LocalDate getEstimatedDelivDate() {
		return estimatedDelivDate;
	}

	public void setEstimatedDelivDate(LocalDate estimatedDelivDate) {
		this.estimatedDelivDate = estimatedDelivDate;
	}

	public LocalDate getDesiredDelivDate() {
		return desiredDelivDate;
	}

	public void setDesiredDelivDate(LocalDate desiredDelivDate) {
		this.desiredDelivDate = desiredDelivDate;
	}

	public BigDecimal getDeliveredQty() {
		return deliveredQty == null ? BigDecimal.ZERO : deliveredQty;
	}

	public void setDeliveredQty(BigDecimal deliveredQty) {
		this.deliveredQty = deliveredQty;
	}

	public BigDecimal getSubTotalCostPrice() {
		return subTotalCostPrice == null ? BigDecimal.ZERO : subTotalCostPrice;
	}

	public void setSubTotalCostPrice(BigDecimal subTotalCostPrice) {
		this.subTotalCostPrice = subTotalCostPrice;
	}

	public BigDecimal getSubTotalGrossMargin() {
		return subTotalGrossMargin == null ? BigDecimal.ZERO : subTotalGrossMargin;
	}

	public void setSubTotalGrossMargin(BigDecimal subTotalGrossMargin) {
		this.subTotalGrossMargin = subTotalGrossMargin;
	}

	public BigDecimal getSubMarginRate() {
		return subMarginRate == null ? BigDecimal.ZERO : subMarginRate;
	}

	public void setSubMarginRate(BigDecimal subMarginRate) {
		this.subMarginRate = subMarginRate;
	}

	public BigDecimal getSubTotalMarkup() {
		return subTotalMarkup == null ? BigDecimal.ZERO : subTotalMarkup;
	}

	public void setSubTotalMarkup(BigDecimal subTotalMarkup) {
		this.subTotalMarkup = subTotalMarkup;
	}

	public BigDecimal getCompanyExTaxTotal() {
		return companyExTaxTotal == null ? BigDecimal.ZERO : companyExTaxTotal;
	}

	public void setCompanyExTaxTotal(BigDecimal companyExTaxTotal) {
		this.companyExTaxTotal = companyExTaxTotal;
	}

	public BigDecimal getCompanyCostPrice() {
		return companyCostPrice == null ? BigDecimal.ZERO : companyCostPrice;
	}

	public void setCompanyCostPrice(BigDecimal companyCostPrice) {
		this.companyCostPrice = companyCostPrice;
	}

	public BigDecimal getCompanyInTaxTotal() {
		return companyInTaxTotal == null ? BigDecimal.ZERO : companyInTaxTotal;
	}

	public void setCompanyInTaxTotal(BigDecimal companyInTaxTotal) {
		this.companyInTaxTotal = companyInTaxTotal;
	}

	public BigDecimal getCompanyCostTotal() {
		return companyCostTotal == null ? BigDecimal.ZERO : companyCostTotal;
	}

	public void setCompanyCostTotal(BigDecimal companyCostTotal) {
		this.companyCostTotal = companyCostTotal;
	}

	public Integer getTypeSelect() {
		return typeSelect == null ? 0 : typeSelect;
	}

	public void setTypeSelect(Integer typeSelect) {
		this.typeSelect = typeSelect;
	}

	public Boolean getIsShowTotal() {
		return isShowTotal == null ? Boolean.FALSE : isShowTotal;
	}

	public void setIsShowTotal(Boolean isShowTotal) {
		this.isShowTotal = isShowTotal;
	}

	public Boolean getIsHideUnitAmounts() {
		return isHideUnitAmounts == null ? Boolean.FALSE : isHideUnitAmounts;
	}

	public void setIsHideUnitAmounts(Boolean isHideUnitAmounts) {
		this.isHideUnitAmounts = isHideUnitAmounts;
	}

	public Integer getStatut() {
		return statut == null ? 0 : statut;
	}

	public void setStatut(Integer statut) {
		this.statut = statut;
	}

	public ModelDocument getModeleDeDescriptifDeConsultation() {
		return modeleDeDescriptifDeConsultation;
	}

	public void setModeleDeDescriptifDeConsultation(ModelDocument modeleDeDescriptifDeConsultation) {
		this.modeleDeDescriptifDeConsultation = modeleDeDescriptifDeConsultation;
	}

	public String getDescriptifTechniqueDeLaConsultation() {
		return descriptifTechniqueDeLaConsultation;
	}

	public void setDescriptifTechniqueDeLaConsultation(String descriptifTechniqueDeLaConsultation) {
		this.descriptifTechniqueDeLaConsultation = descriptifTechniqueDeLaConsultation;
	}

	public List<SupplierConsulation> getConsultationsFournisseursList() {
		return consultationsFournisseursList;
	}

	public void setConsultationsFournisseursList(List<SupplierConsulation> consultationsFournisseursList) {
		this.consultationsFournisseursList = consultationsFournisseursList;
	}

	/**
	 * Add the given {@link SupplierConsulation} item to the {@code consultationsFournisseursList}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addConsultationsFournisseursListItem(SupplierConsulation item) {
		if (getConsultationsFournisseursList() == null) {
			setConsultationsFournisseursList(new ArrayList<>());
		}
		getConsultationsFournisseursList().add(item);
	}

	/**
	 * Remove the given {@link SupplierConsulation} item from the {@code consultationsFournisseursList}.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeConsultationsFournisseursListItem(SupplierConsulation item) {
		if (getConsultationsFournisseursList() == null) {
			return;
		}
		getConsultationsFournisseursList().remove(item);
	}

	/**
	 * Clear the {@code consultationsFournisseursList} collection.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 */
	public void clearConsultationsFournisseursList() {
		if (getConsultationsFournisseursList() != null) {
			getConsultationsFournisseursList().clear();
		}
	}

	public Partner getFournisseurSelectionne() {
		return fournisseurSelectionne;
	}

	public void setFournisseurSelectionne(Partner fournisseurSelectionne) {
		this.fournisseurSelectionne = fournisseurSelectionne;
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

	public BigDecimal getQteaAcheter() {
		return qteaAcheter == null ? BigDecimal.ZERO : qteaAcheter;
	}

	public void setQteaAcheter(BigDecimal qteaAcheter) {
		this.qteaAcheter = qteaAcheter;
	}

	public BigDecimal getSousTotaldAchatHT() {
		return sousTotaldAchatHT == null ? BigDecimal.ZERO : sousTotaldAchatHT;
	}

	public void setSousTotaldAchatHT(BigDecimal sousTotaldAchatHT) {
		this.sousTotaldAchatHT = sousTotaldAchatHT;
	}

	public BigDecimal getPrixUnitairedAchat() {
		return prixUnitairedAchat == null ? BigDecimal.ZERO : prixUnitairedAchat;
	}

	public void setPrixUnitairedAchat(BigDecimal prixUnitairedAchat) {
		this.prixUnitairedAchat = prixUnitairedAchat;
	}

	public Boolean getCacherLignealImpression() {
		return cacherLignealImpression == null ? Boolean.FALSE : cacherLignealImpression;
	}

	public void setCacherLignealImpression(Boolean cacherLignealImpression) {
		this.cacherLignealImpression = cacherLignealImpression;
	}

	public Boolean getCacherSousTotaux() {
		return cacherSousTotaux == null ? Boolean.FALSE : cacherSousTotaux;
	}

	public void setCacherSousTotaux(Boolean cacherSousTotaux) {
		this.cacherSousTotaux = cacherSousTotaux;
	}

	public Set<SupplierOption> getListeDesOptions() {
		return listeDesOptions;
	}

	public void setListeDesOptions(Set<SupplierOption> listeDesOptions) {
		this.listeDesOptions = listeDesOptions;
	}

	/**
	 * Add the given {@link SupplierOption} item to the {@code listeDesOptions}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addListeDesOption(SupplierOption item) {
		if (getListeDesOptions() == null) {
			setListeDesOptions(new HashSet<>());
		}
		getListeDesOptions().add(item);
	}

	/**
	 * Remove the given {@link SupplierOption} item from the {@code listeDesOptions}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeListeDesOption(SupplierOption item) {
		if (getListeDesOptions() == null) {
			return;
		}
		getListeDesOptions().remove(item);
	}

	/**
	 * Clear the {@code listeDesOptions} collection.
	 *
	 */
	public void clearListeDesOptions() {
		if (getListeDesOptions() != null) {
			getListeDesOptions().clear();
		}
	}

	public Set<SupplierQtity> getListeDesQuantites() {
		return listeDesQuantites;
	}

	public void setListeDesQuantites(Set<SupplierQtity> listeDesQuantites) {
		this.listeDesQuantites = listeDesQuantites;
	}

	/**
	 * Add the given {@link SupplierQtity} item to the {@code listeDesQuantites}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addListeDesQuantite(SupplierQtity item) {
		if (getListeDesQuantites() == null) {
			setListeDesQuantites(new HashSet<>());
		}
		getListeDesQuantites().add(item);
	}

	/**
	 * Remove the given {@link SupplierQtity} item from the {@code listeDesQuantites}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeListeDesQuantite(SupplierQtity item) {
		if (getListeDesQuantites() == null) {
			return;
		}
		getListeDesQuantites().remove(item);
	}

	/**
	 * Clear the {@code listeDesQuantites} collection.
	 *
	 */
	public void clearListeDesQuantites() {
		if (getListeDesQuantites() != null) {
			getListeDesQuantites().clear();
		}
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
		if (!(obj instanceof SaleOrderLine)) return false;

		final SaleOrderLine other = (SaleOrderLine) obj;
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
			.add("sequence", getSequence())
			.add("qty", getQty())
			.add("oldQty", getOldQty())
			.add("isToPrintLineSubTotal", getIsToPrintLineSubTotal())
			.add("productName", getProductName())
			.add("price", getPrice())
			.add("inTaxPrice", getInTaxPrice())
			.add("priceDiscounted", getPriceDiscounted())
			.add("exTaxTotal", getExTaxTotal())
			.add("inTaxTotal", getInTaxTotal())
			.add("discountAmount", getDiscountAmount())
			.omitNullValues()
			.toString();
	}
}
