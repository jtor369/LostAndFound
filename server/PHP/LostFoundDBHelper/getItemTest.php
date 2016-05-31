<?php
require_once 'Item.php';
require_once 'ItemDBHelper.php';

$refid = intval(54);
$item = ItemDBHelper::getItem($refid);
echo json_encode($item);

?>