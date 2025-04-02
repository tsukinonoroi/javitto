CREATE TABLE advertisements (
                                id BIGSERIAL PRIMARY KEY,
                                title VARCHAR(255) NOT NULL,
                                description TEXT,
                                cost NUMERIC(10,2) NOT NULL,
                                parent_category VARCHAR(50) NOT NULL, -- Enum
                                sub_category VARCHAR(50) NOT NULL, -- Enum
                                date_of_creation TIMESTAMP DEFAULT now(),
                                address VARCHAR(255),
                                user_id BIGINT NOT NULL,
                                CONSTRAINT fk_advertisement_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE advertisement_photos (
                                      id BIGSERIAL PRIMARY KEY,
                                      advertisement_id BIGINT NOT NULL,
                                      photo_url VARCHAR(500) NOT NULL,
                                      photo_order INT NOT NULL,
                                      CONSTRAINT fk_advertisement_photo FOREIGN KEY (advertisement_id) REFERENCES advertisements(id) ON DELETE CASCADE
);