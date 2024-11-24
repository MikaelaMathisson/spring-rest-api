-- src/main/resources/data.sql

-- Insert categories
INSERT INTO category (id, name, symbol, description)
VALUES (1, 'Food', '🍔', 'Foodcourt'),
       (2, 'Transport', '🚗', 'Transportation center'),
       (3, 'Entertainment', '🎬', 'Entertainment center'),
       (4, 'Health', '🏥', 'Health care'),
       (5, 'Clothes', '👗', 'Clothes store');

-- Insert places
-- Insert places
INSERT INTO place (id, name, category_id, user_id, is_public, last_modified, description, coordinates, created_at)
VALUES (1, 'Central Park Foodcourt', 1, 1, true, NOW(), 'A large public park in New York City', ST_GeomFromText('POINT(-73.968285 40.785091)', 4326), NOW()),
       (2, 'Statue of Liberty', 3, 2, true, NOW(), 'A colossal neoclassical sculpture on Liberty Island', ST_GeomFromText('POINT(-74.044500 40.689247)', 4326), NOW()),
       (3, 'Times Square', 3, 3, true, NOW(), 'A major commercial intersection and tourist destination', ST_GeomFromText('POINT(-73.985130 40.758896)', 4326), NOW()),
       (4, 'Grand Central Terminal', 2, 4, true, NOW(), 'A commuter rail terminal in New York City', ST_GeomFromText('POINT(-73.977229 40.752726)', 4326), NOW()),
       (5, 'Mount Sinai Hospital', 4, 5, true, NOW(), 'A hospital in New York City', ST_GeomFromText('POINT(-73.952285 40.789623)', 4326), NOW()),
       (6, 'Macy\'s Herald Square', 5, 6, true, NOW(), 'A department store in New York City', ST_GeomFromText('POINT(-73.987732 40.750504)', 4326), NOW());