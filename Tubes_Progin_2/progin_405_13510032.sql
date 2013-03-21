-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 20, 2013 at 02:52 PM
-- Server version: 5.5.16
-- PHP Version: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `progin_405_13510032`
--

-- --------------------------------------------------------

--
-- Table structure for table `assignee`
--

CREATE TABLE IF NOT EXISTS `assignee` (
  `id_usertask` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `id_task` int(11) NOT NULL,
  PRIMARY KEY (`id_usertask`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `assignee`
--

INSERT INTO `assignee` (`id_usertask`, `username`, `id_task`) VALUES
(1, 'EndyDoank', 1);

-- --------------------------------------------------------

--
-- Table structure for table `attachment`
--

CREATE TABLE IF NOT EXISTS `attachment` (
  `id_attachment` int(11) NOT NULL AUTO_INCREMENT,
  `path` varchar(100) NOT NULL,
  PRIMARY KEY (`id_attachment`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE IF NOT EXISTS `category` (
  `id_cat` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`id_cat`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`id_cat`, `name`) VALUES
(1, 'Kategori 1');

-- --------------------------------------------------------

--
-- Table structure for table `categorycreator`
--

CREATE TABLE IF NOT EXISTS `categorycreator` (
  `id_catcreator` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `id_cat` int(11) NOT NULL,
  PRIMARY KEY (`id_catcreator`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `comment`
--

CREATE TABLE IF NOT EXISTS `comment` (
  `id_comment` int(11) NOT NULL AUTO_INCREMENT,
  `id_task` int(11) NOT NULL,
  `username` varchar(20) NOT NULL,
  `time` varchar(10) NOT NULL,
  `content` varchar(140) NOT NULL,
  PRIMARY KEY (`id_comment`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `joincategory`
--

CREATE TABLE IF NOT EXISTS `joincategory` (
  `id_join` int(11) NOT NULL AUTO_INCREMENT,
  `id_cat` int(11) NOT NULL,
  `username` varchar(20) NOT NULL,
  PRIMARY KEY (`id_join`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `tag`
--

CREATE TABLE IF NOT EXISTS `tag` (
  `id_tag` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) NOT NULL,
  PRIMARY KEY (`id_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `task`
--

CREATE TABLE IF NOT EXISTS `task` (
  `id_task` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `status` tinyint(1) NOT NULL,
  `deadline` varchar(20) NOT NULL,
  `id_cat` int(11) NOT NULL,
  KEY `id_task` (`id_task`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `task`
--

INSERT INTO `task` (`id_task`, `name`, `status`, `deadline`, `id_cat`) VALUES
(1, 'Tugas 1', 0, '11 Maret 2013', 1);

-- --------------------------------------------------------

--
-- Table structure for table `taskattachment`
--

CREATE TABLE IF NOT EXISTS `taskattachment` (
  `id_task` int(11) NOT NULL,
  `id_attachment` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `taskcreator`
--

CREATE TABLE IF NOT EXISTS `taskcreator` (
  `id_taskcreator` int(11) NOT NULL AUTO_INCREMENT,
  `id_task` int(11) NOT NULL,
  `username` varchar(20) NOT NULL,
  PRIMARY KEY (`id_taskcreator`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `tasktag`
--

CREATE TABLE IF NOT EXISTS `tasktag` (
  `id_task` int(11) NOT NULL,
  `id_tag` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `username` varchar(20) NOT NULL,
  `fullname` varchar(50) NOT NULL,
  `avatar` varchar(100) NOT NULL,
  `birthday` varchar(20) NOT NULL,
  `email` varchar(35) NOT NULL,
  `password` varchar(20) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`username`, `fullname`, `avatar`, `birthday`, `email`, `password`) VALUES
('EndyDoank', 'nugroho satrijandi', '', '1992-10-27', 'nugroho.satrijandi@gmail.com', 'd41d8cd98f00b204e980');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
