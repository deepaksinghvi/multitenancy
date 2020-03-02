package com.tenancy.model.multitenancy.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "DataSourceConfig")
public class DataSourceConfig implements Serializable {

  @Id
  private Long id;
  @Column(name = "driverclassname")
  private String driverClassName;
  @Column(name = "url")
  private String url;
  @Column(name = "schemaname")
  private String schemaName;
  @Column(name = "tenantid")
  private String tenantId;
  @Column(name = "username")
  private String userName;
  @Column(name = "password")
  private String password;
}
