CREATE TABLE IF NOT EXISTS tenant (
  id BIGINT NOT NULL AUTO_INCREMENT,
  tenant_code VARCHAR(64) NOT NULL,
  tenant_name VARCHAR(128) NOT NULL,
  status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_tenant_code (tenant_code),
  KEY idx_tenant_name (tenant_name),
  KEY idx_status (status),
  KEY idx_updated_at (updated_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS process_definition (
  id BIGINT NOT NULL AUTO_INCREMENT,
  process_key VARCHAR(64) NOT NULL,
  process_name VARCHAR(128) NOT NULL,
  bpmn_xml LONGTEXT NOT NULL,
  status VARCHAR(16) NOT NULL,
  current_version INT NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_process_key (process_key),
  KEY idx_status (status),
  KEY idx_updated_at (updated_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS process_version (
  id BIGINT NOT NULL AUTO_INCREMENT,
  process_id BIGINT NOT NULL,
  version INT NOT NULL,
  deployment_id VARCHAR(64) NOT NULL,
  process_definition_id VARCHAR(128) NOT NULL,
  bpmn_xml LONGTEXT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_process_version (process_id, version),
  KEY idx_process_id (process_id),
  KEY idx_deployment_id (deployment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
