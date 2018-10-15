<?php
	require_once("redmineDb.php");

	if (isset($_POST)) {
		$varFN = $_REQUEST["selectFn"];
		if ($varFN == "searchStudent") {
			if ($_REQUEST["studId"] != "") {
				$strStudId = $_REQUEST["studId"];
				$strQry = "select * from `users`  where login = :login";
				$stmt   = $dbPDO->prepare($strQry);
				$stmt->execute(array('login' => $strStudId));
			}
			else{
				$strStudName = $_REQUEST["studName"];
				$strQry = "select * from `users`  where firstname like :name or lastname like :name";
				$stmt   = $dbPDO->prepare($strQry);
				$stmt->execute(array('name' => $strStudName));
			}
			$recordSetObj = $stmt->fetchAll(PDO::FETCH_OBJ);
			
			echo json_encode($recordSetObj);
		}
	}
?>