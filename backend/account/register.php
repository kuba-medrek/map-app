<?php 
error_reporting(0);

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
	endConn("error", "serverdown", "503 Service Temporarily Unavailable");
}

if (!(isset($_POST['login']) && isset($_POST['password']) && isset($_POST['email']))) {
	endConn("error", "badreq", "400");
}

$login = $_POST['login'];
$password = $_POST['password'];
$email = $_POST['email'];

$pg_qry = "INSERT INTO users (login, password, email, name, description, birthday, lastname) VALUES ('$login', '$password','$email', NULL, NULL, NULL, NULL)";

$result = pg_query($conn,$pg_qry);

if($result) {
	endConn("ok", "registered", "200");
} else {
	if(pg_fetch_array(pg_query($conn, "SELECT user_id FROM users WHERE email = '$email';")) != null) {
		endConn("error", "emaildupl", "404");
	} else if(pg_fetch_array(pg_query($conn, "SELECT user_id FROM users WHERE login = '$login';")) != null) {
		endConn("error", "logindupl", "404");
	}
}
pg_close();

?>
