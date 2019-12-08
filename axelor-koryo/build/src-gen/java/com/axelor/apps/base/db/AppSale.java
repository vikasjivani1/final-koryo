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

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.axelor.db.EntityHelper;
import com.axelor.db.annotations.Track;
import com.axelor.db.annotations.TrackEvent;
import com.axelor.db.annotations.TrackField;
import com.axelor.db.annotations.Widget;

@Entity
@Table(name = "BASE_APP_SALE")
@Track(fields = { @TrackField(name = "manageSaleOrderVersion", on = TrackEvent.UPDATE), @TrackField(name = "printingOnSOFinalization", on = TrackEvent.UPDATE), @TrackField(name = "manageSalesUnits", on = TrackEvent.UPDATE), @TrackField(name = "productPackMgt", on = TrackEvent.UPDATE), @TrackField(name = "enableConfigurator", on = TrackEvent.UPDATE), @TrackField(name = "allowPendingOrderModification", on = TrackEvent.UPDATE), @TrackField(name = "manageMultipleSaleQuantity", on = TrackEvent.UPDATE), @TrackField(name = "printingConfigPerSaleOrder", on = TrackEvent.UPDATE), @TrackField(name = "closeOpportunityUponSaleOrderConfirmation", on = TrackEvent.UPDATE), @TrackField(name = "salemanSelect", on = TrackEvent.UPDATE), @TrackField(name = "enableCustomerCatalogMgt", on = TrackEvent.UPDATE), @TrackField(name = "isDisplaySaleOrderLineNumber", on = TrackEvent.UPDATE), @TrackField(name = "isEnabledProductDescriptionCopy", on = TrackEvent.UPDATE) })
public class AppSale extends App {

	@Widget(title = "Manage sale order versions")
	private Boolean manageSaleOrderVersion = Boolean.FALSE;

	@Widget(title = "Generate the pdf printing during sale order finalization")
	private Boolean printingOnSOFinalization = Boolean.FALSE;

	@Widget(title = "Manage sales unit on products")
	private Boolean manageSalesUnits = Boolean.FALSE;

	@Widget(title = "Enable business configurator")
	private Boolean enableConfigurator = Boolean.FALSE;

	private Boolean allowPendingOrderModification = Boolean.FALSE;

	@Widget(title = "Manage multiple sale quantity")
	private Boolean manageMultipleSaleQuantity = Boolean.FALSE;

	@Widget(title = "Printing config per Sale Order")
	private Boolean printingConfigPerSaleOrder = Boolean.FALSE;

	@Widget(title = "Close opportunity when one of the linked sale orders is confirmed")
	private Boolean closeOpportunityUponSaleOrderConfirmation = Boolean.FALSE;

	@Widget(title = "Enable product description copy")
	private Boolean isEnabledProductDescriptionCopy = Boolean.FALSE;

	@Widget(title = "User to fill saleman", selection = "sale.order.fill.saleman.select")
	private Integer salemanSelect = 1;

	@Widget(title = "Enable customer catalog management")
	private Boolean enableCustomerCatalogMgt = Boolean.FALSE;

	@Widget(title = "Pack Management")
	private Boolean enablePackManagement = Boolean.FALSE;

	@Widget(title = "Display sale order line number")
	private Boolean isDisplaySaleOrderLineNumber = Boolean.FALSE;

	@Widget(title = "coefficient de majoration devises")
	private BigDecimal currencyIncreaseCoefficient = BigDecimal.ZERO;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public AppSale() {
	}

	public Boolean getManageSaleOrderVersion() {
		return manageSaleOrderVersion == null ? Boolean.FALSE : manageSaleOrderVersion;
	}

	public void setManageSaleOrderVersion(Boolean manageSaleOrderVersion) {
		this.manageSaleOrderVersion = manageSaleOrderVersion;
	}

	public Boolean getPrintingOnSOFinalization() {
		return printingOnSOFinalization == null ? Boolean.FALSE : printingOnSOFinalization;
	}

	public void setPrintingOnSOFinalization(Boolean printingOnSOFinalization) {
		this.printingOnSOFinalization = printingOnSOFinalization;
	}

	public Boolean getManageSalesUnits() {
		return manageSalesUnits == null ? Boolean.FALSE : manageSalesUnits;
	}

	public void setManageSalesUnits(Boolean manageSalesUnits) {
		this.manageSalesUnits = manageSalesUnits;
	}

	public Boolean getEnableConfigurator() {
		return enableConfigurator == null ? Boolean.FALSE : enableConfigurator;
	}

	public void setEnableConfigurator(Boolean enableConfigurator) {
		this.enableConfigurator = enableConfigurator;
	}

	public Boolean getAllowPendingOrderModification() {
		return allowPendingOrderModification == null ? Boolean.FALSE : allowPendingOrderModification;
	}

	public void setAllowPendingOrderModification(Boolean allowPendingOrderModification) {
		this.allowPendingOrderModification = allowPendingOrderModification;
	}

	public Boolean getManageMultipleSaleQuantity() {
		return manageMultipleSaleQuantity == null ? Boolean.FALSE : manageMultipleSaleQuantity;
	}

	public void setManageMultipleSaleQuantity(Boolean manageMultipleSaleQuantity) {
		this.manageMultipleSaleQuantity = manageMultipleSaleQuantity;
	}

	public Boolean getPrintingConfigPerSaleOrder() {
		return printingConfigPerSaleOrder == null ? Boolean.FALSE : printingConfigPerSaleOrder;
	}

	public void setPrintingConfigPerSaleOrder(Boolean printingConfigPerSaleOrder) {
		this.printingConfigPerSaleOrder = printingConfigPerSaleOrder;
	}

	public Boolean getCloseOpportunityUponSaleOrderConfirmation() {
		return closeOpportunityUponSaleOrderConfirmation == null ? Boolean.FALSE : closeOpportunityUponSaleOrderConfirmation;
	}

	public void setCloseOpportunityUponSaleOrderConfirmation(Boolean closeOpportunityUponSaleOrderConfirmation) {
		this.closeOpportunityUponSaleOrderConfirmation = closeOpportunityUponSaleOrderConfirmation;
	}

	public Boolean getIsEnabledProductDescriptionCopy() {
		return isEnabledProductDescriptionCopy == null ? Boolean.FALSE : isEnabledProductDescriptionCopy;
	}

	public void setIsEnabledProductDescriptionCopy(Boolean isEnabledProductDescriptionCopy) {
		this.isEnabledProductDescriptionCopy = isEnabledProductDescriptionCopy;
	}

	public Integer getSalemanSelect() {
		return salemanSelect == null ? 0 : salemanSelect;
	}

	public void setSalemanSelect(Integer salemanSelect) {
		this.salemanSelect = salemanSelect;
	}

	public Boolean getEnableCustomerCatalogMgt() {
		return enableCustomerCatalogMgt == null ? Boolean.FALSE : enableCustomerCatalogMgt;
	}

	public void setEnableCustomerCatalogMgt(Boolean enableCustomerCatalogMgt) {
		this.enableCustomerCatalogMgt = enableCustomerCatalogMgt;
	}

	public Boolean getEnablePackManagement() {
		return enablePackManagement == null ? Boolean.FALSE : enablePackManagement;
	}

	public void setEnablePackManagement(Boolean enablePackManagement) {
		this.enablePackManagement = enablePackManagement;
	}

	public Boolean getIsDisplaySaleOrderLineNumber() {
		return isDisplaySaleOrderLineNumber == null ? Boolean.FALSE : isDisplaySaleOrderLineNumber;
	}

	public void setIsDisplaySaleOrderLineNumber(Boolean isDisplaySaleOrderLineNumber) {
		this.isDisplaySaleOrderLineNumber = isDisplaySaleOrderLineNumber;
	}

	public BigDecimal getCurrencyIncreaseCoefficient() {
		return currencyIncreaseCoefficient == null ? BigDecimal.ZERO : currencyIncreaseCoefficient;
	}

	public void setCurrencyIncreaseCoefficient(BigDecimal currencyIncreaseCoefficient) {
		this.currencyIncreaseCoefficient = currencyIncreaseCoefficient;
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
