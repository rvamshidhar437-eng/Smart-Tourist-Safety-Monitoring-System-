CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    preferred_language VARCHAR(10),
    created_at DATETIME NOT NULL
);

CREATE TABLE locations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    timestamp DATETIME NOT NULL,
    CONSTRAINT fk_locations_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE sos_alerts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    alert_time DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL,
    message VARCHAR(500),
    CONSTRAINT fk_sos_alerts_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE incident_reports (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    incident_type VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(255) NOT NULL,
    report_time DATETIME NOT NULL,
    evidence_path VARCHAR(500),
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_incident_reports_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE emergency_services (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    contact_number VARCHAR(20),
    address VARCHAR(255)
);

CREATE TABLE safety_tips (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(80) NOT NULL,
    title VARCHAR(150) NOT NULL,
    content VARCHAR(1000) NOT NULL
);

CREATE TABLE notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    recipient_role VARCHAR(20) NOT NULL,
    title VARCHAR(120) NOT NULL,
    message VARCHAR(800) NOT NULL,
    read_status BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(id)
);
