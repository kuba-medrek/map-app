<?php 
error_reporting(0);

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

if (!(isset($_POST['activityName']) && isset($_POST['creatorId']) && isset($_POST['lat']) && isset($_POST['lng']) && isset($_POST['kindOfActivity']) && isset($_POST['activityDescription']))) {
	endConn("error", "badreq", "400 Bad Request");
}
    $name = $_POST['activityName'];
    $creator_id = $_POST['creatorId'];
    if(!(is_numeric($_POST['lat']) && is_numeric($_POST['lng']))) {
        endConn("error", "badformat", "400 Bad Request");
    }
    $lat = $_POST['lat'];
    $lng = $_POST['lng'];
    $kind_of_activity = $_POST['kindOfActivity'];
    $description = $_POST['activityDescription'];

$pg_qry = "INSERT INTO activities(activity_name, creator_id, lat, lng, kind_of_activity, description)
    VALUES ('$name', '$creator_id', '$lat', '$lng','$kind_of_activity','$description');";

$result = pg_query($conn, $pg_qry);

$pg_qry = "SELECT A.activity_id, A.activity_name, U.login AS creator_login, A.kind_of_activity, A.description, A.lat, A.lng
    FROM activities AS A
    JOIN users AS U ON A.creator_id = U.user_id
    WHERE A.activity_name = '$name' AND A.creator_id = '$creator_id' AND A.lat = '$lat' AND A.lng = '$lng' AND A.kind_of_activity = '$kind_of_activity' AND A.description = '$description';";

$result = pg_query($conn,$pg_qry);
$response = array();

$row = pg_fetch_array($result);
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

http_response_code(201);
ob_clean();
die(json_encode($obj));
?>
