<?php
if (isset($_GET["message"])) {
    $message = $_GET["message"];
    $allowed = explode(";", $_GET["allowed"]);
    
    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    
    include_once './gcm.php';
    $gcm = new GCM();
    
    // get all users
    $users       = mysql_query("select * FROM users");
    $no_of_users = mysql_num_rows($users);
    
    $registatoin_ids = array();
    
    while ($row = mysql_fetch_array($users)) {
         array_push($registatoin_ids, $row["gcm"]);
    }
    
    $message = array(
        "message" => $message
    );
        
    echo $gcm->send_notification($registatoin_ids, $message);
}

?>
