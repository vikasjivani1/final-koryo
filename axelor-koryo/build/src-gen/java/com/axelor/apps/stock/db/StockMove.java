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
package com.axelor.apps.stock.db;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.apps.base.db.Address;
import com.axelor.apps.base.db.CancelReason;
import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.PrintingSettings;
import com.axelor.apps.base.db.TradingName;
import com.axelor.apps.message.db.Template;
import com.axelor.apps.purchase.db.PurchaseOrder;
import com.axelor.auth.db.AuditableModel;
import com.axelor.auth.db.User;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Track;
import com.axelor.db.annotations.TrackEvent;
import com.axelor.db.annotations.TrackField;
import com.axelor.db.annotations.TrackMessage;
import com.axelor.db.annotations.Widget;
import com.axelor.meta.db.MetaFile;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "STOCK_STOCK_MOVE", uniqueConstraints = { @UniqueConstraint(columnNames = { "stockMoveSeq", "company" }) }, indexes = { @Index(columnList = "stockMoveSeq"), @Index(columnList = "name"), @Index(columnList = "from_stock_location"), @Index(columnList = "to_stock_location"), @Index(columnList = "from_address"), @Index(columnList = "to_address"), @Index(columnList = "company"), @Index(columnList = "partner"), @Index(columnList = "shipment_mode"), @Index(columnList = "freight_carrier_mode"), @Index(columnList = "carrier_partner"), @Index(columnList = "forwarder_partner"), @Index(columnList = "incoterm"), @Index(columnList = "cancel_reason"), @Index(columnList = "planned_stock_move_message_template"), @Index(columnList = "real_stock_move_message_template"), @Index(columnList = "trading_name"), @Index(columnList = "printing_settings"), @Index(columnList = "electronic_signature"), @Index(columnList = "signatory_user"), @Index(columnList = "stock_move_origin"), @Index(columnList = "stock_correction_reason"), @Index(columnList = "reversion_origin_stock_move"), @Index(columnList = "entity_group"), @Index(columnList = "billing_address"), @Index(columnList = "provider"), @Index(columnList = "control_supplier") })
@Track(fields = { @TrackField(name = "stockMoveSeq"), @TrackField(name = "typeSelect"), @TrackField(name = "partner"), @TrackField(name = "statusSelect"), @TrackField(name = "fromStockLocation"), @TrackField(name = "toStockLocation"), @TrackField(name = "estimatedDate"), @TrackField(name = "cancelReason"), @TrackField(name = "realDate", condition = "statusSelect == 3"), @TrackField(name = "pickingIsEdited"), @TrackField(name = "availabilityRequest") }, messages = { @TrackMessage(message = "Move created", condition = "true", on = TrackEvent.CREATE), @TrackMessage(message = "Draft move", condition = "statusSelect == 1", tag = "important"), @TrackMessage(message = "Planned move", condition = "statusSelect == 2", tag = "info"), @TrackMessage(message = "Move realized", condition = "statusSelect == 3", tag = "success"), @TrackMessage(message = "Move canceled", condition = "statusSelect == 4", tag = "warning") })
public class StockMove extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_STOCK_MOVE_SEQ")
	@SequenceGenerator(name = "STOCK_STOCK_MOVE_SEQ", sequenceName = "STOCK_STOCK_MOVE_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Ref.", readonly = true)
	@NameColumn
	private String stockMoveSeq;

	@Widget(title = "Name")
	private String name;

	@Widget(title = "From stock location")
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private StockLocation fromStockLocation;

	@Widget(title = "To stock location")
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private StockLocation toStockLocation;

	@Widget(title = "Estimated date")
	private LocalDate estimatedDate;

	@Widget(title = "Move date", readonly = true)
	private LocalDate realDate;

	@Widget(title = "Supplier Shipment Date")
	private LocalDate supplierShipmentDate;

	@Widget(title = "Supplier Shipment Ref")
	private String supplierShipmentRef;

	@Widget(title = "Status", readonly = true, selection = "stock.move.status.select")
	@NotNull
	private Integer statusSelect = 1;

	@Widget(title = "Type Status", selection = "stock.move.type.select")
	@NotNull
	private Integer typeSelect = 0;

	@Widget(title = "Available status", selection = "stock.stock.move.available.status.select")
	@Transient
	private Integer availableStatusSelect = 0;

	@Widget(title = "From address")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Address fromAddress;

	@Widget(title = "To address")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Address toAddress;

	@Widget(title = "From address", multiline = true)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String fromAddressStr;

	@Widget(title = "To address", multiline = true)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String toAddressStr;

	@Widget(title = "Company")
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Company company;

	@Widget(title = "Partner")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner partner;

	@Widget(title = "Stock move content")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "stockMove", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("sequence")
	private List<StockMoveLine> stockMoveLineList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "plannedStockMove", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("sequence")
	private List<StockMoveLine> plannedStockMoveLineList;

	@Widget(title = "Return surplus")
	private Boolean isWithReturnSurplus = Boolean.FALSE;

	@Widget(title = "Manage backorder")
	private Boolean isWithBackorder = Boolean.FALSE;

	@Widget(title = "Tracking Number")
	private String trackingNumber;

	@Widget(title = "Number Of Packages")
	private Integer numOfPackages = 0;

	@Widget(title = "Number Of Palettes")
	private Integer numOfPalettes = 0;

	@Widget(title = "Gross Mass")
	private BigDecimal grossMass = BigDecimal.ZERO;

	@Widget(title = "Total W.T.")
	@Digits(integer = 18, fraction = 2)
	private BigDecimal exTaxTotal = BigDecimal.ZERO;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ShipmentMode shipmentMode;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private FreightCarrierMode freightCarrierMode;

	@Widget(title = "Carrier")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner carrierPartner;

	@Widget(title = "Forwarder")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner forwarderPartner;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Incoterm incoterm;

	@Widget(title = "Conformity", selection = "stock.move.line.conformity.select")
	private Integer conformitySelect = 0;

	@Widget(title = "Fully spread over logistical forms")
	private Boolean fullySpreadOverLogisticalFormsFlag = Boolean.FALSE;

	@Widget(title = "Cancel reason")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private CancelReason cancelReason;

	@Widget(title = "Send email when planning stock move")
	private Boolean plannedStockMoveAutomaticMail = Boolean.FALSE;

	@Widget(title = "Message template")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Template plannedStockMoveMessageTemplate;

	@Widget(title = "Send email on stock move realization")
	private Boolean realStockMoveAutomaticMail = Boolean.FALSE;

	@Widget(title = "Message template")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Template realStockMoveMessageTemplate;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private TradingName tradingName;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private PrintingSettings printingSettings;

	@Widget(title = "Is ISPM15 required")
	private Boolean isIspmRequired = Boolean.FALSE;

	@Widget(readonly = true)
	private Boolean isReversion = Boolean.FALSE;

	@Widget(title = "Certificate of conformity")
	private Boolean isNeedingConformityCertificate = Boolean.FALSE;

	@Widget(title = "Is certificate of conformity signed")
	private Boolean isConformityCertifSigned = Boolean.FALSE;

	@Widget(title = "Electronic signature")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private MetaFile electronicSignature;

	@Widget(title = "Signatory user")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User signatoryUser;

	@Widget(title = "Backorder Id")
	private Long backorderId = 0L;

	@Widget(title = "Stock Move Origin")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private StockMove stockMoveOrigin;

	@Widget(title = "Origin Type", selection = "stockmove.origin.select")
	private String originTypeSelect;

	@Widget(title = "Origin Id")
	private Long originId = 0L;

	@Widget(title = "Origin", readonly = true)
	private String origin;

	@Widget(title = "Availability request")
	private Boolean availabilityRequest = Boolean.FALSE;

	@Widget(title = "Correction Reason")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private StockCorrectionReason stockCorrectionReason;

	@Widget(title = "Picking order is edited")
	private Boolean pickingIsEdited = Boolean.FALSE;

	@Widget(title = "Picking order edit date")
	private LocalDate pickingEditDate;

	@Basic
	@Type(type = "com.axelor.db.hibernate.type.ValueEnumType")
	private ModeOfTransport modeOfTransport;

	@Widget(title = "Notes")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String note;

	@Widget(title = "Filter on available products")
	@Transient
	private Boolean filterOnAvailableProducts = Boolean.TRUE;

	@Widget(title = "Origin stock move")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private StockMove reversionOriginStockMove;

	@Widget(title = "Comment to display on picking order")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String pickingOrderComments;

	@Widget(title = "Entity group")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner entityGroup;

	@Widget(title = "Billing address")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Address billingAddress;

	@Widget(title = "Billing status", selection = "koryo.app.stock.move.billing.status.select")
	private Integer billingStatus = 0;

	@Widget(title = "Provider")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner provider;

	@Widget(title = "Control supplier")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private PurchaseOrder controlSupplier;

	@Widget(title = "Litigation")
	private Boolean litigation = Boolean.FALSE;

	@Widget(title = "Litigation info")
	private String litigationInfo;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public StockMove() {
	}

	public StockMove(String name) {
		this.name = name;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getStockMoveSeq() {
		return stockMoveSeq;
	}

	public void setStockMoveSeq(String stockMoveSeq) {
		this.stockMoveSeq = stockMoveSeq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StockLocation getFromStockLocation() {
		return fromStockLocation;
	}

	public void setFromStockLocation(StockLocation fromStockLocation) {
		this.fromStockLocation = fromStockLocation;
	}

	public StockLocation getToStockLocation() {
		return toStockLocation;
	}

	public void setToStockLocation(StockLocation toStockLocation) {
		this.toStockLocation = toStockLocation;
	}

	public LocalDate getEstimatedDate() {
		return estimatedDate;
	}

	public void setEstimatedDate(LocalDate estimatedDate) {
		this.estimatedDate = estimatedDate;
	}

	public LocalDate getRealDate() {
		return realDate;
	}

	public void setRealDate(LocalDate realDate) {
		this.realDate = realDate;
	}

	public LocalDate getSupplierShipmentDate() {
		return supplierShipmentDate;
	}

	public void setSupplierShipmentDate(LocalDate supplierShipmentDate) {
		this.supplierShipmentDate = supplierShipmentDate;
	}

	public String getSupplierShipmentRef() {
		return supplierShipmentRef;
	}

	public void setSupplierShipmentRef(String supplierShipmentRef) {
		this.supplierShipmentRef = supplierShipmentRef;
	}

	public Integer getStatusSelect() {
		return statusSelect == null ? 0 : statusSelect;
	}

	public void setStatusSelect(Integer statusSelect) {
		this.statusSelect = statusSelect;
	}

	public Integer getTypeSelect() {
		return typeSelect == null ? 0 : typeSelect;
	}

	public void setTypeSelect(Integer typeSelect) {
		this.typeSelect = typeSelect;
	}

	public Integer getAvailableStatusSelect() {
		return availableStatusSelect == null ? 0 : availableStatusSelect;
	}

	public void setAvailableStatusSelect(Integer availableStatusSelect) {
		this.availableStatusSelect = availableStatusSelect;
	}

	public Address getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(Address fromAddress) {
		this.fromAddress = fromAddress;
	}

	public Address getToAddress() {
		return toAddress;
	}

	public void setToAddress(Address toAddress) {
		this.toAddress = toAddress;
	}

	public String getFromAddressStr() {
		return fromAddressStr;
	}

	public void setFromAddressStr(String fromAddressStr) {
		this.fromAddressStr = fromAddressStr;
	}

	public String getToAddressStr() {
		return toAddressStr;
	}

	public void setToAddressStr(String toAddressStr) {
		this.toAddressStr = toAddressStr;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public List<StockMoveLine> getStockMoveLineList() {
		return stockMoveLineList;
	}

	public void setStockMoveLineList(List<StockMoveLine> stockMoveLineList) {
		this.stockMoveLineList = stockMoveLineList;
	}

	/**
	 * Add the given {@link StockMoveLine} item to the {@code stockMoveLineList}.
	 *
	 * <p>
	 * It sets {@code item.stockMove = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addStockMoveLineListItem(StockMoveLine item) {
		if (getStockMoveLineList() == null) {
			setStockMoveLineList(new ArrayList<>());
		}
		getStockMoveLineList().add(item);
		item.setStockMove(this);
	}

	/**
	 * Remove the given {@link StockMoveLine} item from the {@code stockMoveLineList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeStockMoveLineListItem(StockMoveLine item) {
		if (getStockMoveLineList() == null) {
			return;
		}
		getStockMoveLineList().remove(item);
	}

	/**
	 * Clear the {@code stockMoveLineList} collection.
	 *
	 * <p>
	 * If you have to query {@link StockMoveLine} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearStockMoveLineList() {
		if (getStockMoveLineList() != null) {
			getStockMoveLineList().clear();
		}
	}

	public List<StockMoveLine> getPlannedStockMoveLineList() {
		return plannedStockMoveLineList;
	}

	public void setPlannedStockMoveLineList(List<StockMoveLine> plannedStockMoveLineList) {
		this.plannedStockMoveLineList = plannedStockMoveLineList;
	}

	/**
	 * Add the given {@link StockMoveLine} item to the {@code plannedStockMoveLineList}.
	 *
	 * <p>
	 * It sets {@code item.plannedStockMove = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addPlannedStockMoveLineListItem(StockMoveLine item) {
		if (getPlannedStockMoveLineList() == null) {
			setPlannedStockMoveLineList(new ArrayList<>());
		}
		getPlannedStockMoveLineList().add(item);
		item.setPlannedStockMove(this);
	}

	/**
	 * Remove the given {@link StockMoveLine} item from the {@code plannedStockMoveLineList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removePlannedStockMoveLineListItem(StockMoveLine item) {
		if (getPlannedStockMoveLineList() == null) {
			return;
		}
		getPlannedStockMoveLineList().remove(item);
	}

	/**
	 * Clear the {@code plannedStockMoveLineList} collection.
	 *
	 * <p>
	 * If you have to query {@link StockMoveLine} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearPlannedStockMoveLineList() {
		if (getPlannedStockMoveLineList() != null) {
			getPlannedStockMoveLineList().clear();
		}
	}

	public Boolean getIsWithReturnSurplus() {
		return isWithReturnSurplus == null ? Boolean.FALSE : isWithReturnSurplus;
	}

	public void setIsWithReturnSurplus(Boolean isWithReturnSurplus) {
		this.isWithReturnSurplus = isWithReturnSurplus;
	}

	public Boolean getIsWithBackorder() {
		return isWithBackorder == null ? Boolean.FALSE : isWithBackorder;
	}

	public void setIsWithBackorder(Boolean isWithBackorder) {
		this.isWithBackorder = isWithBackorder;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public Integer getNumOfPackages() {
		return numOfPackages == null ? 0 : numOfPackages;
	}

	public void setNumOfPackages(Integer numOfPackages) {
		this.numOfPackages = numOfPackages;
	}

	public Integer getNumOfPalettes() {
		return numOfPalettes == null ? 0 : numOfPalettes;
	}

	public void setNumOfPalettes(Integer numOfPalettes) {
		this.numOfPalettes = numOfPalettes;
	}

	public BigDecimal getGrossMass() {
		return grossMass == null ? BigDecimal.ZERO : grossMass;
	}

	public void setGrossMass(BigDecimal grossMass) {
		this.grossMass = grossMass;
	}

	public BigDecimal getExTaxTotal() {
		return exTaxTotal == null ? BigDecimal.ZERO : exTaxTotal;
	}

	public void setExTaxTotal(BigDecimal exTaxTotal) {
		this.exTaxTotal = exTaxTotal;
	}

	public ShipmentMode getShipmentMode() {
		return shipmentMode;
	}

	public void setShipmentMode(ShipmentMode shipmentMode) {
		this.shipmentMode = shipmentMode;
	}

	public FreightCarrierMode getFreightCarrierMode() {
		return freightCarrierMode;
	}

	public void setFreightCarrierMode(FreightCarrierMode freightCarrierMode) {
		this.freightCarrierMode = freightCarrierMode;
	}

	public Partner getCarrierPartner() {
		return carrierPartner;
	}

	public void setCarrierPartner(Partner carrierPartner) {
		this.carrierPartner = carrierPartner;
	}

	public Partner getForwarderPartner() {
		return forwarderPartner;
	}

	public void setForwarderPartner(Partner forwarderPartner) {
		this.forwarderPartner = forwarderPartner;
	}

	public Incoterm getIncoterm() {
		return incoterm;
	}

	public void setIncoterm(Incoterm incoterm) {
		this.incoterm = incoterm;
	}

	public Integer getConformitySelect() {
		return conformitySelect == null ? 0 : conformitySelect;
	}

	public void setConformitySelect(Integer conformitySelect) {
		this.conformitySelect = conformitySelect;
	}

	public Boolean getFullySpreadOverLogisticalFormsFlag() {
		return fullySpreadOverLogisticalFormsFlag == null ? Boolean.FALSE : fullySpreadOverLogisticalFormsFlag;
	}

	public void setFullySpreadOverLogisticalFormsFlag(Boolean fullySpreadOverLogisticalFormsFlag) {
		this.fullySpreadOverLogisticalFormsFlag = fullySpreadOverLogisticalFormsFlag;
	}

	public CancelReason getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(CancelReason cancelReason) {
		this.cancelReason = cancelReason;
	}

	public Boolean getPlannedStockMoveAutomaticMail() {
		return plannedStockMoveAutomaticMail == null ? Boolean.FALSE : plannedStockMoveAutomaticMail;
	}

	public void setPlannedStockMoveAutomaticMail(Boolean plannedStockMoveAutomaticMail) {
		this.plannedStockMoveAutomaticMail = plannedStockMoveAutomaticMail;
	}

	public Template getPlannedStockMoveMessageTemplate() {
		return plannedStockMoveMessageTemplate;
	}

	public void setPlannedStockMoveMessageTemplate(Template plannedStockMoveMessageTemplate) {
		this.plannedStockMoveMessageTemplate = plannedStockMoveMessageTemplate;
	}

	public Boolean getRealStockMoveAutomaticMail() {
		return realStockMoveAutomaticMail == null ? Boolean.FALSE : realStockMoveAutomaticMail;
	}

	public void setRealStockMoveAutomaticMail(Boolean realStockMoveAutomaticMail) {
		this.realStockMoveAutomaticMail = realStockMoveAutomaticMail;
	}

	public Template getRealStockMoveMessageTemplate() {
		return realStockMoveMessageTemplate;
	}

	public void setRealStockMoveMessageTemplate(Template realStockMoveMessageTemplate) {
		this.realStockMoveMessageTemplate = realStockMoveMessageTemplate;
	}

	public TradingName getTradingName() {
		return tradingName;
	}

	public void setTradingName(TradingName tradingName) {
		this.tradingName = tradingName;
	}

	public PrintingSettings getPrintingSettings() {
		return printingSettings;
	}

	public void setPrintingSettings(PrintingSettings printingSettings) {
		this.printingSettings = printingSettings;
	}

	public Boolean getIsIspmRequired() {
		return isIspmRequired == null ? Boolean.FALSE : isIspmRequired;
	}

	public void setIsIspmRequired(Boolean isIspmRequired) {
		this.isIspmRequired = isIspmRequired;
	}

	public Boolean getIsReversion() {
		return isReversion == null ? Boolean.FALSE : isReversion;
	}

	public void setIsReversion(Boolean isReversion) {
		this.isReversion = isReversion;
	}

	public Boolean getIsNeedingConformityCertificate() {
		return isNeedingConformityCertificate == null ? Boolean.FALSE : isNeedingConformityCertificate;
	}

	public void setIsNeedingConformityCertificate(Boolean isNeedingConformityCertificate) {
		this.isNeedingConformityCertificate = isNeedingConformityCertificate;
	}

	public Boolean getIsConformityCertifSigned() {
		return isConformityCertifSigned == null ? Boolean.FALSE : isConformityCertifSigned;
	}

	public void setIsConformityCertifSigned(Boolean isConformityCertifSigned) {
		this.isConformityCertifSigned = isConformityCertifSigned;
	}

	public MetaFile getElectronicSignature() {
		return electronicSignature;
	}

	public void setElectronicSignature(MetaFile electronicSignature) {
		this.electronicSignature = electronicSignature;
	}

	public User getSignatoryUser() {
		return signatoryUser;
	}

	public void setSignatoryUser(User signatoryUser) {
		this.signatoryUser = signatoryUser;
	}

	public Long getBackorderId() {
		return backorderId == null ? 0L : backorderId;
	}

	public void setBackorderId(Long backorderId) {
		this.backorderId = backorderId;
	}

	public StockMove getStockMoveOrigin() {
		return stockMoveOrigin;
	}

	public void setStockMoveOrigin(StockMove stockMoveOrigin) {
		this.stockMoveOrigin = stockMoveOrigin;
	}

	public String getOriginTypeSelect() {
		return originTypeSelect;
	}

	public void setOriginTypeSelect(String originTypeSelect) {
		this.originTypeSelect = originTypeSelect;
	}

	public Long getOriginId() {
		return originId == null ? 0L : originId;
	}

	public void setOriginId(Long originId) {
		this.originId = originId;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Boolean getAvailabilityRequest() {
		return availabilityRequest == null ? Boolean.FALSE : availabilityRequest;
	}

	public void setAvailabilityRequest(Boolean availabilityRequest) {
		this.availabilityRequest = availabilityRequest;
	}

	public StockCorrectionReason getStockCorrectionReason() {
		return stockCorrectionReason;
	}

	public void setStockCorrectionReason(StockCorrectionReason stockCorrectionReason) {
		this.stockCorrectionReason = stockCorrectionReason;
	}

	public Boolean getPickingIsEdited() {
		return pickingIsEdited == null ? Boolean.FALSE : pickingIsEdited;
	}

	public void setPickingIsEdited(Boolean pickingIsEdited) {
		this.pickingIsEdited = pickingIsEdited;
	}

	public LocalDate getPickingEditDate() {
		return pickingEditDate;
	}

	public void setPickingEditDate(LocalDate pickingEditDate) {
		this.pickingEditDate = pickingEditDate;
	}

	public ModeOfTransport getModeOfTransport() {
		return modeOfTransport;
	}

	public void setModeOfTransport(ModeOfTransport modeOfTransport) {
		this.modeOfTransport = modeOfTransport;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Boolean getFilterOnAvailableProducts() {
		return filterOnAvailableProducts == null ? Boolean.FALSE : filterOnAvailableProducts;
	}

	public void setFilterOnAvailableProducts(Boolean filterOnAvailableProducts) {
		this.filterOnAvailableProducts = filterOnAvailableProducts;
	}

	public StockMove getReversionOriginStockMove() {
		return reversionOriginStockMove;
	}

	public void setReversionOriginStockMove(StockMove reversionOriginStockMove) {
		this.reversionOriginStockMove = reversionOriginStockMove;
	}

	public String getPickingOrderComments() {
		return pickingOrderComments;
	}

	public void setPickingOrderComments(String pickingOrderComments) {
		this.pickingOrderComments = pickingOrderComments;
	}

	public Partner getEntityGroup() {
		return entityGroup;
	}

	public void setEntityGroup(Partner entityGroup) {
		this.entityGroup = entityGroup;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	public Integer getBillingStatus() {
		return billingStatus == null ? 0 : billingStatus;
	}

	public void setBillingStatus(Integer billingStatus) {
		this.billingStatus = billingStatus;
	}

	public Partner getProvider() {
		return provider;
	}

	public void setProvider(Partner provider) {
		this.provider = provider;
	}

	public PurchaseOrder getControlSupplier() {
		return controlSupplier;
	}

	public void setControlSupplier(PurchaseOrder controlSupplier) {
		this.controlSupplier = controlSupplier;
	}

	public Boolean getLitigation() {
		return litigation == null ? Boolean.FALSE : litigation;
	}

	public void setLitigation(Boolean litigation) {
		this.litigation = litigation;
	}

	public String getLitigationInfo() {
		return litigationInfo;
	}

	public void setLitigationInfo(String litigationInfo) {
		this.litigationInfo = litigationInfo;
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
		if (!(obj instanceof StockMove)) return false;

		final StockMove other = (StockMove) obj;
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
			.add("stockMoveSeq", getStockMoveSeq())
			.add("name", getName())
			.add("estimatedDate", getEstimatedDate())
			.add("realDate", getRealDate())
			.add("supplierShipmentDate", getSupplierShipmentDate())
			.add("supplierShipmentRef", getSupplierShipmentRef())
			.add("statusSelect", getStatusSelect())
			.add("typeSelect", getTypeSelect())
			.add("availableStatusSelect", getAvailableStatusSelect())
			.add("isWithReturnSurplus", getIsWithReturnSurplus())
			.add("isWithBackorder", getIsWithBackorder())
			.omitNullValues()
			.toString();
	}
}
