
-- -----------------------------------------------------
-- Data for table `category`
-- -----------------------------------------------------
INSERT INTO `category` (`id`, `name`) VALUES (1, 'Category1');
INSERT INTO `category` (`id`, `name`) VALUES (2, 'Category2');


-- -----------------------------------------------------
-- Data for table `sub_category`
-- -----------------------------------------------------
INSERT INTO `sub_category` (`id`, `name`, `category_id`) VALUES (1, 'Sub Category 1_1', 1);
INSERT INTO `sub_category` (`id`, `name`, `category_id`) VALUES (2, 'Sub Category 1_2', 1);
INSERT INTO `sub_category` (`id`, `name`, `category_id`) VALUES (3, 'Sub Category 2_1', 2);


-- -----------------------------------------------------
-- Data for table `inventory`
-- -----------------------------------------------------
INSERT INTO `inventory` (`id`, `name`, `quantity`) VALUES (1, 'Inventory 1', 0);
INSERT INTO `inventory` (`id`, `name`, `quantity`) VALUES (2, 'Inventory 2', 0);



-- -----------------------------------------------------
-- Data for table `inventory_sub_category`
-- -----------------------------------------------------
INSERT INTO `inventory_sub_category` (`inventory_id`, `sub_category_id`) VALUES (1, 1);
INSERT INTO `inventory_sub_category` (`inventory_id`, `sub_category_id`) VALUES (1, 2);
INSERT INTO `inventory_sub_category` (`inventory_id`, `sub_category_id`) VALUES (2, 1);
INSERT INTO `inventory_sub_category` (`inventory_id`, `sub_category_id`) VALUES (2, 3);