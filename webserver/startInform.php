<?php




//echo $filename;
//echo $newfilename;

//$path = "".getcwd()."/inform.php?message=".$newfilename;

//exec($path);
$filename = getcwd() . "/test.jpg";
echo "Hello";
if(file_exists($filename))
{
	echo "copy";
	$folder = "/img/img_" . date('m-d-Y-His') . ".jpg";
	$newfilename = getcwd() . $folder;
	rename($filename, $newfilename);
	//$_GET['message'] = $newfilename;
	$_GET['message'] = $folder;
	include('inform.php');
}


?>
