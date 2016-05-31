
<?php 
require_once 'grabJsonItem.php';
require_once 'ItemDBHelper.php';

$item = grabJsonItem();
if ($item != null)
{
$items = ItemDBHelper::searchForItems($item);
echo json_encode($items);
}else
{
	echo '[]';
}

//var_dump($item);
//processItem($item);


?>