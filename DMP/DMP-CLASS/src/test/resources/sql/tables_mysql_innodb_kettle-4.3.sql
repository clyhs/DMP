/*
MySQL Data Transfer
Source Host: localhost
Source Database: dmp
Target Host: localhost
Target Database: dmp
Date: 2014/9/23 14:18:06
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for tl_logging_channels
-- ----------------------------
CREATE TABLE `TL_LOGGING_CHANNELS` (
  `id_batch` int(11) DEFAULT NULL,
  `channel_id` varchar(255) DEFAULT NULL,
  `log_date` datetime DEFAULT NULL,
  `logging_object_type` varchar(255) DEFAULT NULL,
  `object_name` varchar(255) DEFAULT NULL,
  `object_copy` varchar(255) DEFAULT NULL,
  `repository_directory` varchar(255) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `object_id` varchar(255) DEFAULT NULL,
  `object_revision` varchar(255) DEFAULT NULL,
  `parent_channel_id` varchar(255) DEFAULT NULL,
  `root_channel_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tl_performance
-- ----------------------------
CREATE TABLE `TL_PERFORMANCE` (
  `id_batch` int(11) DEFAULT NULL,
  `seq_nr` int(11) DEFAULT NULL,
  `logdate` datetime DEFAULT NULL,
  `transname` varchar(255) DEFAULT NULL,
  `stepname` varchar(255) DEFAULT NULL,
  `step_copy` int(11) DEFAULT NULL,
  `lines_read` int(11) DEFAULT NULL,
  `lines_written` int(11) DEFAULT NULL,
  `lines_updated` int(11) DEFAULT NULL,
  `lines_input` int(11) DEFAULT NULL,
  `lines_output` int(11) DEFAULT NULL,
  `lines_rejected` int(11) DEFAULT NULL,
  `errors` int(11) DEFAULT NULL,
  `input_buffer_rows` int(11) DEFAULT NULL,
  `output_buffer_rows` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tl_step
-- ----------------------------
CREATE TABLE `TL_STEP` (
  `id_batch` int(11) DEFAULT NULL,
  `channel_id` varchar(255) DEFAULT NULL,
  `log_date` datetime DEFAULT NULL,
  `transname` varchar(255) DEFAULT NULL,
  `stepname` varchar(255) DEFAULT NULL,
  `step_copy` int(11) DEFAULT NULL,
  `lines_read` int(11) DEFAULT NULL,
  `lines_written` int(11) DEFAULT NULL,
  `lines_updated` int(11) DEFAULT NULL,
  `lines_input` int(11) DEFAULT NULL,
  `lines_output` int(11) DEFAULT NULL,
  `lines_rejected` int(11) DEFAULT NULL,
  `errors` int(11) DEFAULT NULL,
  `log_field` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tl_transformation
-- ----------------------------
CREATE TABLE `TL_TRANSFORMATION` (
  `id_batch` int(11) DEFAULT NULL,
  `channel_id` varchar(255) DEFAULT NULL,
  `transname` varchar(255) DEFAULT NULL,
  `status` varchar(15) DEFAULT NULL,
  `lines_read` int(11) DEFAULT NULL,
  `lines_written` int(11) DEFAULT NULL,
  `lines_updated` int(11) DEFAULT NULL,
  `lines_input` int(11) DEFAULT NULL,
  `lines_output` int(11) DEFAULT NULL,
  `lines_rejected` int(11) DEFAULT NULL,
  `errors` int(11) DEFAULT NULL,
  `startdate` datetime DEFAULT NULL,
  `enddate` datetime DEFAULT NULL,
  `logdate` datetime DEFAULT NULL,
  `depdate` datetime DEFAULT NULL,
  `replaydate` datetime DEFAULT NULL,
  `log_field` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
