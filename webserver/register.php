<?php

// response json
$json = array();

/**
 * Registering a user device
 * Store reg id in users table
 */
if (isset($_GET["number"]) && isset($_GET["gcm"])) {
    $number = $_GET["number"];
    $gcm    = $_GET["gcm"]; // GCM Registration ID
    
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
    
    // connecting to db
    $db = new DB_CONNECT();
    
    // insert user into database
    $result = mysql_query("REPLACE INTO users SET number = '$number', gcm = '$gcm'");
	
	// check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "User successfully created or updated.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
    // required field is missing
		$response["success"] = 0;
		$response["message"] = "Database error!";
	 
		// echoing JSON response
		echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing!";
 
    // echoing JSON response
    echo json_encode($response);
}

?>
