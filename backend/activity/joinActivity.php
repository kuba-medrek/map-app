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

if (!(isset($_POST['userId']) && isset($_POST['activityId']))) {
	endConn("error", "badreq", "400 Bad Request");
}

if(!(is_numeric($_POST['userId']) && is_numeric($_POST['activityId']))) {
    endConn("error", "badformat", "400 Bad Request");
}
$user_id = $_POST['userId'];
$activity_id = $_POST['activityId'];   
 
$pg_qry = "INSERT INTO participants(activity_id, user_id)
    VALUES ('$activity_id', '$user_id'); ";

$result = pg_query($conn, $pg_qry);
if($result == false) {
    endConn("error", "badreq", "400 Bad Request");
}

$pg_qry2 = "SELECT *
    FROM activities
    JOIN users ON activities.creator_id = users.user_id
    WHERE activity_id = '$activity_id';";

$result2 = pg_query($conn, $pg_qry2);
$response = array();

$row = pg_fetch_array($result2);
foreach($row as $key => $value) {
    if(is_numeric($key)) {
        unset($row[$key]);
    }
}

$response = $row;
pg_close();

$obj = new stdClass();
$obj -> status = "ok";
$obj -> activity = $response;

http_response_code(200);
ob_clean();
die(json_encode($obj));
?>