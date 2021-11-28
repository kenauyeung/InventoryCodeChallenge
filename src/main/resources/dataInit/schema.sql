----------------------------------------
-- Schema Inventory
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Table `category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `category` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `sub_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sub_category` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NOT NULL,
  `category_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_sub_category_category`
    FOREIGN KEY (`category_id`)
    REFERENCES `category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
;


-- -----------------------------------------------------
-- Table `inventory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inventory` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NULL,
  `quantity` INT UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`id`))
;


-- -----------------------------------------------------
-- Table `inventory_sub_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `inventory_sub_category` (
  `inventory_id` INT UNSIGNED NOT NULL,
  `sub_category_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`inventory_id`, `sub_category_id`),
  CONSTRAINT `fk_inventory_has_sub_category_inventory1`
    FOREIGN KEY (`inventory_id`)
    REFERENCES `inventory` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_inventory_has_sub_category_sub_category1`
    FOREIGN KEY (`sub_category_id`)
    REFERENCES `sub_category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
;
