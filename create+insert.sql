-- phpMyAdmin SQL Dump
-- version 5.1.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 02, 2022 at 07:09 AM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 7.4.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `shoestation`
--

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `productID` int(11) NOT NULL,
  `UserID` varchar(11) NOT NULL,
  `Quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `cart`
--

INSERT INTO `cart` (`productID`, `UserID`, `Quantity`) VALUES
(3, 'CU012', 1);

-- --------------------------------------------------------

--
-- Table structure for table `detailtransaction`
--

CREATE TABLE `detailtransaction` (
  `transactionID` int(11) NOT NULL,
  `productID` int(11) NOT NULL,
  `Quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `detailtransaction`
--

INSERT INTO `detailtransaction` (`transactionID`, `productID`, `Quantity`) VALUES
(4, 3, 1),
(4, 6, 1),
(6, 3, 2),
(6, 6, 3),
(7, 3, 1),
(7, 7, 1),
(8, 7, 2);

-- --------------------------------------------------------

--
-- Table structure for table `headertransaction`
--

CREATE TABLE `headertransaction` (
  `transactionID` int(11) NOT NULL,
  `userID` varchar(11) NOT NULL,
  `Time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `headertransaction`
--

INSERT INTO `headertransaction` (`transactionID`, `userID`, `Time`) VALUES
(4, 'CU012', '2022-05-31 03:05:09'),
(5, 'CU012', '2022-05-31 12:35:19'),
(6, 'CU012', '2022-05-31 13:45:12'),
(7, 'CU012', '2022-06-01 14:18:32'),
(8, 'CU012', '2022-06-02 04:20:33');

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `productID` int(11) NOT NULL,
  `productName` varchar(20) NOT NULL,
  `productStock` int(11) NOT NULL,
  `productTypeID` int(11) NOT NULL,
  `productPrice` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`productID`, `productName`, `productStock`, `productTypeID`, `productPrice`) VALUES
(3, 'Megaboost 21', 7, 4, 100000),
(6, 'Chronosphere1234', 0, 4, 1000000),
(7, 'Marioboro', 5, 1, 300000),
(15, 'TestInsert', 59, 1, 190000);

-- --------------------------------------------------------

--
-- Table structure for table `producttype`
--

CREATE TABLE `producttype` (
  `productTypeID` int(11) NOT NULL,
  `productTypeName` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `producttype`
--

INSERT INTO `producttype` (`productTypeID`, `productTypeName`) VALUES
(1, 'Futsal'),
(3, 'Volleyball'),
(4, 'Badminton'),
(9, 'PinkPonk4');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `userID` varchar(11) NOT NULL,
  `userName` varchar(20) NOT NULL,
  `Age` int(11) NOT NULL,
  `Email` varchar(30) NOT NULL,
  `Gender` varchar(10) NOT NULL,
  `Password` varchar(20) NOT NULL,
  `userType` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`userID`, `userName`, `Age`, `Email`, `Gender`, `Password`, `userType`) VALUES
('CU012', 'asdfg', 55, 'asdqwe@gmail.com', 'Male', 'asdasd', 'User'),
('CU111', 'test123', 55, 'drakenlabo@gmail.com', 'Male', 'Lol123', 'User'),
('CU123', 'asdfg123', 11, 'asd@gmail.com', 'Female', 'lol123', 'User'),
('CU221', 'asdzxc', 11, 'asd@gmail.com', 'Female', 'asdasd', 'User'),
('CU456', 'asdasd', 11, 'asdasda', 'Male', 'asdasdas', 'User'),
('CU555', 'asd123', 12, 'asd@gmail.com', 'Female', 'qwerty', 'User'),
('CU761', 'asdfg1XD', 12, 'drakenlabo@gmail.com', 'Male', 'draken021', 'User'),
('CU765', 'admin', 55, 'admin@gmail.com', 'Male', 'admin', 'Admin'),
('CU987', 'asdasdzxc', 13, 'asd@g.com', 'Male', 'dsadsa', 'User');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`productID`,`UserID`),
  ADD KEY `User1_FK` (`UserID`);

--
-- Indexes for table `detailtransaction`
--
ALTER TABLE `detailtransaction`
  ADD PRIMARY KEY (`transactionID`,`productID`),
  ADD KEY `Product12_fk` (`productID`);

--
-- Indexes for table `headertransaction`
--
ALTER TABLE `headertransaction`
  ADD PRIMARY KEY (`transactionID`),
  ADD KEY `User2_FK` (`userID`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`productID`),
  ADD KEY `ProductType_FK` (`productTypeID`);

--
-- Indexes for table `producttype`
--
ALTER TABLE `producttype`
  ADD PRIMARY KEY (`productTypeID`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `headertransaction`
--
ALTER TABLE `headertransaction`
  MODIFY `transactionID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `productID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `producttype`
--
ALTER TABLE `producttype`
  MODIFY `productTypeID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `Product_FK` FOREIGN KEY (`productID`) REFERENCES `product` (`productID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `User1_FK` FOREIGN KEY (`UserID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `detailtransaction`
--
ALTER TABLE `detailtransaction`
  ADD CONSTRAINT `Product12_fk` FOREIGN KEY (`productID`) REFERENCES `product` (`productID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Transaction_fk` FOREIGN KEY (`transactionID`) REFERENCES `headertransaction` (`transactionID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `headertransaction`
--
ALTER TABLE `headertransaction`
  ADD CONSTRAINT `User2_FK` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `product`
--
ALTER TABLE `product`
  ADD CONSTRAINT `ProductType_FK` FOREIGN KEY (`productTypeID`) REFERENCES `producttype` (`productTypeID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
