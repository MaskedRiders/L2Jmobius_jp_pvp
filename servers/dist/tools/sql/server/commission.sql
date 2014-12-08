CREATE TABLE IF NOT EXISTS `commission` (
  `object_id` int(11) NOT NULL,
  `owner_id` int(11) NOT NULL,
  `item_id` int(7) NOT NULL,
  `count` bigint(20) NOT NULL,
  `enchant_level` int(11) NOT NULL,
  `loc` varchar(32) NOT NULL,
  `loc_data` int(11) NOT NULL,
  `life_time` int(11) NOT NULL,  
  `augmentation_id` int(11) NOT NULL,
  `attribute_fire` int(11) NOT NULL,
  `attribute_water` int(11) NOT NULL,
  `attribute_wind` int(11) NOT NULL,
  `attribute_earth` int(11) NOT NULL,
  `attribute_holy` int(11) NOT NULL,
  `attribute_unholy` int(11) NOT NULL,
  `custom_type1` int(5) NOT NULL,
  `custom_type2` int(5) NOT NULL,
  `custom_flags` int(11) NOT NULL,
  `agathion_energy` int(11) NOT NULL,
  `price` bigint(20) NOT NULL,
  `end_time` int(11) NOT NULL,  
  `cat` int(2) NOT NULL,
  `item_name` VARCHAR(35) CHARACTER SET UTF8 NOT NULL DEFAULT '',
  PRIMARY KEY  (`object_id`),
  KEY `owner_id` (`owner_id`),
  KEY `loc` (`loc`),
  KEY `item_id` (`item_id`)
) ENGINE=InnoDB;