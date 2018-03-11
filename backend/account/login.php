<?php

error_reporting(E_ERROR | E_PARSE);
require_once "../conn.php";

function endConn($status, $code, $HTTPcode) {
	$obj = new stdClass();
	$obj -> status = $status;
	$obj -> code = $code;
	http_response_code($HTTPcode);
	ob_clean();
	die(json_encode($obj));
}

if(!$conn) {
	endConn("error", "serverdown", "503");
}

$login = $_POST['login'];
$password = $_POST['password'];

if(!(isset($login) && isset($password))) {
	endConn("error", "badreq", "400");
}

$pg_qry = "SELECT user_id
	FROM users
	WHERE login = '$login' AND password = '$password';";

$result = pg_query($conn, $pg_qry);

$row = pg_fetch_array($result);

if($row == null) {
	if(pg_fetch_array(pg_query($conn, "SELECT user_id FROM users WHERE login = '$login';")) == null) {
		endConn("error", "noacc", "404");
	} else {
		endConn("error", "badpass", "403");
	}
}
pg_close();

$obj = new stdClass();
$obj -> status = "ok";
$obj -> code = "logged";
$obj -> user_id = $row['user_id'];
ob_clean();
http_response_code(200);
die(json_encode($obj));
?>
