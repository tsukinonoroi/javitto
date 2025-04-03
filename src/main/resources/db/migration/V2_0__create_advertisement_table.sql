CREATE TABLE advertisement (
                               id BIGSERIAL PRIMARY KEY,
                               title VARCHAR(255),
                               description TEXT,
                               cost DECIMAL(10,2) NOT NULL,
                               parent_category VARCHAR(50),
                               sub_category VARCHAR(50),
                               date_of_creation TIMESTAMP,
                               city VARCHAR(50),
                               address VARCHAR(255),
                               user_id BIGINT NOT NULL,
                               FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE advertisement_photos (
                                      advertisement_id BIGINT NOT NULL,
                                      photo_url VARCHAR(255) NOT NULL,
                                      photo_order INT NOT NULL,
                                      FOREIGN KEY (advertisement_id) REFERENCES advertisement(id)
);