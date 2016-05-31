<?php
require_once 'Item.php';
require_once 'ItemMain.php';
require_once 'Location.php';
require_once 'Tag.php';

$refid = 555;
$itembase = new ItemMain($refid, "Some Description",0, "4121223", "");
$location = new Location($refid,111.111,222.222,333.333);

$taglist = array();

array_push($taglist, new Tag($refid, "TagA"));
array_push($taglist, new Tag($refid, "TagB"));
array_push($taglist, new Tag($refid, "TagC"));

$item = Item::combine($itembase, $taglist, $location);
$jsonItem = json_encode($item);

echo $jsonItem;
//var_dump($item);




?>