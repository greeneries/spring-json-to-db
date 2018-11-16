CREATE TABLE IF NOT EXISTS food_type (
  id INT(11) NOT NULL AUTO_INCREMENT,
  type VARCHAR(45) NOT NULL,
  PRIMARY KEY (id));
  
 CREATE TABLE IF NOT EXISTS cooking_type (
  id INT(11) NOT NULL AUTO_INCREMENT,
  cooking_type VARCHAR(20) NOT NULL,
  PRIMARY KEY (id));
  
CREATE TABLE IF NOT EXISTS food (
	id INT(11) NOT NULL,
	name VARCHAR(100) NOT NULL,
	cooking_type_id INT(11) NOT NULL,
	food_type_id INT(11) NOT NULL,
	calorie FLOAT NULL DEFAULT 0,
	carbohydrate FLOAT NULL DEFAULT 0,
	protein FLOAT NULL DEFAULT 0,
	fat FLOAT NULL DEFAULT 0,
	natrium FLOAT NULL DEFAULT 0,
	hash_tag VARCHAR(45) NULL DEFAULT NULL,
	small_image_location VARCHAR(500) NULL DEFAULT NULL,
	big_image_location VARCHAR(500) NULL DEFAULT NULL,
	ingredients VARCHAR(1000) NULL DEFAULT NULL,
	like_it INT(11) NOT NULL DEFAULT 0,
	PRIMARY KEY (id),
CONSTRAINT cooking_type_id_idx FOREIGN KEY (cooking_type_id) REFERENCES cooking_type (id) ON UPDATE CASCADE,
CONSTRAINT food_type_id_idx FOREIGN KEY (cooking_type_id) REFERENCES food_type (id) ON UPDATE CASCADE);


CREATE TABLE IF NOT EXISTS ingredients (
  food_id INT(11) NOT NULL,
  name VARCHAR(45) NOT NULL,
  PRIMARY KEY (food_id, name),
  CONSTRAINT food_incredients_id FOREIGN KEY (food_id) REFERENCES food (id) ON UPDATE CASCADE);


CREATE TABLE IF NOT EXISTS comments (
  id INT(11) NOT NULL AUTO_INCREMENT,
  food_id INT(11) NOT NULL,
  name VARCHAR(45) NOT NULL,
  comments VARCHAR(1000) NOT NULL,
  password VARCHAR(45) NOT NULL,
  PRIMARY KEY (id),
CONSTRAINT main_dishes_id_idx FOREIGN KEY (food_id ) REFERENCES food (id) ON UPDATE CASCADE);

CREATE TABLE IF NOT EXISTS recipe (
  food_id INT(11) NOT NULL,
  image_location VARCHAR(500) NOT NULL,
  desc VARCHAR(1000) NOT NULL,
  PRIMARY KEY (food_id, image_location),
  CONSTRAINT food_recipe_id_idx FOREIGN KEY (food_id ) REFERENCES food (id) ON UPDATE CASCADE);
  


