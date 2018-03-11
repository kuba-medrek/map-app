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
    endConn("error", "serverdown", "503");
}

if (!(isset($_POST['userLogin']))) {
	endConn("error", "badreq", "400");
}

$user_login = $_POST['userLogin'];

$pg_qry ="SELECT *
    FROM users
    WHERE login = '$user_login';";

$result = pg_query($conn, $pg_qry);
$response = array();

while ($row = pg_fetch_array($result)){
    foreach ($row as $key => $value) {
        if (is_int($key)) {
            unset($row[$key]);
        }
    }
    $response += $row;
}
pg_close();

if(count($response) < 1) {
    endConn("error", "noacc", "404");
}

$obj = new stdClass();
$obj -> status = "ok";
$obj -> data = $response;

http_response_code(200);
ob_clean();
echo(json_encode($obj));
?>
