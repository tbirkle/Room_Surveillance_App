<?php




//echo $filename;
//echo $newfilename;

//$path = "".getcwd()."/inform.php?message=".$newfilename;

//exec($path);
$filename = getcwd() . "/img/test.jpg";
if(file_exists($filename))
{
	$newfilename = getcwd() . "/img/img_" . date('m-d-Y-His') . ".jpg";
	rename($filename, $newfilename);
	$_GET['message'] = $newfilename;
	include('inform.php');
}


?>