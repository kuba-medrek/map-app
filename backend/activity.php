<?php 

function endConn($status, $code, $HTTPcode) {
	$obj = new stdClass();
	$obj -> status = $status;
	$obj -> code = $code;
	http_response_code($HTTPcode);
	ob_clean();
	die(json_encode($obj));
}

require_once "./conn.php";

if(!($conn)) {
	endConn("error", "serverdown", "503 Service Temporarily Unavailable");
}

if (!(isset($_POST['mode']) && isset($_POST['activityId']))) {
	endConn("error", "badreq", "400 Bad Request");
}

$mode = $_POST['mode'];

if (!is_numeric($_POST['activityId'])) {
	endConn("error", "badformat", "400 Bad Request");
}
$activity_id = $_POST['activityId'];

if($mode == 'details') {
	$pg_qry ="SELECT *
		FROM activities
		WHERE activity_id = '$activity_id';";

} else if ($mode == 'guests') {
	$pg_qry ="SELECT U.*
		FROM activities as A
		JOIN participants as P ON P.activity_id = A.activity_id
		JOIN users as U ON P.user_id = U.user_id
		WHERE A.activity_id = '$activity_id';";
} else {
	endConn("error", "badreq", "400 Bad Request");
}

$result = pg_query($conn, $pg_qry);

if(pg_num_rows($result) < 1) {
	endConn("error", "badreq", "400 Bad Request");
}

$response = array();

while($row = pg_fetch_array($result)) {
	foreach ($row as $key => $value) {
		if (is_int($key)) {
			unset($row[$key]);
		}
	}
	array_push($response, $row);
}
pg_close();

$obj = new stdClass();
$obj -> status = "ok";
$obj -> data = $response;

http_response_code(200);
ob_clean();
echo(json_encode($obj));
?>
