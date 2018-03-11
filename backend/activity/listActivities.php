<?php 

function endConn($status, $code, $HTTPcode) {
	$obj = new stdClass();
	$obj -> status = $status;
	$obj -> code = $code;
	http_response_code($HTTPcode);
	ob_clean();
	die(json_encode($obj));
}

require_once "../conn.php";

if(!($conn)) {
	endConn("error", "serverdown", "503 Service Temporarily Unavailable");
}

if (!isset($_POST['mode'])) {
	endConn("error", "badreq", "400 Bad Request");
}
$mode = $_POST['mode'];

$pg_qry = "";

if($mode == 'all') {
	$pg_qry = "SELECT activity_id, login, kind_of_activity, activities.description, lat, lng
		FROM activities
		JOIN users ON activities.creator_id = users.user_id;";
} else {
	if (!isset($_POST['userId'])) {
		endConn("error", "badreq", "400 Bad Request");
	}
	if (!is_numeric($_POST['userId'])) {
		endConn("error", "badformat", "400 Bad Request");
	}
	$user_id = $_POST['userId'];

	$tmp_query = "SELECT *
		FROM users
		WHERE user_id = '$user_id';";
	
	if(pg_num_rows(pg_query($conn, $tmp_query)) < 1) {
		endConn("error", "badreq", "400 Bad Request");
	}

	if ($mode == 'host') {
		$pg_qry = "SELECT *
			FROM activities as A
			JOIN users AS U ON A.creator_id = U.user_id
			WHERE creator_id = '$user_id';";
	} else if ($mode == 'guest') {
		$pg_qry = "SELECT DISTINCT A.*, U.login 
			FROM participants as P
			JOIN activities as A ON A.activity_id = P.activity_id
			JOIN users as U ON A.creator_id=U.user_id
			WHERE P.user_id = '$user_id';";
	} 
	else {
		endConn("error", "badreq", "400 Bad Request");
	}
}

$result = pg_query($conn, $pg_qry);
$response = array();

while ($row = pg_fetch_array($result)){
	foreach($row as $key => $value) {
	   if(is_numeric($key)) {
			unset($row[$key]);
		}
	}
	array_push($response,$row);
}
pg_close();

$obj = new stdClass();
$obj -> status = "ok";
$obj -> activities = $response;

http_response_code(200);
ob_clean();
die(json_encode($obj));
?>
