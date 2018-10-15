<?php

	$hostAddr = "localhost";
	$dbName = "mockDB";
	$dbUser = "root";
	$dbPwd = "";
	$dbPort = 3306;

	$dbPDO = new PDO("mysql:host=$hostAddr;dbname=$dbName",$dbUser,$dbPwd);
?>