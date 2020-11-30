INSERT INTO tag(`name`) VALUES ('velo' );
INSERT INTO tag(`name`) VALUES ('spa' );

INSERT INTO certificates( name, description,  price, createDate, createDateTimezone, lastUpdateDate, lastUpdateDateTimezone,duration) VALUES ('Bike Certificate', 'Very good bikes', 12.99, '2020-09-12 12:12:11', '+03:00','2020-09-12 12:12:11', '+03:00', 12);
INSERT INTO certificates(name, description,  price, createDate,createDateTimezone, lastUpdateDate, lastUpdateDateTimezone, duration)VALUES ('Spa Certificate', 'Great spa', 19.99, '2020-09-12 12:12:11', '+03:00','2020-09-12 12:12:11', '+03:00', 22);

INSERT INTO tag_m2m_gift_certificate VALUES ( 1, 1);
INSERT INTO tag_m2m_gift_certificate VALUES ( 2, 2);