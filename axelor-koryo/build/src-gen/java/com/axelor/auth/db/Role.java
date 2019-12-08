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
package com.axelor.auth.db;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.axelor.db.annotations.HashKey;
import com.axelor.db.annotations.Track;
import com.axelor.db.annotations.TrackField;
import com.axelor.db.annotations.Widget;
import com.axelor.meta.db.MetaMenu;
import com.axelor.meta.db.MetaPermission;
import com.google.common.base.MoreObjects;

/**
 * This object stores the roles.
 */
@Entity
@Table(name = "AUTH_ROLE")
@Track(fields = { @TrackField(name = "name"), @TrackField(name = "description") })
public class Role extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTH_ROLE_SEQ")
	@SequenceGenerator(name = "AUTH_ROLE_SEQ", sequenceName = "AUTH_ROLE_SEQ", allocationSize = 1)
	private Long id;

	@HashKey
	@NotNull
	@Column(unique = true)
	private String name;

	private String description;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<MetaMenu> menus;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Permission> permissions;

	@Widget(title = "Permissions (fields)")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<MetaPermission> metaPermissions;

	public Role() {
	}

	public Role(String name) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<MetaMenu> getMenus() {
		return menus;
	}

	public void setMenus(Set<MetaMenu> menus) {
		this.menus = menus;
	}

	/**
	 * Add the given {@link MetaMenu} item to the {@code menus}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addMenu(MetaMenu item) {
		if (getMenus() == null) {
			setMenus(new HashSet<>());
		}
		getMenus().add(item);
	}

	/**
	 * Remove the given {@link MetaMenu} item from the {@code menus}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeMenu(MetaMenu item) {
		if (getMenus() == null) {
			return;
		}
		getMenus().remove(item);
	}

	/**
	 * Clear the {@code menus} collection.
	 *
	 */
	public void clearMenus() {
		if (getMenus() != null) {
			getMenus().clear();
		}
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * Add the given {@link Permission} item to the {@code permissions}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addPermission(Permission item) {
		if (getPermissions() == null) {
			setPermissions(new HashSet<>());
		}
		getPermissions().add(item);
	}

	/**
	 * Remove the given {@link Permission} item from the {@code permissions}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removePermission(Permission item) {
		if (getPermissions() == null) {
			return;
		}
		getPermissions().remove(item);
	}

	/**
	 * Clear the {@code permissions} collection.
	 *
	 */
	public void clearPermissions() {
		if (getPermissions() != null) {
			getPermissions().clear();
		}
	}

	public Set<MetaPermission> getMetaPermissions() {
		return metaPermissions;
	}

	public void setMetaPermissions(Set<MetaPermission> metaPermissions) {
		this.metaPermissions = metaPermissions;
	}

	/**
	 * Add the given {@link MetaPermission} item to the {@code metaPermissions}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addMetaPermission(MetaPermission item) {
		if (getMetaPermissions() == null) {
			setMetaPermissions(new HashSet<>());
		}
		getMetaPermissions().add(item);
	}

	/**
	 * Remove the given {@link MetaPermission} item from the {@code metaPermissions}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeMetaPermission(MetaPermission item) {
		if (getMetaPermissions() == null) {
			return;
		}
		getMetaPermissions().remove(item);
	}

	/**
	 * Clear the {@code metaPermissions} collection.
	 *
	 */
	public void clearMetaPermissions() {
		if (getMetaPermissions() != null) {
			getMetaPermissions().clear();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof Role)) return false;

		final Role other = (Role) obj;
		if (this.getId() != null || other.getId() != null) {
			return Objects.equals(this.getId(), other.getId());
		}

		if (!Objects.equals(getName(), other.getName())) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(2552982, this.getName());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("id", getId())
			.add("name", getName())
			.add("description", getDescription())
			.omitNullValues()
			.toString();
	}
}
